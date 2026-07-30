package io.yak.framework.security.common.dto.tenant;

import lombok.Data;

/** 租户新增、更新及外部目录同步参数。 */
@Data
public class TenantSaveDTO {

  /** 租户标识，新增时为空。 */
  private Long id;

  /** 稳定租户编码。 */
  private String tenantCode;

  /** 租户名称。 */
  private String tenantName;

  /** 外部目录系统标识。 */
  private String externalSystem;

  /** 外部目录中的租户标识。 */
  private String externalTenantId;

  /** 租户状态：1 启用，2 禁用。 */
  private Integer status;

  /** 租户说明。 */
  private String description;
}
