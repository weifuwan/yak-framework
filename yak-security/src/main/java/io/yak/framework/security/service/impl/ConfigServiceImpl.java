package io.yak.framework.security.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.yak.framework.security.common.PagingData;
import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.config.ConfigDTO;
import io.yak.framework.security.common.dto.config.ConfigQueryDTO;
import io.yak.framework.security.common.enums.ConfigStatusEnum;
import io.yak.framework.security.common.po.ConfigPO;
import io.yak.framework.security.common.vo.config.ConfigVO;
import io.yak.framework.security.dao.ConfigDao;
import io.yak.framework.security.service.ConfigService;
import io.yak.framework.security.util.CopyBeanUtil;
import io.yak.framework.security.util.JsonUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfigServiceImpl implements ConfigService {
  private static final Logger log =
      LoggerFactory.getLogger(ConfigServiceImpl.class);
  private static final Logger LOGGER =
      LoggerFactory.getLogger(ConfigServiceImpl.class);
  private static final String NOT_EXIST = "\u914d\u7f6e\u4e0d\u5b58\u5728";
  private static final String SYSTEM = "system";
  @Autowired private ConfigDao configDao;

  @Override
  @Transactional(rollbackFor = {Exception.class})
  public Result<Integer> addConfig(ConfigDTO configInfoDTO, String user) {
    Result<Void> checkResult = this.checkParam(configInfoDTO);
    if (checkResult.failed()) {
      log.warn("class=ConfigVOServiceImpl||method=addConfig||msg={}||msg=" +
               "check fail!",
               (Object)checkResult.getMessage());
      return Result.buildFrom(checkResult);
    }
    configInfoDTO.setOperator(user);
    this.initConfig(configInfoDTO);
    ConfigPO oldConfig = this.getByGroupAndNameFromDB(
        configInfoDTO.getValueGroup(), configInfoDTO.getValueName());
    if (oldConfig != null) {
      return Result.buildDuplicate("\u914d\u7f6e\u91cd\u590d");
    }
    ConfigPO param = CopyBeanUtil.copy(configInfoDTO, ConfigPO.class);
    this.configDao.insert(param);
    return Result.buildSucc(param.getId());
  }

  @Override
  public Result<Integer> addConfig(String valueGroup, String valueName,
                                   String value, String user) {
    ConfigDTO configInfoDTO = new ConfigDTO();
    configInfoDTO.setValueGroup(valueGroup);
    configInfoDTO.setValueName(valueName);
    configInfoDTO.setValue(value);
    configInfoDTO.setStatus(ConfigStatusEnum.NORMAL.getCode());
    return this.addConfig(configInfoDTO, user);
  }

  @Override
  @Transactional(rollbackFor = {Exception.class})
  public Result<Void> delConfig(Integer configId, String user) {
    ConfigPO configInfoPO = this.configDao.getbyId(configId);
    if (configInfoPO == null) {
      return Result.buildNotExist(NOT_EXIST);
    }
    return Result.build(1 == this.configDao.deleteById(configInfoPO.getId()));
  }

  @Override
  @Transactional(rollbackFor = {Exception.class})
  public Result<Void> editConfig(ConfigDTO configInfoDTO, String user) {
    if (StringUtils.isBlank((CharSequence)configInfoDTO.getValueGroup()) ||
        StringUtils.isBlank((CharSequence)configInfoDTO.getValueName())) {
      return Result.buildParamIllegal(
          "\u914d\u7f6e\u7ec4\u6216\u8005\u914d\u7f6e\u540d\u4e3a\u7a7a");
    }
    if (ConfigStatusEnum.NORMAL.getCode() !=
            configInfoDTO.getStatus().intValue() &&
        ConfigStatusEnum.DISABLE.getCode() !=
            configInfoDTO.getStatus().intValue()) {
      return Result.buildParamIllegal(
          "\u72b6\u6001\u53ea\u80fd\u662f1\u6216\u80052");
    }
    ConfigPO configInfoPO = this.configDao.getByGroupAndName(
        configInfoDTO.getValueGroup(), configInfoDTO.getValueName());
    if (configInfoPO == null) {
      Result<Integer> retAdd = this.addConfig(configInfoDTO, user);
      return Result.buildFrom(retAdd);
    }
    configInfoPO.setOperator(user);
    boolean succ = 1 == this.configDao.update(
                            CopyBeanUtil.copy(configInfoDTO, ConfigPO.class));
    return Result.build(succ);
  }

  @Override
  @Transactional(rollbackFor = {Exception.class})
  public Result<Void> switchConfig(Integer configId, Integer status,
                                   String user) {
    ConfigPO configInfoPO = this.configDao.getbyId(configId);
    if (configInfoPO == null) {
      return Result.buildNotExist(NOT_EXIST);
    }
    ConfigStatusEnum statusEnum = ConfigStatusEnum.valueOf(status);
    if (statusEnum == null) {
      return Result.buildParamIllegal("\u72b6\u6001\u975e\u6cd5");
    }
    configInfoPO.setOperator(user);
    configInfoPO.setStatus(status);
    return Result.build(1 == this.configDao.updateById(configInfoPO));
  }

  @Override
  public PagingData<ConfigVO> pagingConfig(ConfigQueryDTO queryDTO) {
    IPage<ConfigPO> configPOs = this.configDao.selectPage(queryDTO);
    List<ConfigVO> configVOS =
        CopyBeanUtil.copyList(configPOs.getRecords(), ConfigVO.class);
    return new PagingData<ConfigVO>(configVOS, configPOs);
  }

  @Override
  public List<ConfigVO> queryByCondt(ConfigDTO param) {
    List<ConfigPO> configInfoPOs = this.configDao.listByCondition(
        CopyBeanUtil.copy(param, ConfigPO.class));
    return CopyBeanUtil.copyList(configInfoPOs, ConfigVO.class);
  }

  @Override
  public List<String> listGroups() {
    return this.configDao.listDistinctGroup();
  }

  @Override
  public List<ConfigVO> listConfigByGroup(String group) {
    List<ConfigPO> configInfoPOs = this.configDao.listConfigByGroup(group);
    return CopyBeanUtil.copyList(configInfoPOs, ConfigVO.class);
  }

  @Override
  public ConfigVO getConfigById(Integer configId) {
    return CopyBeanUtil.copy(this.configDao.getbyId(configId), ConfigVO.class);
  }

  @Override
  public String stringSetting(String group, String name, String defaultValue) {
    try {
      ConfigPO configInfoPO = this.getByGroupAndNameFromDB(group, name);
      if (configInfoPO == null ||
          StringUtils.isBlank((CharSequence)configInfoPO.getValue())) {
        return defaultValue;
      }
      return configInfoPO.getValue();
    } catch (Exception e) {
      log.warn("class=ConfigVOServiceImpl||method=stringSetting||group={}||" +
               "name={}||msg=get config error!",
               new Object[] {group, name, e});
      return defaultValue;
    }
  }

  @Override
  public Integer intSetting(String group, String name, Integer defaultValue) {
    try {
      ConfigPO configInfoPO = this.getByGroupAndNameFromDB(group, name);
      if (configInfoPO == null ||
          StringUtils.isBlank((CharSequence)configInfoPO.getValue())) {
        return defaultValue;
      }
      return Integer.valueOf(configInfoPO.getValue());
    } catch (NumberFormatException e) {
      LOGGER.warn("class=ConfigServiceImpl||method=intSetting||group={}||" +
                  "name={}||msg=get config error!",
                  (Object)group, (Object)name);
      return defaultValue;
    }
  }

  @Override
  public Long longSetting(String group, String name, Long defaultValue) {
    try {
      ConfigPO configInfoPO = this.getByGroupAndNameFromDB(group, name);
      if (configInfoPO == null ||
          StringUtils.isBlank((CharSequence)configInfoPO.getValue())) {
        return defaultValue;
      }
      return Long.valueOf(configInfoPO.getValue());
    } catch (Exception e) {
      LOGGER.warn("class=ConfigServiceImpl||method=longSetting||group={}||" +
                  "name={}||msg=get config error!",
                  (Object)group, (Object)name);
      return defaultValue;
    }
  }

  @Override
  public Double doubleSetting(String group, String name, Double defaultValue) {
    try {
      ConfigPO configInfoPO = this.getByGroupAndNameFromDB(group, name);
      if (configInfoPO == null ||
          StringUtils.isBlank((CharSequence)configInfoPO.getValue())) {
        return defaultValue;
      }
      return Double.valueOf(configInfoPO.getValue());
    } catch (Exception e) {
      LOGGER.warn("class=ConfigServiceImpl||method=doubleSetting||group={}||" +
                  "name={}||msg=get config error!",
                  new Object[] {group, name, e});
      return defaultValue;
    }
  }

  @Override
  public Boolean booleanSetting(String group, String name,
                                Boolean defaultValue) {
    ConfigPO configInfoPO = this.getByGroupAndNameFromDB(group, name);
    if (configInfoPO == null ||
        StringUtils.isBlank((CharSequence)configInfoPO.getValue())) {
      return defaultValue;
    }
    return Boolean.valueOf(configInfoPO.getValue());
  }

  @Override
  public <T> T objectSetting(String group, String name, T defaultValue,
                             Class<T> clazz) {
    try {
      ConfigPO configInfoPO = this.getByGroupAndNameFromDB(group, name);
      if (configInfoPO == null ||
          StringUtils.isBlank((CharSequence)configInfoPO.getValue())) {
        return defaultValue;
      }
      return (T)JsonUtils.fromJson(configInfoPO.getValue(), clazz);
    } catch (Exception e) {
      LOGGER.warn("class=ConfigServiceImpl||method=objectSetting||group={}||" +
                  "name={}||msg=get config error!",
                  new Object[] {group, name, e});
      return defaultValue;
    }
  }

  private Result<Void> checkParam(ConfigDTO configInfoDTO) {
    if (null == configInfoDTO) {
      return Result.buildParamIllegal("\u914d\u7f6e\u4fe1\u606f\u4e3a\u7a7a");
    }
    if (StringUtils.isBlank((CharSequence)configInfoDTO.getValueGroup())) {
      return Result.buildParamIllegal("\u7ec4\u4e3a\u7a7a");
    }
    if (StringUtils.isBlank((CharSequence)configInfoDTO.getValueName())) {
      return Result.buildParamIllegal("\u540d\u5b57\u4e3a\u7a7a");
    }
    return Result.buildSucc(null);
  }

  private void initConfig(ConfigDTO configInfoDTO) {
    if (configInfoDTO.getStatus() == null) {
      configInfoDTO.setStatus(ConfigStatusEnum.NORMAL.getCode());
    }
    if (configInfoDTO.getValue() == null) {
      configInfoDTO.setValue("");
    }
    if (configInfoDTO.getMemo() == null) {
      configInfoDTO.setMemo("");
    }
  }

  private ConfigPO getByGroupAndNameFromDB(String group, String valueName) {
    return this.configDao.getByGroupAndName(group, valueName);
  }
}
