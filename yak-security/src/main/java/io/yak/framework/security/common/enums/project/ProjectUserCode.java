package io.yak.framework.security.common.enums.project;

public enum ProjectUserCode {
  NORMAL(0, "\u666e\u901a\u7528\u6237"),
  OWNER(1, "\u9879\u76ee\u8d1f\u8d23\u4eba");

  private final Integer type;
  private final String info;

  public Integer getType() { return this.type; }

  public String getInfo() { return this.info; }

  private ProjectUserCode(Integer type, String info) {
    this.type = type;
    this.info = info;
  }
}
