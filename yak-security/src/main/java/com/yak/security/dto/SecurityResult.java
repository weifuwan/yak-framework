package com.yak.security.dto;
public class SecurityResult<T> {
  private final int code;
  private final String message;
  private final T data;
  private SecurityResult(int c, String m, T d) {
    code = c;
    message = m;
    data = d;
  }
  public static <T> SecurityResult<T> success(T d) {
    return new SecurityResult<T>(0, "success", d);
  }
  public static <T> SecurityResult<T> failure(int c, String m) {
    return new SecurityResult<T>(c, m, null);
  }
  public int getCode() { return code; }
  public String getMessage() { return message; }
  public T getData() { return data; }
}
