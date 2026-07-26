/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.yak.job.rest;

import com.yak.job.common.CommonUtil;
import com.yak.job.common.PagingResult;
import com.yak.job.common.dto.TaskLogPageQueryDTO;
import com.yak.job.common.vo.YakJobLogVO;
import com.yak.job.core.job.JobLogManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"v1/yak-job/logs"})
@Api(tags = {"yak-job \u6267\u884c\u751f\u6210\u7684\u4f5c\u4e1a\u65e5\u5fd7\u76f8\u5173\u63a5\u53e3"})
public class JobLogsController {
    @Autowired
    private JobLogManager jobLogManager;

    @PostMapping(value = {"/list"})
    @ApiOperation(value = "\u5206\u9875\u83b7\u53d6\u4f5c\u4e1a\u6267\u884c\u65e5\u5fd7", notes = "")
    public PagingResult<YakJobLogVO> getJobLogs(@RequestBody TaskLogPageQueryDTO pageQueryDTO) {
        pageQueryDTO.setTaskDesc(CommonUtil.sqlFuzzyQueryTransfer(pageQueryDTO.getTaskDesc()));
        List<YakJobLogVO> yakJobLogVOS = this.jobLogManager.pageJobLogs(pageQueryDTO);
        int totalCount = this.jobLogManager.getJobLogsCount(pageQueryDTO);
        return PagingResult.buildSucc(yakJobLogVOS, totalCount, pageQueryDTO.getPage().intValue(), pageQueryDTO.getSize().intValue());
    }
}

