package io.yak.framework.security.service;

import io.yak.framework.security.common.enums.oplog.OplogCode;
import java.util.List;

public interface OplogExtraService {
  public List<String> getOplogExtraNameListByType(Integer var1);

  public void saveOplogExtraList(List<String> var1, OplogCode var2);
}
