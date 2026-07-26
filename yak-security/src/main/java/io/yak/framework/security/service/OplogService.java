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
  Integer saveOplog(OplogDTO var1);

  PagingData<OplogVO> getOplogPage(OplogQueryDTO var1);

  OplogVO getOplogDetailByOplogId(Long var1);

  List<String> listTargetType();
}
