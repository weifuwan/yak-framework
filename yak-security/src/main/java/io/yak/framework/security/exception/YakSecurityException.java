package io.yak.framework.security.exception;

import io.yak.framework.security.exception.CodeMsg;

public class YakSecurityException extends RuntimeException {
  public YakSecurityException() {}

  public YakSecurityException(CodeMsg codeMsg) {
    super(codeMsg.getCode() + "-" + codeMsg.getMessage());
  }

  public YakSecurityException(String message, Throwable cause) {
    super(message, cause);
  }
}
