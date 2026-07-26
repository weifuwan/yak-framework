package io.yak.framework.security.common.vo.permission;

import java.util.List;
public class PermissionTreeVO {
  private Integer id;
  private Boolean has;
  private String permissionName;
  private Integer parentId;
  private Boolean leaf;
  private List<PermissionTreeVO> childList;

  public PermissionTreeVO() {}

  PermissionTreeVO(Integer id, Boolean has, String permissionName,
                   Integer parentId, Boolean leaf,
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

  public Integer getId() { return this.id; }

  public Boolean getHas() { return this.has; }

  public String getPermissionName() { return this.permissionName; }

  public Integer getParentId() { return this.parentId; }

  public Boolean getLeaf() { return this.leaf; }

  public List<PermissionTreeVO> getChildList() { return this.childList; }

  public void setId(Integer id) { this.id = id; }

  public void setHas(Boolean has) { this.has = has; }

  public void setPermissionName(String permissionName) {
    this.permissionName = permissionName;
  }

  public void setParentId(Integer parentId) { this.parentId = parentId; }

  public void setLeaf(Boolean leaf) { this.leaf = leaf; }

  public void setChildList(List<PermissionTreeVO> childList) {
    this.childList = childList;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof PermissionTreeVO)) {
      return false;
    }
    PermissionTreeVO other = (PermissionTreeVO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Integer this$id = this.getId();
    Integer other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    Boolean this$has = this.getHas();
    Boolean other$has = other.getHas();
    if (this$has == null ? other$has != null
                         : !((Object)this$has).equals(other$has)) {
      return false;
    }
    Integer this$parentId = this.getParentId();
    Integer other$parentId = other.getParentId();
    if (this$parentId == null
            ? other$parentId != null
            : !((Object)this$parentId).equals(other$parentId)) {
      return false;
    }
    Boolean this$leaf = this.getLeaf();
    Boolean other$leaf = other.getLeaf();
    if (this$leaf == null ? other$leaf != null
                          : !((Object)this$leaf).equals(other$leaf)) {
      return false;
    }
    String this$permissionName = this.getPermissionName();
    String other$permissionName = other.getPermissionName();
    if (this$permissionName == null
            ? other$permissionName != null
            : !this$permissionName.equals(other$permissionName)) {
      return false;
    }
    List<PermissionTreeVO> this$childList = this.getChildList();
    List<PermissionTreeVO> other$childList = other.getChildList();
    return !(this$childList == null
                 ? other$childList != null
                 : !((Object)this$childList).equals(other$childList));
  }

  protected boolean canEqual(Object other) {
    return other instanceof PermissionTreeVO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Integer $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    Boolean $has = this.getHas();
    result = result * 59 + ($has == null ? 43 : ((Object)$has).hashCode());
    Integer $parentId = this.getParentId();
    result =
        result * 59 + ($parentId == null ? 43 : ((Object)$parentId).hashCode());
    Boolean $leaf = this.getLeaf();
    result = result * 59 + ($leaf == null ? 43 : ((Object)$leaf).hashCode());
    String $permissionName = this.getPermissionName();
    result = result * 59 +
             ($permissionName == null ? 43 : $permissionName.hashCode());
    List<PermissionTreeVO> $childList = this.getChildList();
    result = result * 59 +
             ($childList == null ? 43 : ((Object)$childList).hashCode());
    return result;
  }

  public String toString() {
    return "PermissionTreeVO(id=" + this.getId() + ", has=" + this.getHas() +
        ", permissionName=" + this.getPermissionName() +
        ", parentId=" + this.getParentId() + ", leaf=" + this.getLeaf() +
        ", childList=" + this.getChildList() + ")";
  }

  public static class PermissionTreeVOBuilder {
    private Integer id;
    private Boolean has;
    private String permissionName;
    private Integer parentId;
    private Boolean leaf;
    private List<PermissionTreeVO> childList;

    PermissionTreeVOBuilder() {}

    public PermissionTreeVOBuilder id(Integer id) {
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

    public PermissionTreeVOBuilder parentId(Integer parentId) {
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
