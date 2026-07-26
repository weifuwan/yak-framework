package io.yak.framework.security.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.yak.framework.security.common.dto.oplog.OplogQueryDTO;
import io.yak.framework.security.common.entity.Oplog;
import java.util.List;

public interface OplogDao {
  public IPage<Oplog> selectPageWithoutDetail(OplogQueryDTO var1);

  public Oplog selectByOplogId(Long oplogId);

  public void insert(Oplog var1);

  public List<String> listTargetType();
}
