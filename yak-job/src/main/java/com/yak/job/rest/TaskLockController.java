/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.yak.job.rest;

import com.yak.job.common.Result;
import com.yak.job.common.vo.YakTaskLockVO;
import com.yak.job.core.task.TaskLockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"v1/yak-job/taskLock"})
@Api(tags = {"yak-job \u7684\u4efb\u52a1\u9501\u76f8\u5173\u63a5\u53e3"})
public class TaskLockController {
    @Autowired
    private TaskLockService taskLockService;

    @PostMapping(value = {"/release"})
    @ApiOperation(value = "\u91ca\u653e\u67d0\u4e00\u4e2a\u9501\u4f4f\u7684\u4efb\u52a1", notes = "")
    public Result<Boolean> release(@RequestParam String taskCode, @RequestParam String workerCode) {
        return Result.buildSucc(this.taskLockService.tryRelease(taskCode, workerCode));
    }

    @GetMapping(value = {"/getAll"})
    @ApiOperation(value = "\u83b7\u53d6\u6240\u6709\u9501\u4f4f\u7684\u4efb\u52a1", notes = "")
    public Result<List<YakTaskLockVO>> getAll() {
        return Result.buildSucc(this.taskLockService.getAll());
    }
}

