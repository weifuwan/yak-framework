package io.yak.framework.security.extend;

/** Host hook invoked for security operation audit events. */
public interface OperationLogExtend {
  void record(String operator, String operation, String target, String detail);
}
