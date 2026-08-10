package io.yak.framework.common;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 与具体持久化框架无关的分页数据。
 *
 * <p>该类型用于 Repository / Service 等内部业务边界，避免 MyBatis {@code IPage} 等基础设施类型向上泄漏。
 * HTTP 输出仍由 {@link PagingData} 负责，以保持现有接口 JSON 结构稳定。</p>
 *
 * @param records 当前页记录
 * @param total 数据总条数
 * @param pages 总页数
 * @param pageNo 当前页码
 * @param pageSize 每页数据条数
 * @param <T> 业务数据类型
 * @author weifuwan
 */
public record PageData<T>(
        List<T> records,
        long total,
        long pages,
        long pageNo,
        long pageSize) {

    public PageData {
        records = records == null ? List.of() : List.copyOf(records);
    }

    /**
     * 映射当前页记录，并保持分页元数据不变。
     *
     * @param mapper 记录转换函数
     * @param <R> 目标记录类型
     * @return 映射后的分页数据
     */
    public <R> PageData<R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        return new PageData<>(
                records.stream().map(mapper).toList(),
                total,
                pages,
                pageNo,
                pageSize);
    }

    /**
     * 创建空分页数据。
     *
     * @param pageNo 当前页码
     * @param pageSize 每页条数
     * @param <T> 业务数据类型
     * @return 空分页数据
     */
    public static <T> PageData<T> empty(long pageNo, long pageSize) {
        return new PageData<>(List.of(), 0L, 0L, pageNo, pageSize);
    }
}
