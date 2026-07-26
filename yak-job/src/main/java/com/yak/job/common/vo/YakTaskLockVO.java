/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.job.common.vo;

import lombok.Data;

import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

/**
 * 任务调度锁视图对象。
 *
 * @author weifuwan
 */
@Data
public class YakTaskLockVO {
    /**
     * 主键标识。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u9501id")
    private Long id;
    /**
     * 任务编码。
     */
    @ApiModelProperty(value = "\u4efb\u52a1code")
    private String taskCode;
    /**
     * 执行器编码。
     */
    @ApiModelProperty(value = "\u8c03\u5ea6\u5668")
    private String workerCode;
    /**
     * 创建时间。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u9501\u521b\u5efa\u65f6\u95f4")
    private Timestamp createTime;
    /**
     * 最后更新时间。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u9501\u66f4\u65b0\u65f6\u95f4")
    private Timestamp updateTime;

}
