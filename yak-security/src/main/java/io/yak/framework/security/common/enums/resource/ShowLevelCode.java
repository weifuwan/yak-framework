package io.yak.framework.security.common.enums.resource;

public enum ShowLevelCode {
  PROJECT(1, "\u9879\u76ee\u7ea7\u522b"),
  RESOURCE_TYPE(2, "\u8d44\u6e90\u7c7b\u522b\u7ea7\u522b"),
  RESOURCE(3, "\u8d44\u6e90\u7ea7\u522b");

  private final Integer type;
  private final String info;

  private ShowLevelCode(Integer type, String info) {
    this.type = type;
    this.info = info;
  }

  public static ShowLevelCode getByType(Integer type) {
    ShowLevelCode[] showLevelCodes;
    for (ShowLevelCode showLevelCode :
         showLevelCodes = ShowLevelCode.values()) {
      if (!showLevelCode.type.equals(type))
        continue;
      return showLevelCode;
    }
    return null;
  }

  public Integer getType() { return this.type; }

  public String getInfo() { return this.info; }
}
