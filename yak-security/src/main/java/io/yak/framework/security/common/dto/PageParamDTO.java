package io.yak.framework.security.common.dto;

import lombok.Data;
/**
 * 分页参数数据传输对象。
 *
 * @author weifuwan
 */
@Data
public class PageParamDTO {
  /** 当前页码。 */
  private int page = 1;
  /** 每页记录数。 */
  private int size = 10;

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
