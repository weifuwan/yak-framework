package io.yak.framework.security.common.vo.permission;

import lombok.Data;

import java.util.List;
/**
 * 权限树节点视图对象。
 *
 * @author weifuwan
 */
@Data
public class PermissionTreeVO {
  /** 主键标识。 */
  private Long id;
  /** 是否已分配。 */
  private Boolean has;
  /** 权限名称。 */
  private String permissionName;
  /** 父节点标识。 */
  private Long parentId;
  /** 是否为叶子节点。 */
  private Boolean leaf;
  /** 子节点列表。 */
  private List<PermissionTreeVO> childList;

  public PermissionTreeVO() {}

  PermissionTreeVO(Long id, Boolean has, String permissionName,
                   Long parentId, Boolean leaf,
                   List<PermissionTreeVO> childList) {
    this.id = id;
    this.has = has;
    this.permissionName = permissionName;
    this.parentId = parentId;
    this.leaf = leaf;
    this.childList = childList;
  }

  public static PermissionTreeVOBuilder builder() {
    return new PermissionTreeVOBuilder();
  }

  /**
   * 权限树节点构建器。
   *
   * @author weifuwan
   */
  public static class PermissionTreeVOBuilder {
    /** 主键标识。 */
    private Long id;
    /** 是否已分配。 */
    private Boolean has;
    /** 权限名称。 */
    private String permissionName;
    /** 父节点标识。 */
    private Long parentId;
    /** 是否为叶子节点。 */
    private Boolean leaf;
    /** 子节点列表。 */
    private List<PermissionTreeVO> childList;

    PermissionTreeVOBuilder() {}

    public PermissionTreeVOBuilder id(Long id) {
      this.id = id;
      return this;
    }

    public PermissionTreeVOBuilder has(Boolean has) {
      this.has = has;
      return this;
    }

    public PermissionTreeVOBuilder permissionName(String permissionName) {
      this.permissionName = permissionName;
      return this;
    }

    public PermissionTreeVOBuilder parentId(Long parentId) {
      this.parentId = parentId;
      return this;
    }

    public PermissionTreeVOBuilder leaf(Boolean leaf) {
      this.leaf = leaf;
      return this;
    }

    public PermissionTreeVOBuilder childList(List<PermissionTreeVO> childList) {
      this.childList = childList;
      return this;
    }

    public PermissionTreeVO build() {
      return new PermissionTreeVO(this.id, this.has, this.permissionName,
                                  this.parentId, this.leaf, this.childList);
    }

    public String toString() {
      return "PermissionTreeVO.PermissionTreeVOBuilder(id=" + this.id +
          ", has=" + this.has + ", permissionName=" + this.permissionName +
          ", parentId=" + this.parentId + ", leaf=" + this.leaf +
          ", childList=" + this.childList + ")";
    }
  }
}
