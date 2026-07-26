package io.yak.framework.security.common.vo.dept;

import lombok.Data;

import java.util.List;
/**
 * 部门树节点视图对象。
 *
 * @author weifuwan
 */
@Data
public class DeptTreeVO {
  /** 主键标识。 */
  private Long id;
  /** 部门名称。 */
  private String deptName;
  /** 描述信息。 */
  private String description;
  /** 父节点标识。 */
  private Long parentId;
  /** 是否为叶子节点。 */
  private Boolean leaf;
  /** 子节点列表。 */
  private List<DeptTreeVO> childList;

  public DeptTreeVO() {}

  DeptTreeVO(Long id, String deptName, String description, Long parentId,
             Boolean leaf, List<DeptTreeVO> childList) {
    this.id = id;
    this.deptName = deptName;
    this.description = description;
    this.parentId = parentId;
    this.leaf = leaf;
    this.childList = childList;
  }

  public static DeptTreeVOBuilder builder() { return new DeptTreeVOBuilder(); }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof DeptTreeVO)) {
      return false;
    }
    DeptTreeVO other = (DeptTreeVO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$id = this.getId();
    Long other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    Long this$parentId = this.getParentId();
    Long other$parentId = other.getParentId();
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
    String this$deptName = this.getDeptName();
    String other$deptName = other.getDeptName();
    if (this$deptName == null ? other$deptName != null
                              : !this$deptName.equals(other$deptName)) {
      return false;
    }
    String this$description = this.getDescription();
    String other$description = other.getDescription();
    if (this$description == null
            ? other$description != null
            : !this$description.equals(other$description)) {
      return false;
    }
    List<DeptTreeVO> this$childList = this.getChildList();
    List<DeptTreeVO> other$childList = other.getChildList();
    return !(this$childList == null
                 ? other$childList != null
                 : !((Object)this$childList).equals(other$childList));
  }

  protected boolean canEqual(Object other) {
    return other instanceof DeptTreeVO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    Long $parentId = this.getParentId();
    result =
        result * 59 + ($parentId == null ? 43 : ((Object)$parentId).hashCode());
    Boolean $leaf = this.getLeaf();
    result = result * 59 + ($leaf == null ? 43 : ((Object)$leaf).hashCode());
    String $deptName = this.getDeptName();
    result = result * 59 + ($deptName == null ? 43 : $deptName.hashCode());
    String $description = this.getDescription();
    result =
        result * 59 + ($description == null ? 43 : $description.hashCode());
    List<DeptTreeVO> $childList = this.getChildList();
    result = result * 59 +
             ($childList == null ? 43 : ((Object)$childList).hashCode());
    return result;
  }

  public String toString() {
    return "DeptTreeVO(id=" + this.getId() +
        ", deptName=" + this.getDeptName() +
        ", description=" + this.getDescription() +
        ", parentId=" + this.getParentId() + ", leaf=" + this.getLeaf() +
        ", childList=" + this.getChildList() + ")";
  }

  /**
   * 部门树节点构建器。
   *
   * @author weifuwan
   */
  public static class DeptTreeVOBuilder {
    /** 主键标识。 */
    private Long id;
    /** 部门名称。 */
    private String deptName;
    /** 描述信息。 */
    private String description;
    /** 父节点标识。 */
    private Long parentId;
    /** 是否为叶子节点。 */
    private Boolean leaf;
    /** 子节点列表。 */
    private List<DeptTreeVO> childList;

    DeptTreeVOBuilder() {}

    public DeptTreeVOBuilder id(Long id) {
      this.id = id;
      return this;
    }

    public DeptTreeVOBuilder deptName(String deptName) {
      this.deptName = deptName;
      return this;
    }

    public DeptTreeVOBuilder description(String description) {
      this.description = description;
      return this;
    }

    public DeptTreeVOBuilder parentId(Long parentId) {
      this.parentId = parentId;
      return this;
    }

    public DeptTreeVOBuilder leaf(Boolean leaf) {
      this.leaf = leaf;
      return this;
    }

    public DeptTreeVOBuilder childList(List<DeptTreeVO> childList) {
      this.childList = childList;
      return this;
    }

    public DeptTreeVO build() {
      return new DeptTreeVO(this.id, this.deptName, this.description,
                            this.parentId, this.leaf, this.childList);
    }

    public String toString() {
      return "DeptTreeVO.DeptTreeVOBuilder(id=" + this.id +
          ", deptName=" + this.deptName +
          ", description=" + this.description +
          ", parentId=" + this.parentId + ", leaf=" + this.leaf +
          ", childList=" + this.childList + ")";
    }
  }
}
