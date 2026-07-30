package io.yak.framework.security.common.vo.tenant;

import lombok.Builder;
import lombok.Data;

/** 租户成员关系视图对象。 */
@Data
@Builder
public class TenantMemberVO {

  private Long userId;
  private Long tenantId;
  private Integer memberType;
  private Boolean defaultTenant;
  private Integer status;
  private String externalMembershipId;
}
