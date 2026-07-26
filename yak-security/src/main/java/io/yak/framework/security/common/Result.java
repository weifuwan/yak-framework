package io.yak.framework.security.common;

import io.yak.framework.security.common.enums.ResultCode;
import io.yak.framework.security.exception.YakSecurityException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 统一接口返回结果。
 *
 * <p>封装接口执行状态、提示信息和业务数据。</p>
 *
 * @param <T> 返回数据类型
 * @author weifuwan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Result<T> extends BaseResult {

  /**
   * 异常消息中错误码与错误信息的分隔符。
   */
  private static final String ERROR_MESSAGE_SEPARATOR = "-";

  /**
   * 返回的业务数据。
   */
  protected T data;

  /**
   * 创建只包含状态码的返回结果。
   *
   * @param code 状态码
   */
  private Result(Integer code) {
    this.code = code;
  }

  /**
   * 创建包含状态码和提示信息的返回结果。
   *
   * @param code    状态码
   * @param message 提示信息
   */
  private Result(Integer code, String message) {
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
   * 判断请求是否成功。
   *
   * @return 成功返回 {@code true}
   * @deprecated 方法名存在拼写错误，请使用 {@link #succeeded()}
   */
  @Deprecated
  public boolean successed() {
    return succeeded();
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
   * 根据布尔值构建返回结果。
   *
   * @param success 是否成功
   * @param <T>     返回数据类型
   * @return 返回结果
   */
  public static <T> Result<T> build(boolean success) {
    return success ? Result.success() : Result.fail();
  }

  /**
   * 构建成功结果，并携带业务数据。
   *
   * @param data 业务数据
   * @param <T>  返回数据类型
   * @return 成功结果
   */
  public static <T> Result<T> success(T data) {
    Result<T> result = success();
    result.setData(data);
    return result;
  }

  /**
   * 构建不携带业务数据的成功结果。
   *
   * @param <T> 返回数据类型
   * @return 成功结果
   */
  public static <T> Result<T> success() {
    return new Result<>(
            ResultCode.SUCCESS.getCode(),
            ResultCode.SUCCESS.getMessage()
    );
  }

  /**
   * 根据结果码构建失败结果。
   *
   * @param resultCode 结果码枚举
   * @param <T>        返回数据类型
   * @return 失败结果
   */
  public static <T> Result<T> fail(ResultCode resultCode) {
    if (resultCode == null) {
      return fail();
    }

    return new Result<>(
            resultCode.getCode(),
            resultCode.getMessage()
    );
  }

  /**
   * 根据状态码和提示信息构建失败结果。
   *
   * @param code    状态码
   * @param message 提示信息
   * @param <T>     返回数据类型
   * @return 失败结果
   */
  public static <T> Result<T> fail(Integer code, String message) {
    return new Result<>(code, message);
  }

  /**
   * 根据提示信息构建通用失败结果。
   *
   * @param message 提示信息
   * @param <T>     返回数据类型
   * @return 失败结果
   */
  public static <T> Result<T> fail(String message) {
    return new Result<>(
            ResultCode.COMMON_FAIL.getCode(),
            message
    );
  }

  /**
   * 构建通用失败结果。
   *
   * @param <T> 返回数据类型
   * @return 失败结果
   */
  public static <T> Result<T> fail() {
    return fail(ResultCode.COMMON_FAIL);
  }

  /**
   * 根据业务异常构建失败结果。
   *
   * <p>兼容异常消息格式：</p>
   *
   * <pre>
   * 状态码-错误信息
   * 例如：10001-用户不存在
   * </pre>
   *
   * <p>当异常消息为空或格式不正确时，自动返回通用失败结果，
   * 避免出现空指针、数组越界或数字格式异常。</p>
   *
   * @param exception 业务异常
   * @param <T>       返回数据类型
   * @return 失败结果
   */
  public static <T> Result<T> fail(YakSecurityException exception) {
    if (exception == null) {
      return fail();
    }

    String exceptionMessage = exception.getMessage();
    if (exceptionMessage == null || exceptionMessage.trim().isEmpty()) {
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
   * 构建成功结果，并携带业务数据。
   *
   * @param data 业务数据
   * @param <T>  返回数据类型
   * @return 成功结果
   * @deprecated 请使用 {@link #success(Object)}
   */
  @Deprecated
  public static <T> Result<T> buildSucc(T data) {
    return success(data);
  }

  /**
   * 根据已有结果复制状态码和提示信息。
   *
   * <p>该方法只复制状态信息，不复制原结果中的业务数据。</p>
   *
   * @param source 原始返回结果
   * @param <T>    新返回结果的数据类型
   * @return 新返回结果
   */
  public static <T> Result<T> buildFrom(Result<?> source) {
    if (source == null) {
      return fail();
    }

    return new Result<>(
            source.getCode(),
            source.getMessage()
    );
  }

  /**
   * 构建参数校验失败结果。
   *
   * @param message 参数校验说明
   * @param <T>     返回数据类型
   * @return 参数校验失败结果
   */
  public static <T> Result<T> buildParamIllegal(String message) {
    String detail = message == null ? "" : message.trim();

    return new Result<>(
            ResultCode.PARAM_NOT_VALID.getCode(),
            ResultCode.PARAM_NOT_VALID.getMessage()
                    + (detail.isEmpty() ? "" : "：" + detail)
                    + "，请检查后再提交！"
    );
  }

  /**
   * 构建资源不存在结果。
   *
   * @param message 提示信息
   * @param <T>     返回数据类型
   * @return 资源不存在结果
   */
  public static <T> Result<T> buildNotExist(String message) {
    return new Result<>(
            ResultCode.RESOURCE_TYPE_NOT_EXISTS.getCode(),
            message
    );
  }

  /**
   * 构建资源重复结果。
   *
   * @param message 提示信息
   * @param <T>     返回数据类型
   * @return 资源重复结果
   */
  public static <T> Result<T> buildDuplicate(String message) {
    return new Result<>(
            ResultCode.RESOURCE_DUPLICATION.getCode(),
            message
    );
  }
}