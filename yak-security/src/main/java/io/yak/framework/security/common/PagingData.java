package io.yak.framework.security.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.ArrayList;
import java.util.List;
public class PagingData<T> {
  private List<T> bizData;
  private Pagination pagination;

  public PagingData() {}

  public PagingData(List<T> bizData, Pagination pagination) {
    this.pagination = pagination;
    this.bizData = bizData;
  }

  public PagingData(List<T> bizData, IPage<?> pageInfo) {
    this.pagination = Pagination.builder()
                          .total(pageInfo.getTotal())
                          .pages(pageInfo.getPages())
                          .pageNo(pageInfo.getCurrent())
                          .pageSize(pageInfo.getSize())
                          .build();
    this.bizData = bizData;
  }

  public PagingData(IPage<?> pageInfo) {
    this.pagination = Pagination.builder()
                          .total(pageInfo.getTotal())
                          .pages(pageInfo.getPages())
                          .pageNo(pageInfo.getCurrent())
                          .pageSize(pageInfo.getSize())
                          .build();
    this.bizData = new ArrayList<T>();
  }

  public List<T> getBizData() { return this.bizData; }

  public Pagination getPagination() { return this.pagination; }

  public void setBizData(List<T> bizData) { this.bizData = bizData; }

  public void setPagination(Pagination pagination) {
    this.pagination = pagination;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof PagingData)) {
      return false;
    }
    PagingData other = (PagingData)o;
    if (!other.canEqual(this)) {
      return false;
    }
    List<T> this$bizData = this.getBizData();
    List<T> other$bizData = other.getBizData();
    if (this$bizData == null ? other$bizData != null
                             : !((Object)this$bizData).equals(other$bizData)) {
      return false;
    }
    Pagination this$pagination = this.getPagination();
    Pagination other$pagination = other.getPagination();
    return !(this$pagination == null
                 ? other$pagination != null
                 : !((Object)this$pagination).equals(other$pagination));
  }

  protected boolean canEqual(Object other) {
    return other instanceof PagingData;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    List<T> $bizData = this.getBizData();
    result =
        result * 59 + ($bizData == null ? 43 : ((Object)$bizData).hashCode());
    Pagination $pagination = this.getPagination();
    result = result * 59 +
             ($pagination == null ? 43 : ((Object)$pagination).hashCode());
    return result;
  }

  public String toString() {
    return "PagingData(bizData=" + this.getBizData() +
        ", pagination=" + this.getPagination() + ")";
  }
  public static class Pagination {
    private long total;
    private long pages;
    private long pageNo;
    private long pageSize;

    Pagination(long total, long pages, long pageNo, long pageSize) {
      this.total = total;
      this.pages = pages;
      this.pageNo = pageNo;
      this.pageSize = pageSize;
    }

    public static PaginationBuilder builder() {
      return new PaginationBuilder();
    }

    public long getTotal() { return this.total; }

    public long getPages() { return this.pages; }

    public long getPageNo() { return this.pageNo; }

    public long getPageSize() { return this.pageSize; }

    public void setTotal(long total) { this.total = total; }

    public void setPages(long pages) { this.pages = pages; }

    public void setPageNo(long pageNo) { this.pageNo = pageNo; }

    public void setPageSize(long pageSize) { this.pageSize = pageSize; }

    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (!(o instanceof Pagination)) {
        return false;
      }
      Pagination other = (Pagination)o;
      if (!other.canEqual(this)) {
        return false;
      }
      if (this.getTotal() != other.getTotal()) {
        return false;
      }
      if (this.getPages() != other.getPages()) {
        return false;
      }
      if (this.getPageNo() != other.getPageNo()) {
        return false;
      }
      return this.getPageSize() == other.getPageSize();
    }

    protected boolean canEqual(Object other) {
      return other instanceof Pagination;
    }

    public int hashCode() {
      int PRIME = 59;
      int result = 1;
      long $total = this.getTotal();
      result = result * 59 + (int)($total >>> 32 ^ $total);
      long $pages = this.getPages();
      result = result * 59 + (int)($pages >>> 32 ^ $pages);
      long $pageNo = this.getPageNo();
      result = result * 59 + (int)($pageNo >>> 32 ^ $pageNo);
      long $pageSize = this.getPageSize();
      result = result * 59 + (int)($pageSize >>> 32 ^ $pageSize);
      return result;
    }

    public String toString() {
      return "PagingData.Pagination(total=" + this.getTotal() +
          ", pages=" + this.getPages() + ", pageNo=" + this.getPageNo() +
          ", pageSize=" + this.getPageSize() + ")";
    }

    public static class PaginationBuilder {
      private long total;
      private long pages;
      private long pageNo;
      private long pageSize;

      PaginationBuilder() {}

      public PaginationBuilder total(long total) {
        this.total = total;
        return this;
      }

      public PaginationBuilder pages(long pages) {
        this.pages = pages;
        return this;
      }

      public PaginationBuilder pageNo(long pageNo) {
        this.pageNo = pageNo;
        return this;
      }

      public PaginationBuilder pageSize(long pageSize) {
        this.pageSize = pageSize;
        return this;
      }

      public Pagination build() {
        return new Pagination(this.total, this.pages, this.pageNo,
                              this.pageSize);
      }

      public String toString() {
        return "PagingData.Pagination.PaginationBuilder(total=" + this.total +
            ", pages=" + this.pages + ", pageNo=" + this.pageNo +
            ", pageSize=" + this.pageSize + ")";
      }
    }
  }
}
