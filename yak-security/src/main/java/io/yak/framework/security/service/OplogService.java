package io.yak.framework.security.service;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.dto.oplog.OplogDTO;
import io.yak.framework.security.common.dto.oplog.OplogQueryDTO;
import io.yak.framework.security.common.vo.oplog.OplogVO;
import java.util.List;

/**
 * 操作日志服务接口。
 */
public interface OplogService {
  /**
   * 保存操作日志。
   */
  Integer saveOplog(OplogDTO var1);

  /**
   * 分页查询操作日志。
   */
  PagingData<OplogVO> getOplogPage(OplogQueryDTO var1);

  /**
   * 根据操作日志 ID 查询日志详情。
   */
  OplogVO getOplogDetailByOplogId(Long var1);

  /**
   * 查询全部操作目标类型。
   */
  List<String> listTargetType();
}
