package io.yak.framework.security.extend.impl;
import io.yak.framework.security.extend.OperationLogExtend;
public class NoOpOperationLogExtend implements OperationLogExtend {
  public void record(String operator, String operation, String target, String detail) { }
}
