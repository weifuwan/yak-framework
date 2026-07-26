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
 * 任务调度执行日志视图对象。
 *
 * @author weifuwan
 */
@Data
@ApiModel(description = "YakJobLogVO \u8c03\u5ea6\u6267\u884c\u7684\u4efb\u52a1\u8be6\u60c5")
public class YakJobLogVO {
    /**
     * 主键标识。
     */
    @ApiModelProperty(value = "\u8c03\u5ea6\u6267\u884c\u7684\u4efb\u52a1id")
    private Long id;
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
     * 任务标识。
     */
    @ApiModelProperty(value = "\u914d\u7f6e\u7684\u4efb\u52a1Id")
    private Long taskId;
    /**
     * 任务名称。
     */
    @ApiModelProperty(value = "\u914d\u7f6e\u7684\u4efb\u52a1\u540d\u79f0")
    private String taskName;
    /**
     * 任务描述。
     */
    @ApiModelProperty(value = "\u914d\u7f6e\u7684\u4efb\u52a1\u63cf\u8ff0")
    private String taskDesc;
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
     * 任务开始执行时间。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u5f00\u59cb\u6267\u884c\u65f6\u95f4")
    private Timestamp startTime;
    /**
     * 任务结束执行时间。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u7ed3\u675f\u6267\u884c\u65f6\u95f4")
    private Timestamp endTime;
    /**
     * 创建时间。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u88ab\u8c03\u5ea6\u65f6\u95f4")
    private Timestamp createTime;
    /**
     * 最后更新时间。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u88ab\u8c03\u5ea6\u65f6\u95f4")
    private Timestamp updateTime;
    /**
     * 状态。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u8c03\u5ea6\u7ed3\u679c\uff0c0:\u8c03\u5ea6\u542f\u52a8\u4e2d\u30011:\u8fd0\u884c\u4e2d\u3001 2\uff1a\u6210\u529f\u30013\uff1a\u5931\u8d25\u30014\uff1a\u53d6\u6d88")
    private Integer status;
    /**
     * 任务执行错误信息。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u6267\u884c\u9519\u8bef")
    private String error;
    /**
     * 任务执行结果。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u6267\u884c\u7ed3\u679c")
    private String result;
    /**
     * 全部可调度执行器 IP 列表。
     */
    @ApiModelProperty(value = "\u6240\u6709\u53ef\u88ab\u8c03\u5ea6\u7684\u673a\u5668\u5217\u8868")
    private List<String> allWorkerIps;
    /**
     * 实际执行器 IP 地址。
     */
    @ApiModelProperty(value = "\u8c03\u5ea6\u5230\u7684\u673a\u5668\u5217\u8868")
    private String workerIp;

}
