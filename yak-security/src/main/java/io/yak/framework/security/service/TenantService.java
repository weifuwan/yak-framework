package io.yak.framework.security.service;

import io.yak.framework.security.common.dto.tenant.TenantMemberAssignDTO;
import io.yak.framework.security.common.dto.tenant.TenantSaveDTO;
import io.yak.framework.security.common.vo.tenant.TenantMemberVO;
import io.yak.framework.security.common.vo.tenant.TenantVO;
import java.util.List;

/** 租户管理服务。 */
public interface TenantService {

  TenantVO create(TenantSaveDTO tenantSaveDTO);

  TenantVO update(TenantSaveDTO tenantSaveDTO);

  TenantVO upsertExternal(TenantSaveDTO tenantSaveDTO);

  TenantVO detail(Long tenantId);

  List<TenantVO> list();

  List<TenantVO> listByUserId(Long userId);

  List<TenantMemberVO> listMembers(Long tenantId);

  void replaceMembers(
      Long tenantId,
      TenantMemberAssignDTO assignDTO);

  void delete(Long tenantId);
}
