package io.yak.framework.security.common.enums.user;

public enum UserCheckType {
  USER_NAME(1, "\u7528\u6237\u540d"),
  USER_PHONE(2, "\u7528\u6237\u7535\u8bdd"),
  USER_MAIL(3, "\u7528\u6237\u90ae\u7bb1");

  private int code;
  private String desc;

  private UserCheckType(int code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public int getCode() { return this.code; }

  public String getDesc() { return this.desc; }
}
