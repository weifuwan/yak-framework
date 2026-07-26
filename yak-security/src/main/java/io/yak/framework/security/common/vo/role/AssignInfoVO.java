package io.yak.framework.security.common.vo.role;
public class AssignInfoVO {
  private Integer id;
  private String name;
  private Boolean has;

  public Integer getId() { return this.id; }

  public String getName() { return this.name; }

  public Boolean getHas() { return this.has; }

  public void setId(Integer id) { this.id = id; }

  public void setName(String name) { this.name = name; }

  public void setHas(Boolean has) { this.has = has; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof AssignInfoVO)) {
      return false;
    }
    AssignInfoVO other = (AssignInfoVO)o;
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
    String this$name = this.getName();
    String other$name = other.getName();
    return !(this$name == null ? other$name != null
                               : !this$name.equals(other$name));
  }

  protected boolean canEqual(Object other) {
    return other instanceof AssignInfoVO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Integer $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    Boolean $has = this.getHas();
    result = result * 59 + ($has == null ? 43 : ((Object)$has).hashCode());
    String $name = this.getName();
    result = result * 59 + ($name == null ? 43 : $name.hashCode());
    return result;
  }

  public String toString() {
    return "AssignInfoVO(id=" + this.getId() + ", name=" + this.getName() +
        ", has=" + this.getHas() + ")";
  }
}
