package io.yak.framework.security.common.entity;

public class ResourceType {
  private Long id;
  private String typeName;

  public Long getId() { return this.id; }

  public String getTypeName() { return this.typeName; }

  public void setId(Long id) { this.id = id; }

  public void setTypeName(String typeName) { this.typeName = typeName; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof ResourceType)) {
      return false;
    }
    ResourceType other = (ResourceType)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$id = this.getId();
    Long other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    String this$typeName = this.getTypeName();
    String other$typeName = other.getTypeName();
    return !(this$typeName == null ? other$typeName != null
                                   : !this$typeName.equals(other$typeName));
  }

  protected boolean canEqual(Object other) {
    return other instanceof ResourceType;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    String $typeName = this.getTypeName();
    result = result * 59 + ($typeName == null ? 43 : $typeName.hashCode());
    return result;
  }

  public String toString() {
    return "ResourceType(id=" + this.getId() +
        ", typeName=" + this.getTypeName() + ")";
  }
}
