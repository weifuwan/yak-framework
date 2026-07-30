package io.yak.framework.security.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.yak.framework.security.common.po.UserTenantPO;
import io.yak.framework.security.dao.mapper.UserTenantMapper;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** 用户租户成员关系数据访问组件。 */
@Repository
@RequiredArgsConstructor
public class UserTenantRepository {

  private final UserTenantMapper userTenantMapper;

  public UserTenantPO selectMembership(Long userId, Long tenantId) {
    if (userId == null || tenantId == null) {
      return null;
    }
    return userTenantMapper.selectOne(
        Wrappers.<UserTenantPO>lambdaQuery()
            .eq(UserTenantPO::getUserId, userId)
            .eq(UserTenantPO::getTenantId, tenantId)
            .last("LIMIT 1"));
  }

  public List<UserTenantPO> selectByUserId(Long userId) {
    if (userId == null) {
      return Collections.emptyList();
    }
    List<UserTenantPO> memberships = userTenantMapper.selectList(
        Wrappers.<UserTenantPO>lambdaQuery()
            .eq(UserTenantPO::getUserId, userId)
            .eq(UserTenantPO::getStatus, 1)
            .orderByDesc(UserTenantPO::getDefaultTenant)
            .orderByAsc(UserTenantPO::getTenantId));
    return memberships == null ? Collections.emptyList() : memberships;
  }

  public List<UserTenantPO> selectByTenantId(Long tenantId) {
    if (tenantId == null) {
      return Collections.emptyList();
    }
    List<UserTenantPO> memberships = userTenantMapper.selectList(
        Wrappers.<UserTenantPO>lambdaQuery()
            .eq(UserTenantPO::getTenantId, tenantId)
            .orderByDesc(UserTenantPO::getMemberType)
            .orderByAsc(UserTenantPO::getUserId));
    return memberships == null ? Collections.emptyList() : memberships;
  }

  public long countByTenantId(Long tenantId) {
    if (tenantId == null) {
      return 0L;
    }
    Long count = userTenantMapper.selectCount(
        Wrappers.<UserTenantPO>lambdaQuery()
            .eq(UserTenantPO::getTenantId, tenantId));
    return count == null ? 0L : count;
  }

  @Transactional(
      transactionManager = "yakSecurityTransactionManager",
      rollbackFor = Exception.class)
  public void replaceTenantMembers(
      Long tenantId,
      List<UserTenantPO> memberships,
      List<Long> defaultUserIds) {
    userTenantMapper.delete(
        Wrappers.<UserTenantPO>lambdaQuery()
            .eq(UserTenantPO::getTenantId, tenantId));

    if (defaultUserIds != null && !defaultUserIds.isEmpty()) {
      userTenantMapper.update(
          null,
          Wrappers.<UserTenantPO>lambdaUpdate()
              .set(UserTenantPO::getDefaultTenant, Boolean.FALSE)
              .in(UserTenantPO::getUserId, defaultUserIds));
    }

    if (memberships == null) {
      return;
    }
    memberships.forEach(userTenantMapper::insert);
  }

  public int deleteByTenantId(Long tenantId) {
    if (tenantId == null) {
      return 0;
    }
    return userTenantMapper.delete(
        Wrappers.<UserTenantPO>lambdaQuery()
            .eq(UserTenantPO::getTenantId, tenantId));
  }
}
