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
 * 任务日志分页查询数据传输对象。
 *
 * @author weifuwan
 */
@Data
@ApiModel(description = "\u4efb\u52a1\u65e5\u5fd7\u5206\u9875\u67e5\u627e\u6761\u4ef6\u4fe1\u606f")
public class TaskLogPageQueryDTO {
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
     * 开始时间。
     */
    @ApiModelProperty(value = "\u5f00\u59cb\u65f6\u95f4", dataType = "Long", required = false)
    private Long beginTime;
    /**
     * 结束时间。
     */
    @ApiModelProperty(value = "\u7ed3\u675f\u65f6\u95f4", dataType = "Long", required = false)
    private Long endTime;
    /**
     * 任务状态。
     */
    @ApiModelProperty(value = "\u4efb\u52a1\u72b6\u6001", dataType = "Integer", required = false)
    private Integer taskStatus;
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
     * 排序方向。
     */
    @ApiModelProperty(value = "\u6392\u5e8f\u65b9\u5f0f\uff0casc\uff1a\u6b63\u5e8f(\u4ece\u5c0f\u5230\u5927)\uff0cdesc\uff1a\u9006\u5e8f(\u4ece\u5927\u5230\u5c0f)", dataType = "String", required = false)
    private String sortAsc;
    /**
     * 排序字段。
     */
    @ApiModelProperty(value = "\u6392\u5e8f\u5b57\u6bb5\uff0c\u5fc5\u987b\u662f\uff1astatus\u3001result\u3001create_time\u3001start_time\u3001end_time", dataType = "String", required = false)
    private String sortName;

}
