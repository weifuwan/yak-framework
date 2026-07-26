package io.yak.framework.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 分页数据封装对象。
 *
 * <p>用于封装分页查询返回的业务数据列表和分页信息。</p>
 *
 * @param <T> 业务数据类型
 * @author weifuwan
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PagingData<T> {

  /**
   * 当前分页的业务数据列表。
   */
  private List<T> bizData;

  /**
   * 分页信息。
   */
  private Pagination pagination;

  /**
   * 根据业务数据和 MyBatis-Plus 分页对象创建分页数据。
   *
   * @param bizData 业务数据列表
   * @param pageInfo MyBatis-Plus 分页对象
   */
  public PagingData(List<T> bizData, IPage<?> pageInfo) {
    this.pagination = Pagination.builder()
            .total(pageInfo.getTotal())
            .pages(pageInfo.getPages())
            .pageNo(pageInfo.getCurrent())
            .pageSize(pageInfo.getSize())
            .build();
    this.bizData = bizData;
  }

  /**
   * 根据 MyBatis-Plus 分页对象创建分页数据。
   *
   * <p>业务数据列表初始化为空列表。</p>
   *
   * @param pageInfo MyBatis-Plus 分页对象
   */
  public PagingData(IPage<?> pageInfo) {
    this.pagination = Pagination.builder()
            .total(pageInfo.getTotal())
            .pages(pageInfo.getPages())
            .pageNo(pageInfo.getCurrent())
            .pageSize(pageInfo.getSize())
            .build();
    this.bizData = new ArrayList<>();
  }

  /**
   * 分页信息。
   *
   * <p>记录总数据量、总页数、当前页码和每页数据量。</p>
   *
   * @author weifuwan
   */
  @Getter
  @Setter
  @Builder
  @ToString
  @EqualsAndHashCode
  @AllArgsConstructor(access = AccessLevel.PACKAGE)
  public static class Pagination {

    /**
     * 数据总条数。
     */
    private long total;

    /**
     * 总页数。
     */
    private long pages;

    /**
     * 当前页码。
     */
    private long pageNo;

    /**
     * 每页数据条数。
     */
    private long pageSize;
  }
}