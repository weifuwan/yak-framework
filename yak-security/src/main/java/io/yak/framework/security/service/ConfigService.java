package io.yak.framework.security.service;

import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.config.ConfigDTO;
import io.yak.framework.security.common.dto.config.ConfigQueryDTO;
import io.yak.framework.security.common.vo.config.ConfigVO;
import java.util.List;

/**
 * 配置服务接口。
 */
public interface ConfigService {
  Result<Integer> addConfig(ConfigDTO var1, String var2);

  Result<Integer> addConfig(String var1, String var2, String var3,
                                   String var4);

  Result<Void> delConfig(Long var1, String var2);

  Result<Void> editConfig(ConfigDTO var1, String var2);

  Result<Void> switchConfig(Long var1, Integer var2, String var3);

  PagingData<ConfigVO> pagingConfig(ConfigQueryDTO var1);

  List<ConfigVO> queryByCondt(ConfigDTO var1);

  List<String> listGroups();

  List<ConfigVO> listConfigByGroup(String var1);

  ConfigVO getConfigById(Long var1);

  String stringSetting(String var1, String var2, String var3);

  Boolean booleanSetting(String var1, String var2, Boolean var3);

  Integer intSetting(String var1, String var2, Integer var3);

  Long longSetting(String var1, String var2, Long var3);

  Double doubleSetting(String var1, String var2, Double var3);

  <T> T objectSetting(String var1, String var2, T var3, Class<T> var4);
}
