package io.yak.framework.security.common;

import io.yak.framework.security.common.enums.ResultCode;
import io.yak.framework.security.exception.YakSecurityException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 分页接口统一返回结果。
 *
 * <p>封装接口执行状态、提示信息和分页业务数据。</p>
 *
 * @param <T> 分页记录的数据类型
 * @author weifuwan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PagingResult<T> extends BaseResult {

  /**
   * 异常消息中错误码与错误信息的分隔符。
   */
  private static final String ERROR_MESSAGE_SEPARATOR = "-";

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
   * 判断请求是否成功。
   *
   * @return 成功返回 {@code true}
   */
  public boolean succeeded() {
    return ResultCode.SUCCESS.getCode().equals(getCode());
  }

  /**
   * 判断资源是否重复。
   *
   * @return 资源重复返回 {@code true}
   */
  public boolean duplicate() {
    return ResultCode.RESOURCE_DUPLICATION.getCode().equals(getCode());
  }

  /**
   * 判断请求是否失败。
   *
   * @return 失败返回 {@code true}
   */
  public boolean failed() {
    return !succeeded();
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
            ResultCode.SUCCESS.getCode(),
            ResultCode.SUCCESS.getMessage()
    );
  }

  /**
   * 根据结果码构建失败结果。
   *
   * @param resultCode 结果码枚举
   * @param <T>        分页记录的数据类型
   * @return 失败结果
   */
  public static <T> PagingResult<T> fail(ResultCode resultCode) {
    if (resultCode == null) {
      return fail();
    }

    return new PagingResult<>(
            resultCode.getCode(),
            resultCode.getMessage()
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
            ResultCode.COMMON_FAIL.getCode(),
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
    return fail(ResultCode.COMMON_FAIL);
  }

  /**
   * 根据业务异常构建失败结果。
   *
   * <p>兼容以下异常消息格式：</p>
   *
   * <pre>
   * 状态码-错误信息
   * 例如：10001-用户不存在
   * </pre>
   *
   * <p>当异常为空、异常消息为空或消息格式不正确时，
   * 自动返回通用失败结果，避免出现空指针、数组越界
   * 或数字格式转换异常。</p>
   *
   * @param exception 业务异常
   * @param <T>       分页记录的数据类型
   * @return 失败结果
   */
  public static <T> PagingResult<T> fail(
          YakSecurityException exception) {

    if (exception == null) {
      return fail();
    }

    String exceptionMessage = exception.getMessage();
    if (exceptionMessage == null
            || exceptionMessage.trim().isEmpty()) {
      return fail();
    }

    int separatorIndex =
            exceptionMessage.indexOf(ERROR_MESSAGE_SEPARATOR);

    if (separatorIndex <= 0
            || separatorIndex == exceptionMessage.length() - 1) {
      return fail(exceptionMessage);
    }

    String codeText =
            exceptionMessage.substring(0, separatorIndex).trim();

    String message =
            exceptionMessage.substring(separatorIndex + 1).trim();

    try {
      return fail(Integer.valueOf(codeText), message);
    } catch (NumberFormatException ignored) {
      // 异常消息不符合“状态码-错误信息”格式时，
      // 将完整异常消息作为通用失败信息返回。
      return fail(exceptionMessage);
    }
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
            ResultCode.PARAM_NOT_VALID.getCode(),
            ResultCode.PARAM_NOT_VALID.getMessage()
                    + (detail.isEmpty() ? "" : "：" + detail)
                    + "，请检查后再提交！"
    );
  }
}