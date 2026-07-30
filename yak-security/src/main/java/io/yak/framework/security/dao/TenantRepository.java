package io.yak.framework.security.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.yak.framework.security.common.po.TenantPO;
import io.yak.framework.security.dao.mapper.TenantMapper;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/** 租户数据访问组件。 */
@Repository
@RequiredArgsConstructor
public class TenantRepository {

  private final TenantMapper tenantMapper;

  public TenantPO selectById(Long tenantId) {
    return tenantId == null ? null : tenantMapper.selectById(tenantId);
  }

  public TenantPO selectByCode(String tenantCode) {
    if (!StringUtils.hasText(tenantCode)) {
      return null;
    }
    return tenantMapper.selectOne(
        Wrappers.<TenantPO>lambdaQuery()
            .eq(TenantPO::getTenantCode, tenantCode)
            .last("LIMIT 1"));
  }

  public TenantPO selectByExternalIdentity(
      String externalSystem,
      String externalTenantId) {
    if (!StringUtils.hasText(externalSystem)
        || !StringUtils.hasText(externalTenantId)) {
      return null;
    }
    return tenantMapper.selectOne(
        Wrappers.<TenantPO>lambdaQuery()
            .eq(TenantPO::getExternalSystem, externalSystem)
            .eq(TenantPO::getExternalTenantId, externalTenantId)
            .last("LIMIT 1"));
  }

  public List<TenantPO> selectAll() {
    List<TenantPO> tenants = tenantMapper.selectList(
        Wrappers.<TenantPO>lambdaQuery()
            .orderByAsc(TenantPO::getTenantName)
            .orderByAsc(TenantPO::getId));
    return tenants == null ? Collections.emptyList() : tenants;
  }

  public int insert(TenantPO tenantPO) {
    return tenantMapper.insert(tenantPO);
  }

  public int update(TenantPO tenantPO) {
    return tenantMapper.updateById(tenantPO);
  }

  public int delete(Long tenantId) {
    return tenantId == null ? 0 : tenantMapper.deleteById(tenantId);
  }
}
