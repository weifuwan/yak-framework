package io.yak.framework.security.service;

import io.yak.framework.security.common.enums.oplog.OplogCode;
import java.util.List;

/**
 * 操作日志扩展服务接口。
 */
public interface OplogExtraService {
  /**
   * 根据类型查询操作日志扩展名称。
   */
  List<String> getOplogExtraNameListByType(Integer var1);

  /**
   * 保存操作日志扩展信息。
   */
  void saveOplogExtraList(List<String> var1, OplogCode var2);
}
