package io.yak.framework.security.exception;

/**
 * 错误码与错误消息定义接口。
 *
 * <p>业务异常、参数校验异常等错误类型可以实现该接口，
 * 统一提供错误码和错误描述，便于接口响应及日志记录。</p>
 */
public interface CodeMsg {

  /**
   * 获取错误码。
   *
   * @return 错误码
   */
  Integer getCode();

  /**
   * 获取错误消息。
   *
   * @return 错误消息
   */
  String getMessage();
}