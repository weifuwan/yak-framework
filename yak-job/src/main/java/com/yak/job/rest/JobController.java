/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.yak.job.rest;

import com.yak.job.common.Result;
import com.yak.job.core.job.JobManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"v1/logi-job/job"})
@Api(tags={"logi-job \u6267\u884c\u751f\u6210\u7684\u4f5c\u4e1a\u76f8\u5173\u63a5\u53e3"})
public class JobController {
    private static final Logger logger = LoggerFactory.getLogger(JobController.class);
    @Autowired
    private JobManager jobManager;

    @PostMapping(value={"/{jobCode}/stop"})
    @ApiOperation(value="\u505c\u6b62\u4e00\u4e2a\u4f5c\u4e1a\u7684\u6267\u884c", notes="")
    public Result<Boolean> stop(@PathVariable String jobCode) {
        return Result.buildSucc(this.jobManager.stopByJobCode(jobCode));
    }

    @GetMapping(value={"/runningJobs"})
    @ApiOperation(value="\u83b7\u53d6\u6240\u6709\u5728\u6267\u884c\u7684\u4f5c\u4e1a", notes="")
    public Result getRunningJobs() {
        return Result.buildSucc(this.jobManager.getJobs());
    }
}

