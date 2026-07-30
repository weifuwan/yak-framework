package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.dto.tenant.TenantMemberAssignDTO;
import io.yak.framework.security.common.dto.tenant.TenantSaveDTO;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.common.po.TenantPO;
import io.yak.framework.security.common.po.UserTenantPO;
import io.yak.framework.security.common.vo.tenant.TenantMemberVO;
import io.yak.framework.security.common.vo.tenant.TenantVO;
import io.yak.framework.security.dao.TenantRepository;
import io.yak.framework.security.dao.UserDao;
import io.yak.framework.security.dao.UserTenantRepository;
import io.yak.framework.security.service.TenantService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/** 默认租户管理服务实现。 */
@Service("yakSecurityTenantServiceImpl")
public class TenantServiceImpl implements TenantService {

  private static final Pattern TENANT_CODE_PATTERN =
      Pattern.compile("^[A-Za-z0-9][A-Za-z0-9_-]{0,63}$");

  private final TenantRepository tenantRepository;
  private final UserTenantRepository userTenantRepository;
  private final UserDao userDao;

  public TenantServiceImpl(
      TenantRepository tenantRepository,
      UserTenantRepository userTenantRepository,
      UserDao userDao) {
    this.tenantRepository = tenantRepository;
    this.userTenantRepository = userTenantRepository;
    this.userDao = userDao;
  }

  @Override
  @Transactional(
      transactionManager = "yakSecurityTransactionManager",
      rollbackFor = Exception.class)
  public TenantVO create(TenantSaveDTO tenantSaveDTO) {
    validate(tenantSaveDTO, null);
    TenantPO tenantPO = new TenantPO();
    copy(tenantSaveDTO, tenantPO);
    if (tenantPO.getStatus() == null) {
      tenantPO.setStatus(1);
    }
    tenantRepository.insert(tenantPO);
    return toVO(tenantPO);
  }

  @Override
  @Transactional(
      transactionManager = "yakSecurityTransactionManager",
      rollbackFor = Exception.class)
  public TenantVO update(TenantSaveDTO tenantSaveDTO) {
    if (tenantSaveDTO == null
        || tenantSaveDTO.getId() == null) {
      throw new IllegalArgumentException(
          "租户标识不能为空");
    }

    TenantPO existing =
        requiredTenant(tenantSaveDTO.getId());
    validate(tenantSaveDTO, existing.getId());
    copy(tenantSaveDTO, existing);
    tenantRepository.update(existing);
    return toVO(existing);
  }

  @Override
  @Transactional(
      transactionManager = "yakSecurityTransactionManager",
      rollbackFor = Exception.class)
  public TenantVO upsertExternal(
      TenantSaveDTO tenantSaveDTO) {
    if (tenantSaveDTO == null
        || !StringUtils.hasText(
            tenantSaveDTO.getExternalSystem())
        || !StringUtils.hasText(
            tenantSaveDTO.getExternalTenantId())) {
      throw new IllegalArgumentException(
          "外部系统和外部租户标识不能为空");
    }

    TenantPO existing =
        tenantRepository.selectByExternalIdentity(
            tenantSaveDTO.getExternalSystem().trim(),
            tenantSaveDTO.getExternalTenantId().trim());
    if (existing == null) {
      tenantSaveDTO.setId(null);
      return create(tenantSaveDTO);
    }

    tenantSaveDTO.setId(existing.getId());
    return update(tenantSaveDTO);
  }

  @Override
  public TenantVO detail(Long tenantId) {
    return toVO(requiredTenant(tenantId));
  }

  @Override
  public List<TenantVO> list() {
    return tenantRepository.selectAll().stream()
        .map(this::toVO)
        .collect(Collectors.toList());
  }

  @Override
  public List<TenantVO> listByUserId(Long userId) {
    if (userId == null) {
      return Collections.emptyList();
    }

    List<TenantVO> result = new ArrayList<>();
    for (UserTenantPO membership
        : userTenantRepository.selectByUserId(userId)) {
      TenantPO tenant =
          tenantRepository.selectById(
              membership.getTenantId());
      if (tenant != null
          && Integer.valueOf(1).equals(
              tenant.getStatus())) {
        result.add(toVO(tenant));
      }
    }
    return result;
  }

  @Override
  public List<TenantMemberVO> listMembers(
      Long tenantId) {
    requiredTenant(tenantId);
    return userTenantRepository
        .selectByTenantId(tenantId)
        .stream()
        .map(this::toMemberVO)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(
      transactionManager = "yakSecurityTransactionManager",
      rollbackFor = Exception.class)
  public void replaceMembers(
      Long tenantId,
      TenantMemberAssignDTO assignDTO) {
    requiredTenant(tenantId);

    List<Long> userIds = normalize(
        assignDTO == null
            ? null
            : assignDTO.getUserIdList());
    Set<Long> adminUserIds = new LinkedHashSet<>(
        normalize(assignDTO == null
            ? null
            : assignDTO.getAdminUserIdList()));
    Set<Long> defaultUserIds = new LinkedHashSet<>(
        normalize(assignDTO == null
            ? null
            : assignDTO.getDefaultUserIdList()));

    if (!userIds.containsAll(adminUserIds)
        || !userIds.containsAll(defaultUserIds)) {
      throw new IllegalArgumentException(
          "管理员和默认租户用户必须包含在成员列表中");
    }

    for (Long userId : userIds) {
      User user = userDao.selectByUserId(userId);
      if (user == null) {
        throw new IllegalArgumentException(
            "用户不存在: " + userId);
      }
    }

    List<UserTenantPO> memberships =
        userIds.stream()
            .map(userId -> buildMembership(
                tenantId,
                userId,
                adminUserIds.contains(userId),
                defaultUserIds.contains(userId)))
            .collect(Collectors.toList());

    userTenantRepository.replaceTenantMembers(
        tenantId,
        memberships,
        new ArrayList<>(defaultUserIds));
  }

  @Override
  @Transactional(
      transactionManager = "yakSecurityTransactionManager",
      rollbackFor = Exception.class)
  public void delete(Long tenantId) {
    requiredTenant(tenantId);
    long memberCount =
        userTenantRepository.countByTenantId(
            tenantId);
    if (memberCount > 0) {
      throw new IllegalStateException(
          "租户仍关联 " + memberCount
              + " 个用户，不能删除");
    }
    tenantRepository.delete(tenantId);
  }

  private void validate(
      TenantSaveDTO tenantSaveDTO,
      Long currentTenantId) {
    if (tenantSaveDTO == null) {
      throw new IllegalArgumentException(
          "租户参数不能为空");
    }

    String tenantCode =
        trim(tenantSaveDTO.getTenantCode());
    String tenantName =
        trim(tenantSaveDTO.getTenantName());
    if (!StringUtils.hasText(tenantCode)
        || !TENANT_CODE_PATTERN
            .matcher(tenantCode)
            .matches()) {
      throw new IllegalArgumentException(
          "租户编码仅支持 1-64 位字母、数字、下划线和中划线");
    }
    if (!StringUtils.hasText(tenantName)
        || tenantName.length() > 128) {
      throw new IllegalArgumentException(
          "租户名称不能为空且不能超过 128 个字符");
    }

    TenantPO codeOwner =
        tenantRepository.selectByCode(tenantCode);
    if (codeOwner != null
        && !Objects.equals(
            codeOwner.getId(),
            currentTenantId)) {
      throw new IllegalArgumentException(
          "租户编码已存在");
    }

    String externalSystem =
        trimToNull(
            tenantSaveDTO.getExternalSystem());
    String externalTenantId =
        trimToNull(
            tenantSaveDTO.getExternalTenantId());
    if (StringUtils.hasText(externalSystem)
        != StringUtils.hasText(externalTenantId)) {
      throw new IllegalArgumentException(
          "外部系统和外部租户标识必须同时填写");
    }
    if (StringUtils.hasText(externalSystem)) {
      TenantPO externalOwner =
          tenantRepository
              .selectByExternalIdentity(
                  externalSystem,
                  externalTenantId);
      if (externalOwner != null
          && !Objects.equals(
              externalOwner.getId(),
              currentTenantId)) {
        throw new IllegalArgumentException(
            "外部租户标识已绑定");
      }
    }

    tenantSaveDTO.setTenantCode(tenantCode);
    tenantSaveDTO.setTenantName(tenantName);
    tenantSaveDTO.setExternalSystem(
        externalSystem);
    tenantSaveDTO.setExternalTenantId(
        externalTenantId);
    Integer status = tenantSaveDTO.getStatus();
    if (status != null
        && status != 1
        && status != 2) {
      throw new IllegalArgumentException(
          "租户状态只支持 1（启用）或 2（禁用）");
    }

    String description =
        trim(tenantSaveDTO.getDescription());
    tenantSaveDTO.setDescription(
        description == null ? "" : description);
  }

  private TenantPO requiredTenant(Long tenantId) {
    TenantPO tenant =
        tenantRepository.selectById(tenantId);
    if (tenant == null) {
      throw new IllegalArgumentException(
          "租户不存在");
    }
    return tenant;
  }

  private List<Long> normalize(List<Long> values) {
    if (values == null) {
      return Collections.emptyList();
    }
    return values.stream()
        .filter(Objects::nonNull)
        .distinct()
        .collect(Collectors.toList());
  }

  private UserTenantPO buildMembership(
      Long tenantId,
      Long userId,
      boolean administrator,
      boolean defaultTenant) {
    UserTenantPO membership =
        new UserTenantPO();
    membership.setTenantId(tenantId);
    membership.setUserId(userId);
    membership.setMemberType(
        administrator ? 1 : 0);
    membership.setDefaultTenant(
        defaultTenant);
    membership.setStatus(1);
    return membership;
  }

  private void copy(
      TenantSaveDTO source,
      TenantPO target) {
    target.setTenantCode(
        source.getTenantCode());
    target.setTenantName(
        source.getTenantName());
    target.setExternalSystem(
        source.getExternalSystem());
    target.setExternalTenantId(
        source.getExternalTenantId());
    target.setDescription(
        source.getDescription());
    if (source.getStatus() != null) {
      target.setStatus(source.getStatus());
    }
  }

  private TenantVO toVO(TenantPO tenantPO) {
    return TenantVO.builder()
        .id(tenantPO.getId())
        .tenantCode(tenantPO.getTenantCode())
        .tenantName(tenantPO.getTenantName())
        .externalSystem(
            tenantPO.getExternalSystem())
        .externalTenantId(
            tenantPO.getExternalTenantId())
        .status(tenantPO.getStatus())
        .description(tenantPO.getDescription())
        .createTime(tenantPO.getCreateTime())
        .updateTime(tenantPO.getUpdateTime())
        .build();
  }

  private TenantMemberVO toMemberVO(
      UserTenantPO membership) {
    return TenantMemberVO.builder()
        .userId(membership.getUserId())
        .tenantId(membership.getTenantId())
        .memberType(membership.getMemberType())
        .defaultTenant(
            membership.getDefaultTenant())
        .status(membership.getStatus())
        .externalMembershipId(
            membership.getExternalMembershipId())
        .build();
  }

  private String trim(String value) {
    return value == null ? null : value.trim();
  }

  private String trimToNull(String value) {
    String trimmed = trim(value);
    return StringUtils.hasText(trimmed)
        ? trimmed
        : null;
  }
}
