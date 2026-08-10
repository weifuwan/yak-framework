package io.yak.framework.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * HTTP 分页数据封装对象。
 *
 * <p>该类型保留现有 {@code bizData + pagination} JSON 契约。Repository / Service 内部分页应优先使用
 * {@link PageData}，在 HTTP 输出边界转换为本类型。</p>
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
     * 根据框架无关分页数据创建 HTTP 分页数据。
     *
     * @param pageData 业务分页数据
     */
    public PagingData(PageData<T> pageData) {
        if (pageData == null) {
            this.bizData = new ArrayList<>();
            this.pagination = Pagination.builder()
                    .total(0L)
                    .pages(0L)
                    .pageNo(1L)
                    .pageSize(0L)
                    .build();
            return;
        }
        this.bizData = new ArrayList<>(pageData.records());
        this.pagination = Pagination.builder()
                .total(pageData.total())
                .pages(pageData.pages())
                .pageNo(pageData.pageNo())
                .pageSize(pageData.pageSize())
                .build();
    }

    /**
     * 根据框架无关分页数据创建 HTTP 分页数据。
     *
     * @param pageData 业务分页数据
     * @param <T> 业务数据类型
     * @return HTTP 分页数据
     */
    public static <T> PagingData<T> from(PageData<T> pageData) {
        return new PagingData<>(pageData);
    }

    /**
     * 根据业务数据和 MyBatis-Plus 分页对象创建分页数据。
     *
     * <p>仅为第一阶段源码兼容保留。新代码不得在 Service / Repository 使用 MyBatis {@code IPage}，
     * 应先转换为 {@link PageData}。</p>
     *
     * @param bizData  业务数据列表
     * @param pageInfo MyBatis-Plus 分页对象
     * @deprecated 使用 {@link PageData} 和 {@link #from(PageData)}，后续阶段移除 MyBatis 兼容构造器
     */
    @Deprecated(since = "1.0.0", forRemoval = false)
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
     * <p>仅为第一阶段源码兼容保留。业务数据列表初始化为空列表。</p>
     *
     * @param pageInfo MyBatis-Plus 分页对象
     * @deprecated 使用 {@link PageData} 和 {@link #from(PageData)}，后续阶段移除 MyBatis 兼容构造器
     */
    @Deprecated(since = "1.0.0", forRemoval = false)
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
