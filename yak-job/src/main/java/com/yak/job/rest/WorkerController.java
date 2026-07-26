/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.yak.job.rest;

import com.yak.job.common.Result;
import com.yak.job.core.worker.WorkerManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"v1/logi-job/worker"})
@Api(tags={"logi-job \u7684\u8c03\u5ea6\u673a\u5668\u76f8\u5173\u63a5\u53e3"})
public class WorkerController {
    @Autowired
    private WorkerManager workerManager;

    @GetMapping(value={"/list"})
    @ApiOperation(value="\u83b7\u53d6\u6240\u6709\u7684\u8c03\u5ea6\u5668\u5730\u5740", notes="")
    public Result<List<String>> getAll() {
        return this.workerManager.listAllWorkerIps();
    }
}

