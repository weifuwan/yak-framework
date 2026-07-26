package io.yak.framework.security.common.dto.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.yak.framework.security.common.dto.PageParamDTO;
/**
 * 配置查询数据传输对象。
 *
 * @author weifuwan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ConfigQueryDTO extends PageParamDTO {
  /** 配置分组。 */
  private String valueGroup;
  /** 配置名称。 */
  private String valueName;
  /** 状态。 */
  private Integer status;
  /** 备注。 */
  private String memo;
  /** 操作人。 */
  private String operator;

}
