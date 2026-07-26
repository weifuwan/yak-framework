package io.yak.framework.security.dao;

import io.yak.framework.security.common.entity.OplogExtra;
import java.util.List;

public interface OplogExtraDao {
  public List<OplogExtra> selectListByType(Integer var1);

  public void insertBatch(List<OplogExtra> var1);
}
