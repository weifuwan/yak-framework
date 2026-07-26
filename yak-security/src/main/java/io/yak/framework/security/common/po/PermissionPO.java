package io.yak.framework.security.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 权限持久化对象。
 *
 * @author weifuwan
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@TableName(value = "yak_security_permission")
public class PermissionPO extends BasePO {
  /** 权限编码。 */
  private String permissionCode;

  /** 权限名称。 */
  private String permissionName;

  /** 上级权限标识。 */
  private Long parentId;

  /** 是否为叶子权限。 */
  private Boolean leaf;

  /** 权限层级。 */
  private Integer level;

  /** 权限描述。 */
  private String description;
}
