package io.yak.framework.security.common.enums;

public enum ConfigStatusEnum {
  NORMAL(1, "\u6b63\u5e38"),
  DISABLE(2, "\u7981\u7528");

  private int code;
  private String desc;

  private ConfigStatusEnum(int code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public int getCode() { return this.code; }

  public String getDesc() { return this.desc; }

  public static ConfigStatusEnum valueOf(Integer code) {
    if (code == null) {
      return null;
    }
    for (ConfigStatusEnum state : ConfigStatusEnum.values()) {
      if (state.getCode() != code.intValue())
        continue;
      return state;
    }
    return null;
  }
}
