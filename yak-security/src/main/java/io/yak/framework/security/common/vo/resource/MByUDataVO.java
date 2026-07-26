package io.yak.framework.security.common.vo.resource;
public class MByUDataVO {
  private Long id;
  private String name;
  private Integer hasLevel;

  public MByUDataVO() {}

  public MByUDataVO(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() { return this.id; }

  public String getName() { return this.name; }

  public Integer getHasLevel() { return this.hasLevel; }

  public void setId(Long id) { this.id = id; }

  public void setName(String name) { this.name = name; }

  public void setHasLevel(Integer hasLevel) { this.hasLevel = hasLevel; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof MByUDataVO)) {
      return false;
    }
    MByUDataVO other = (MByUDataVO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$id = this.getId();
    Long other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    Integer this$hasLevel = this.getHasLevel();
    Integer other$hasLevel = other.getHasLevel();
    if (this$hasLevel == null
            ? other$hasLevel != null
            : !((Object)this$hasLevel).equals(other$hasLevel)) {
      return false;
    }
    String this$name = this.getName();
    String other$name = other.getName();
    return !(this$name == null ? other$name != null
                               : !this$name.equals(other$name));
  }

  protected boolean canEqual(Object other) {
    return other instanceof MByUDataVO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    Integer $hasLevel = this.getHasLevel();
    result =
        result * 59 + ($hasLevel == null ? 43 : ((Object)$hasLevel).hashCode());
    String $name = this.getName();
    result = result * 59 + ($name == null ? 43 : $name.hashCode());
    return result;
  }

  public String toString() {
    return "MByUDataVO(id=" + this.getId() + ", name=" + this.getName() +
        ", hasLevel=" + this.getHasLevel() + ")";
  }
}
