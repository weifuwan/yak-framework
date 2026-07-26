package io.yak.framework.security.common.enums.oplog;

public enum OplogCode {
  OPERATE_PAGE(1, "\u64cd\u4f5c\u9875\u9762"),
  OPERATE_TYPE(2, "\u64cd\u4f5c\u7c7b\u578b"),
  TARGET_TYPE(3, "\u5bf9\u8c61\u5206\u7c7b");

  private final Integer type;
  private final String info;

  private OplogCode(Integer type, String info) {
    this.type = type;
    this.info = info;
  }

  public Integer getType() { return this.type; }

  public String getInfo() { return this.info; }
}
