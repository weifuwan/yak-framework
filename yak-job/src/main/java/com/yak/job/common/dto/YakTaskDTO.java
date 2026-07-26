/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.job.common.dto;

import lombok.Data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 任务数据传输对象。
 *
 * @author weifuwan
 */
@Data
@ApiModel(description = "YakTask \u4efb\u52a1\u4fe1\u606f")
public class YakTaskDTO {
    /**
     * 名称。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u540d\u79f0")
    private String name;
    /**
     * 描述。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u63cf\u8ff0")
    private String description;
    /**
     * Cron 表达式。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u8c03\u5ea6\u65f6\u95f4\u8868\u8fbe\u5f0f")
    private String cron;
    /**
     * 任务处理类名。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u5bf9\u5e94\u7684\u7c7b\u540d")
    private String className;
    /**
     * 任务参数。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u6267\u884c\u53c2\u6570")
    private String params;
    /**
     * 重试次数。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u91cd\u8bd5\u6b21\u6570")
    private Integer retryTimes;
    /**
     * 是否采用一致性执行。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u62a2\u5360\u6a21\u5f0f")
    private String consensual;

}
