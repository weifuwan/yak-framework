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
package com.didiglobal.logi.job.rest;

import com.didiglobal.logi.job.common.CommonUtil;
import com.didiglobal.logi.job.common.PagingResult;
import com.didiglobal.logi.job.common.dto.TaskLogPageQueryDTO;
import com.didiglobal.logi.job.common.vo.LogIJobLogVO;
import com.didiglobal.logi.job.core.job.JobLogManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"v1/logi-job/logs"})
@Api(tags={"logi-job \u6267\u884c\u751f\u6210\u7684\u4f5c\u4e1a\u65e5\u5fd7\u76f8\u5173\u63a5\u53e3"})
public class JobLogsController {
    @Autowired
    private JobLogManager jobLogManager;

    @PostMapping(value={"/list"})
    @ApiOperation(value="\u5206\u9875\u83b7\u53d6\u4f5c\u4e1a\u6267\u884c\u65e5\u5fd7", notes="")
    public PagingResult<LogIJobLogVO> getJobLogs(@RequestBody TaskLogPageQueryDTO pageQueryDTO) {
        pageQueryDTO.setTaskDesc(CommonUtil.sqlFuzzyQueryTransfer(pageQueryDTO.getTaskDesc()));
        List<LogIJobLogVO> logIJobLogVOS = this.jobLogManager.pageJobLogs(pageQueryDTO);
        int totalCount = this.jobLogManager.getJobLogsCount(pageQueryDTO);
        return PagingResult.buildSucc(logIJobLogVOS, totalCount, pageQueryDTO.getPage().intValue(), pageQueryDTO.getSize().intValue());
    }
}

