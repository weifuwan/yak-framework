package io.yak.framework.security.common.enums.resource;

public enum HasLevelCode {
  NONE(0, "\u4e0d\u62e5\u6709"),
  HALF(1, "\u534a\u62e5\u6709"),
  ALL(2, "\u5168\u62e5\u6709");

  private final Integer type;
  private final String info;

  private HasLevelCode(Integer type, String info) {
    this.type = type;
    this.info = info;
  }

  public static HasLevelCode getByType(Integer type) {
    HasLevelCode[] hasLevelCodes;
    for (HasLevelCode hasLevelCode : hasLevelCodes = HasLevelCode.values()) {
      if (!hasLevelCode.type.equals(type))
        continue;
      return hasLevelCode;
    }
    return null;
  }

  public Integer getType() { return this.type; }

  public String getInfo() { return this.info; }
}
