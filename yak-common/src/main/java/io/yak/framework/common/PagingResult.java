package io.yak.framework.common;

import lombok.*;

/**
 * 分页接口统一返回结果。
 *
 * <p>第一阶段为兼容现有调用保留。新接口统一使用 {@code Result<PagingData<T>>}，避免与 {@link Result}
 * 重复维护 success/fail 等响应语义。该调整不会改变现有分页 JSON 结构。</p>
 *
 * @param <T> 分页记录的数据类型
 * @author weifuwan
 * @deprecated 新代码使用 {@code Result<PagingData<T>>}
 */
@Deprecated(since = "1.0.0", forRemoval = false)
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PagingResult<T> extends BaseResult {

    /**
     * 分页业务数据。
     */
    private PagingData<T> data;

    /**
     * 创建只包含状态码的分页返回结果。
     *
     * @param code 状态码
     */
    private PagingResult(Integer code) {
        this.code = code;
    }

    /**
     * 创建包含状态码和提示信息的分页返回结果。
     *
     * @param code    状态码
     * @param message 提示信息
     */
    private PagingResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 构建成功结果，并携带分页数据。
     *
     * @param data 分页数据
     * @param <T>  分页记录的数据类型
     * @return 成功结果
     */
    public static <T> PagingResult<T> success(PagingData<T> data) {
        PagingResult<T> result = success();
        result.setData(data);
        return result;
    }

    /**
     * 构建不携带分页数据的成功结果。
     *
     * @param <T> 分页记录的数据类型
     * @return 成功结果
     */
    public static <T> PagingResult<T> success() {
        return new PagingResult<>(
                CommonErrorCode.SUCCESS.getCode(),
                CommonErrorCode.SUCCESS.getMessage()
        );
    }

    /**
     * 根据结果码构建失败结果。
     *
     * @param errorCode 错误码定义
     * @param <T>       分页记录的数据类型
     * @return 失败结果
     */
    public static <T> PagingResult<T> fail(ErrorCode errorCode) {
        if (errorCode == null) {
            return fail();
        }

        return new PagingResult<>(
                errorCode.getCode(),
                errorCode.getMessage()
        );
    }

    /**
     * 根据状态码和提示信息构建失败结果。
     *
     * @param code    状态码
     * @param message 提示信息
     * @param <T>     分页记录的数据类型
     * @return 失败结果
     */
    public static <T> PagingResult<T> fail(
            Integer code,
            String message) {

        return new PagingResult<>(code, message);
    }

    /**
     * 根据提示信息构建通用失败结果。
     *
     * @param message 提示信息
     * @param <T>     分页记录的数据类型
     * @return 失败结果
     */
    public static <T> PagingResult<T> fail(String message) {
        return new PagingResult<>(
                CommonErrorCode.COMMON_FAIL.getCode(),
                message
        );
    }

    /**
     * 构建通用失败结果。
     *
     * @param <T> 分页记录的数据类型
     * @return 失败结果
     */
    public static <T> PagingResult<T> fail() {
        return fail(CommonErrorCode.COMMON_FAIL);
    }

    /**
     * 根据业务异常构建失败结果。
     *
     * <p>优先使用异常携带的结构化错误码；只有未提供错误码时才使用异常消息。</p>
     *
     * @param exception 业务异常
     * @param <T>       返回数据类型
     * @return 失败结果
     */
    public static <T> PagingResult<T> fail(BusinessException exception) {
        if (exception == null) {
            return fail();
        }
        if (exception.getErrorCode() != null) {
            return fail(exception.getErrorCode());
        }
        String message = exception.getMessage();
        return message == null || message.trim().isEmpty()
                ? fail()
                : fail(message);
    }

    /**
     * 根据普通返回结果复制状态码和提示信息。
     *
     * <p>该方法只复制状态信息，不复制原结果中的业务数据。</p>
     *
     * @param source 原始返回结果
     * @param <T>    分页记录的数据类型
     * @return 分页返回结果
     */
    public static <T> PagingResult<T> buildFrom(
            Result<?> source) {

        if (source == null) {
            return fail();
        }

        return new PagingResult<>(
                source.getCode(),
                source.getMessage()
        );
    }

    /**
     * 构建参数校验失败结果。
     *
     * @param message 参数校验说明
     * @param <T>     分页记录的数据类型
     * @return 参数校验失败结果
     */
    public static <T> PagingResult<T> buildParamIllegal(
            String message) {

        String detail = message == null ? "" : message.trim();

        return new PagingResult<>(
                CommonErrorCode.PARAM_NOT_VALID.getCode(),
                CommonErrorCode.PARAM_NOT_VALID.getMessage()
                        + (detail.isEmpty() ? "" : "：" + detail)
                        + "，请检查后再提交！"
        );
    }

    /**
     * 判断请求是否成功。
     *
     * @return 成功返回 {@code true}
     */
    public boolean succeeded() {
        return CommonErrorCode.SUCCESS.getCode().equals(getCode());
    }

    /**
     * 判断资源是否重复。
     *
     * @return 资源重复返回 {@code true}
     */
    public boolean duplicate() {
        return CommonErrorCode.RESOURCE_DUPLICATION.getCode().equals(getCode());
    }

    /**
     * 判断请求是否失败。
     *
     * @return 失败返回 {@code true}
     */
    public boolean failed() {
        return !succeeded();
    }
}
