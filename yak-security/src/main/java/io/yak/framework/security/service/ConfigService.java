package io.yak.framework.security.service;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.config.ConfigDTO;
import io.yak.framework.security.common.dto.config.ConfigQueryDTO;
import io.yak.framework.security.common.vo.config.ConfigVO;
import java.util.List;

public interface ConfigService {
  public Result<Integer> addConfig(ConfigDTO var1, String var2);

  public Result<Integer> addConfig(String var1, String var2, String var3,
                                   String var4);

  public Result<Void> delConfig(Integer var1, String var2);

  public Result<Void> editConfig(ConfigDTO var1, String var2);

  public Result<Void> switchConfig(Integer var1, Integer var2, String var3);

  public PagingData<ConfigVO> pagingConfig(ConfigQueryDTO var1);

  public List<ConfigVO> queryByCondt(ConfigDTO var1);

  public List<String> listGroups();

  public List<ConfigVO> listConfigByGroup(String var1);

  public ConfigVO getConfigById(Integer var1);

  public String stringSetting(String var1, String var2, String var3);

  public Boolean booleanSetting(String var1, String var2, Boolean var3);

  public Integer intSetting(String var1, String var2, Integer var3);

  public Long longSetting(String var1, String var2, Long var3);

  public Double doubleSetting(String var1, String var2, Double var3);

  public <T> T objectSetting(String var1, String var2, T var3, Class<T> var4);
}
