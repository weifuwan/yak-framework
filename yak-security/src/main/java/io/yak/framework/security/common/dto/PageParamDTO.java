package io.yak.framework.security.common.dto;
public class PageParamDTO {
  private int page = 1;
  private int size = 10;

  public int getPage() { return this.page; }

  public int getSize() { return this.size; }

  public void setPage(int page) { this.page = page; }

  public void setSize(int size) { this.size = size; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof PageParamDTO)) {
      return false;
    }
    PageParamDTO other = (PageParamDTO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    if (this.getPage() != other.getPage()) {
      return false;
    }
    return this.getSize() == other.getSize();
  }

  protected boolean canEqual(Object other) {
    return other instanceof PageParamDTO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    result = result * 59 + this.getPage();
    result = result * 59 + this.getSize();
    return result;
  }

  public String toString() {
    return "PageParamDTO(page=" + this.getPage() + ", size=" + this.getSize() +
        ")";
  }
}
