package io.yak.framework.security.exception;

/**
 * 错误码与错误消息定义接口。
 */
public interface CodeMsg {
  Integer getCode();

  String getMessage();
}
