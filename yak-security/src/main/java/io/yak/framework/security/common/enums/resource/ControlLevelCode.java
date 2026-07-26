package io.yak.framework.security.common.enums.resource;

public enum ControlLevelCode {
  NONE(0, "\u65e0\u6743\u9650"),
  VIEW(1, "\u67e5\u770b\u6743\u9650"),
  ADMIN(2, "\u7ba1\u7406\u6743\u9650");

  private final Integer type;
  private final String info;

  private ControlLevelCode(Integer type, String info) {
    this.type = type;
    this.info = info;
  }

  public static ControlLevelCode getByType(Integer type) {
    ControlLevelCode[] controlLevelCodes;
    for (ControlLevelCode controlLevelCode :
         controlLevelCodes = ControlLevelCode.values()) {
      if (!controlLevelCode.type.equals(type))
        continue;
      return controlLevelCode;
    }
    return null;
  }

  public Integer getType() { return this.type; }

  public String getInfo() { return this.info; }
}
