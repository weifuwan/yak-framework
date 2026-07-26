package io.yak.framework.security.service;

import io.yak.framework.security.common.enums.oplog.OplogCode;
import java.util.List;

/**
 * 操作日志扩展服务接口。
 */
public interface OplogExtraService {
  List<String> getOplogExtraNameListByType(Integer var1);

  void saveOplogExtraList(List<String> var1, OplogCode var2);
}
