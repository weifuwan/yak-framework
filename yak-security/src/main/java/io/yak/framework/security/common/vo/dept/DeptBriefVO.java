package io.yak.framework.security.common.vo.dept;
public class DeptBriefVO {
  private Integer id;
  private String deptName;
  private Integer parentId;

  public Integer getId() { return this.id; }

  public String getDeptName() { return this.deptName; }

  public Integer getParentId() { return this.parentId; }

  public void setId(Integer id) { this.id = id; }

  public void setDeptName(String deptName) { this.deptName = deptName; }

  public void setParentId(Integer parentId) { this.parentId = parentId; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof DeptBriefVO)) {
      return false;
    }
    DeptBriefVO other = (DeptBriefVO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Integer this$id = this.getId();
    Integer other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    Integer this$parentId = this.getParentId();
    Integer other$parentId = other.getParentId();
    if (this$parentId == null
            ? other$parentId != null
            : !((Object)this$parentId).equals(other$parentId)) {
      return false;
    }
    String this$deptName = this.getDeptName();
    String other$deptName = other.getDeptName();
    return !(this$deptName == null ? other$deptName != null
                                   : !this$deptName.equals(other$deptName));
  }

  protected boolean canEqual(Object other) {
    return other instanceof DeptBriefVO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Integer $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    Integer $parentId = this.getParentId();
    result =
        result * 59 + ($parentId == null ? 43 : ((Object)$parentId).hashCode());
    String $deptName = this.getDeptName();
    result = result * 59 + ($deptName == null ? 43 : $deptName.hashCode());
    return result;
  }

  public String toString() {
    return "DeptBriefVO(id=" + this.getId() +
        ", deptName=" + this.getDeptName() +
        ", parentId=" + this.getParentId() + ")";
  }
}
