package io.yak.framework.security.common.vo.tenant;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

/** 租户视图对象。 */
@Data
@Builder
public class TenantVO {

  private Long id;
  private String tenantCode;
  private String tenantName;
  private String externalSystem;
  private String externalTenantId;
  private Integer status;
  private String description;
  private Date createTime;
  private Date updateTime;
}
