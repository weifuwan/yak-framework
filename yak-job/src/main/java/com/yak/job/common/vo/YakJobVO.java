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

/**
 * 任务调度执行详情视图对象。
 *
 * @author weifuwan
 */
@Data
@ApiModel(description = "YakJobVO \u8c03\u5ea6\u6267\u884c\u7684\u4efb\u52a1\u8be6\u60c5")
public class YakJobVO {
    /**
     * 调度任务编码。
     */
    @ApiModelProperty(value = "\u8c03\u5ea6\u6267\u884c\u7684\u4efb\u52a1")
    private String jobCode;
    /**
     * 任务编码。
     */
    @ApiModelProperty(value = "\u914d\u7f6e\u7684\u4efb\u52a1")
    private String taskCode;
    /**
     * 任务执行类名。
     */
    @ApiModelProperty(value = "\u5b9a\u65f6\u4efb\u52a1\u8c03\u5ea6\u6267\u884c\u4ee3\u7801")
    private String className;
    /**
     * 执行器编码。
     */
    @ApiModelProperty(value = "\u8c03\u5ea6\u6267\u884c\u7684\u673a\u5668")
    private String workerCode;
    /**
     * 任务执行错误信息。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u6267\u884c\u9519\u8bef")
    private String error;
    /**
     * 创建时间。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u88ab\u8c03\u5ea6\u65f6\u95f4")
    private Timestamp createTime;
    /**
     * 任务执行结果。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u6267\u884c\u7ed3\u679c")
    private String result;

}
