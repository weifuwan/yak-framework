package io.yak.framework.security.service;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.dto.oplog.OplogDTO;
import io.yak.framework.security.common.dto.oplog.OplogQueryDTO;
import io.yak.framework.security.common.vo.oplog.OplogVO;
import java.util.List;

public interface OplogService {
  public Integer saveOplog(OplogDTO var1);

  public PagingData<OplogVO> getOplogPage(OplogQueryDTO var1);

  public OplogVO getOplogDetailByOplogId(Long var1);

  public List<String> listTargetType();
}
