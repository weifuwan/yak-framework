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

import java.sql.Timestamp;

/**
 * 作业日志数据传输对象。
 *
 * @author weifuwan
 */
@Data
@ApiModel(description = "YakTask \u4f5c\u4e1a\u65e5\u5fd7\u4fe1\u606f")
public class YakJobLogDTO {
    /** 作业编码。 */
    @ApiModelProperty(value = "\u4f5c\u4e1a\u540d\u79f0")
    private String jobCode;
    /** 所属任务编码。 */
    @ApiModelProperty(value = "\u4f5c\u4e1a\u6240\u5c5e\u4efb\u52a1\u540d\u79f0")
    private String taskCode;
    /** 任务处理类名。 */
    @ApiModelProperty(value = "\u4f5c\u4e1a\u6240\u5c5e\u4efb\u52a1\u7684\u7c7b\u540d")
    private String className;
    /** 重试次数。 */
    @ApiModelProperty(value = "\u4f5c\u4e1a\u5931\u8d25\u91cd\u8bd5\u6b21\u6570")
    private Integer tryTimes;
    /** 工作节点编码。 */
    @ApiModelProperty(value = "\u4f5c\u4e1a\u8c03\u5ea6\u5668\u5730\u5740")
    private String workerCode;
    /** 开始时间。 */
    @ApiModelProperty(value = "\u4f5c\u4e1a\u5f00\u59cb\u6267\u884c\u65f6\u95f4")
    private Timestamp startTime;
    /** 结束时间。 */
    @ApiModelProperty(value = "\u4f5c\u4e1a\u6267\u884c\u7ed3\u675f\u65f6\u95f4")
    private Timestamp endTime;
    /** 状态。 */
    @ApiModelProperty(value = "\u4f5c\u4e1a\u72b6\u6001")
    private Integer status;
    /** 错误信息。 */
    @ApiModelProperty(value = "\u4f5c\u4e1a\u6267\u884c\u5931\u8d25\u4fe1\u606f")
    private String error;
    /** 创建时间。 */
    @ApiModelProperty(value = "\u4f5c\u4e1a\u521b\u5efa\u65f6\u95f4")
    private Timestamp createTime;
    /** 更新时间。 */
    @ApiModelProperty(value = "\u4f5c\u4e1a\u66f4\u65b0\u65f6\u95f4")
    private Timestamp updateTime;
    /** 执行结果。 */
    @ApiModelProperty(value = "\u4f5c\u4e1a\u6267\u884c\u7ed3\u679c")
    private String result;
    /** 操作人。 */
    @ApiModelProperty(value = "\u4f5c\u4e1a\u6267\u884c\u4eba")
    private String operator;

}
