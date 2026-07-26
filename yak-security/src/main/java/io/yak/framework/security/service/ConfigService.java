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
  /**
   * 新增配置。
   */
  Result<Integer> addConfig(ConfigDTO var1, String var2);

  /**
   * 新增配置。
   */
  Result<Integer> addConfig(String var1, String var2, String var3,
                                   String var4);

  /**
   * 删除配置。
   */
  Result<Void> delConfig(Long var1, String var2);

  /**
   * 编辑配置。
   */
  Result<Void> editConfig(ConfigDTO var1, String var2);

  /**
   * 切换配置状态。
   */
  Result<Void> switchConfig(Long var1, Integer var2, String var3);

  /**
   * 分页查询配置。
   */
  PagingData<ConfigVO> pagingConfig(ConfigQueryDTO var1);

  /**
   * 根据条件查询配置。
   */
  List<ConfigVO> queryByCondt(ConfigDTO var1);

  /**
   * 查询全部配置分组。
   */
  List<String> listGroups();

  /**
   * 根据分组查询配置。
   */
  List<ConfigVO> listConfigByGroup(String var1);

  /**
   * 根据配置 ID 查询配置。
   */
  ConfigVO getConfigById(Long var1);

  /**
   * 获取字符串类型配置值。
   */
  String stringSetting(String var1, String var2, String var3);

  /**
   * 获取布尔类型配置值。
   */
  Boolean booleanSetting(String var1, String var2, Boolean var3);

  /**
   * 获取整数类型配置值。
   */
  Integer intSetting(String var1, String var2, Integer var3);

  /**
   * 获取长整数类型配置值。
   */
  Long longSetting(String var1, String var2, Long var3);

  /**
   * 获取双精度浮点类型配置值。
   */
  Double doubleSetting(String var1, String var2, Double var3);

  /**
   * 获取对象类型配置值。
   */
  <T> T objectSetting(String var1, String var2, T var3, Class<T> var4);
}
