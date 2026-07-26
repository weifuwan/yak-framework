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

import java.util.List;

/**
 * 任务更新数据传输对象。
 *
 * @author weifuwan
 */
@Data
@ApiModel(description = "YakTask \u4efb\u52a1\u7f16\u8f91DTO")
public class YakTaskUpdateDTO {
    /** 工作节点 IP 列表。 */
    @ApiModelProperty(value = "\u8c03\u5ea6\u6267\u884c\u5668\u7684ip\u5217\u8868")
    private List<String> workerIps;
    /** 任务参数。 */
    @ApiModelProperty(value = "\u8c03\u5ea6\u6267\u884c\u5668\u7684\u53c2\u6570")
    private String param;

}
