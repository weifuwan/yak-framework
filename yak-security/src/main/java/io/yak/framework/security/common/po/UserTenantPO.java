package io.yak.framework.security.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户租户成员关系持久化对象。
 *
 * <p>用户身份在应用内保持唯一，一个用户可以加入多个租户。
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("yak_security_user_tenant")
public class UserTenantPO extends BasePO {

  /** 用户标识。 */
  private Long userId;

  /** 租户标识。 */
  private Long tenantId;

  /** 成员类型：0 普通成员，1 租户管理员。 */
  private Integer memberType = 0;

  /** 是否为用户的默认租户。 */
  private Boolean defaultTenant = Boolean.FALSE;

  /** 成员状态：1 启用，2 禁用。 */
  private Integer status = 1;

  /** 外部目录中的成员关系标识。 */
  private String externalMembershipId;
}
