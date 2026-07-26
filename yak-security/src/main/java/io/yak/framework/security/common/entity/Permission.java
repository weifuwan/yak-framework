package io.yak.framework.security.common.entity;

import lombok.Data;

/**
 * 权限实体。
 *
 * @author weifuwan
 */
@Data
public class Permission {
  /** 实体标识。 */
  private Long id;
  /** 权限编码。 */
  private String permissionCode;
  /** 权限名称。 */
  private String permissionName;
  /** 父级标识。 */
  private Long parentId;
  /** 叶节点标记。 */
  private Boolean leaf;
  /** 层级。 */
  private Integer level;
  /** 描述。 */
  private String description;

}
