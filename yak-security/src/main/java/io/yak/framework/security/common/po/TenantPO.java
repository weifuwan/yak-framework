package io.yak.framework.security.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 租户持久化对象。
 *
 * <p>租户是应用内部的业务隔离域，和 {@code app_name} 表示的宿主应用隔离不是同一个概念。
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("yak_security_tenant")
public class TenantPO extends BasePO {

  /** 稳定租户编码。 */
  private String tenantCode;

  /** 租户名称。 */
  private String tenantName;

  /** 外部目录系统标识，例如 LDAP、OIDC、企业 IAM。 */
  private String externalSystem;

  /** 外部目录中的租户标识。 */
  private String externalTenantId;

  /** 租户状态：1 启用，2 禁用。 */
  private Integer status = 1;

  /** 租户说明。 */
  private String description;
}
