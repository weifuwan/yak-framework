/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.job.common.vo;

import lombok.Data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.util.List;

/**
 * 任务调度配置视图对象。
 *
 * @author weifuwan
 */
@Data
@ApiModel(description = "YakTaskVO \u4efb\u52a1\u8be6\u60c5")
public class YakTaskVO {
    /** 主键标识。 */
    @ApiModelProperty(value = "taskcode")
    private Long id;
    /** 任务编码。 */
    @ApiModelProperty(value = "taskcode")
    private String taskCode;
    /** 任务名称。 */
    @ApiModelProperty(value = "\u4efb\u52a1\u540d\u79f0")
    private String taskName;
    /** 任务负责人。 */
    @ApiModelProperty(value = "\u4efb\u52a1\u8d23\u4efb\u4eba")
    private String owner;
    /** 任务描述。 */
    @ApiModelProperty(value = "\u4efb\u52a1\u63cf\u8ff0")
    private String taskDesc;
    /** 任务调度 Cron 表达式。 */
    @ApiModelProperty(value = "\u5b9a\u65f6\u4efb\u52a1\u8c03\u5ea6\u65f6\u95f4\u8868\u8fbe\u5f0f")
    private String cron;
    /** 任务执行类名。 */
    @ApiModelProperty(value = "\u5b9a\u65f6\u4efb\u52a1\u8c03\u5ea6\u6267\u884c\u4ee3\u7801")
    private String className;
    /** 任务执行参数。 */
    @ApiModelProperty(value = "\u6267\u884c\u53c2\u6570 map \u5f62\u5f0f{key1:value1,key2:value2}")
    private String params;
    /** 上次调度执行时间。 */
    @ApiModelProperty(value = "\u4e0a\u6b21\u8c03\u5ea6\u6267\u884c\u65f6\u95f4")
    private Timestamp lastFireTime;
    /** 下次调度执行时间。 */
    @ApiModelProperty(value = "\u4e0b\u6b21\u8c03\u5ea6\u6267\u884c\u65f6\u95f4")
    private Timestamp nextFireTime;
    /** 状态。 */
    @ApiModelProperty(value = "\u4efb\u52a1\u72b6\u6001\uff0c1\uff1a\u6b63\u5e38\uff0c0\uff1a\u6682\u505c")
    private Integer status;
    /** 调度方式。 */
    @ApiModelProperty(value = "\u8c03\u5ea6\u65b9\u5f0f\uff1a\u5355\u64ad\u3001\u5e7f\u64ad")
    private String consensual;
    /** 应用名称。 */
    @ApiModelProperty(value = "\u5e94\u7528\u540d\u79f0")
    private String appName;
    /** 调度执行器 IP 列表。 */
    @ApiModelProperty(value = "\u8c03\u5ea6\u673a\u5668\u5217\u8868")
    private List<String> workerIps;
    /** 路由策略。 */
    @ApiModelProperty(value = "\u8def\u7531\u7b56\u7565")
    private String routing;
    /** 运行模式。 */
    @ApiModelProperty(value = "\u8fd0\u884c\u6a21\u5f0f")
    private String runningType = "BEAN\u6a21\u5f0f";
    /** 阻塞策略。 */
    @ApiModelProperty(value = "\u963b\u585e\u7b56\u7565")
    private String blockPolicy = "\u5355\u673a\u4e32\u884c";
    /** 是否允许删除。 */
    @ApiModelProperty(value = "\u662f\u5426\u53ef\u4ee5\u5220\u9664\uff0c1\uff1a\u53ef\u4ee5\uff0c0\uff1a\u4e0d\u53ef\u4ee5")
    private Integer del;
    /** 创建时间。 */
    @ApiModelProperty(value = "\u4efb\u52a1\u88ab\u8c03\u5ea6\u65f6\u95f4")
    private Timestamp createTime;
    /** 最后更新时间。 */
    @ApiModelProperty(value = "\u4efb\u52a1\u88ab\u8c03\u5ea6\u65f6\u95f4")
    private Timestamp updateTime;

}
