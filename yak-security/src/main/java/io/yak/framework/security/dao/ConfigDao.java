package io.yak.framework.security.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.yak.framework.security.common.dto.config.ConfigQueryDTO;
import io.yak.framework.security.common.po.ConfigPO;
import java.util.List;

public interface ConfigDao {
  public int insert(ConfigPO var1);

  public int updateById(ConfigPO var1);

  public int update(ConfigPO var1);

  public int deleteById(Long var1);

  public IPage<ConfigPO> selectPage(ConfigQueryDTO var1);

  public List<ConfigPO> listByCondition(ConfigPO var1);

  public List<ConfigPO> listConfigByGroup(String var1);

  public List<String> listDistinctGroup();

  public ConfigPO getbyId(Long var1);

  public ConfigPO getByGroupAndName(String var1, String var2);
}
