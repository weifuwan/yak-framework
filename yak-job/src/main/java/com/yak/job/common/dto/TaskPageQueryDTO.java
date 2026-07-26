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
 * 任务分页查询数据传输对象。
 *
 * @author weifuwan
 */
@Data
@ApiModel(description = "\u5206\u9875\u67e5\u627e\u6761\u4ef6\u4fe1\u606f")
public class TaskPageQueryDTO {
    /**
     * 当前页码。
     */
    @ApiModelProperty(value = "\u5f53\u524d\u9875", dataType = "Integer", required = true)
    private Integer page = 1;
    /**
     * 每页记录数。
     */
    @ApiModelProperty(value = "\u6bcf\u9875\u5927\u5c0f", dataType = "Integer", required = true)
    private Integer size = 10;
    /**
     * 任务标识。
     */
    @ApiModelProperty(value = "\u4efb\u52a1id", dataType = "Long", required = false)
    private Long taskId;
    /**
     * 任务描述。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u63cf\u8ff0", dataType = "String", required = false)
    private String taskDesc;
    /**
     * 任务处理类名。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u5904\u7406\u5668", dataType = "String", required = false)
    private String className;
    /**
     * 任务状态。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u72b6\u6001", dataType = "Integer", required = false)
    private Integer taskStatus;

}
