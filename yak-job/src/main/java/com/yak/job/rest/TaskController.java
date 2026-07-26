/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.Api
 *  io.swagger.annotations.ApiOperation
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.util.CollectionUtils
 *  org.springframework.util.StringUtils
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.yak.job.rest;

import com.yak.job.common.CommonUtil;
import com.yak.job.common.PagingResult;
import com.yak.job.common.Result;
import com.yak.job.common.domain.LogITask;
import com.yak.job.common.dto.LogITaskCopyDTO;
import com.yak.job.common.dto.LogITaskUpdateDTO;
import com.yak.job.common.dto.TaskPageQueryDTO;
import com.yak.job.common.vo.LogITaskVO;
import com.yak.job.core.consensual.ConsensualEnum;
import com.yak.job.core.task.TaskManager;
import com.yak.job.utils.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"v1/logi-job/task"})
@Api(tags={"logi-job \u7684\u4efb\u52a1\u76f8\u5173\u63a5\u53e3"})
public class TaskController {
    @Autowired
    private TaskManager taskManager;

    @PostMapping(value={"/{taskCode}/do"})
    @ApiOperation(value="\u6267\u884c\u8c03\u5ea6\u4efb\u52a1", notes="")
    public Result<Boolean> execute(@PathVariable String taskCode) {
        return this.taskManager.execute(taskCode, (Boolean)false);
    }

    @PostMapping(value={"/list"})
    @ApiOperation(value="\u83b7\u53d6\u6240\u6709\u7684\u8c03\u5ea6\u4efb\u52a1", notes="")
    public PagingResult<LogITaskVO> getAll(@RequestBody TaskPageQueryDTO taskPageQueryDTO) {
        taskPageQueryDTO.setTaskDesc(CommonUtil.sqlFuzzyQueryTransfer(taskPageQueryDTO.getTaskDesc()));
        taskPageQueryDTO.setClassName(CommonUtil.sqlFuzzyQueryTransfer(taskPageQueryDTO.getClassName()));
        List<LogITask> logITasks = this.taskManager.getPagineList(taskPageQueryDTO);
        int count = this.taskManager.pagineTaskConut(taskPageQueryDTO);
        return PagingResult.buildSucc(this.logITask2LogITaskVO(logITasks), count, taskPageQueryDTO.getPage().intValue(), taskPageQueryDTO.getSize().intValue());
    }

    @PostMapping(value={"/{taskCode}/{status}"})
    @ApiOperation(value="\u66f4\u65b0\u8c03\u5ea6\u4efb\u52a1\u72b6\u6001", notes="")
    public Result<Boolean> status(@PathVariable String taskCode, @PathVariable Integer status) {
        return this.taskManager.updateTaskStatus(taskCode, status);
    }

    @GetMapping(value={"/{taskCode}/detail"})
    @ApiOperation(value="\u8c03\u5ea6\u4efb\u52a1\u8be6\u60c5", notes="")
    public Result<LogITaskVO> detail(@PathVariable String taskCode) {
        return Result.buildSucc(this.logITask2LogITaskVO(this.taskManager.getByCode(taskCode)));
    }

    @PostMapping(value={"/{taskCode}/{workerCode}/release"})
    @ApiOperation(value="\u6062\u590d\u8c03\u5ea6\u4efb\u52a1", notes="")
    public Result<Boolean> release(@PathVariable String taskCode, @PathVariable String workerCode) {
        return this.taskManager.release(taskCode, workerCode);
    }

    @DeleteMapping(value={"/{taskCode}"})
    @ApiOperation(value="\u5220\u9664\u8c03\u5ea6\u4efb\u52a1", notes="")
    public Result<Boolean> delete(@PathVariable String taskCode) {
        return this.taskManager.delete(taskCode);
    }

    @PostMapping(value={"/{taskCode}/copy"})
    @ApiOperation(value="\u590d\u5236\u8c03\u5ea6\u4efb\u52a1", notes="")
    public Result<Boolean> copy(@PathVariable String taskCode, @RequestBody LogITaskCopyDTO logITaskCopyDTO) {
        return this.taskManager.copy(taskCode, logITaskCopyDTO.getTaskDesc(), logITaskCopyDTO.getWorkerIps(), logITaskCopyDTO.getParam());
    }

    @PostMapping(value={"/{taskCode}/update"})
    @ApiOperation(value="\u7f16\u8f91\u8c03\u5ea6\u4efb\u52a1", notes="")
    public Result<Boolean> update(@PathVariable String taskCode, @RequestBody LogITaskUpdateDTO logITaskUpdateDTO) {
        return this.taskManager.updateWorkIpsParam(taskCode, logITaskUpdateDTO.getWorkerIps(), logITaskUpdateDTO.getParam());
    }

    private List<LogITaskVO> logITask2LogITaskVO(List<LogITask> logITasks) {
        if (CollectionUtils.isEmpty(logITasks)) {
            return new ArrayList<LogITaskVO>();
        }
        return logITasks.stream().map(l -> this.logITask2LogITaskVO((LogITask)l)).collect(Collectors.toList());
    }

    private LogITaskVO logITask2LogITaskVO(LogITask logITask) {
        LogITaskVO logITaskVO = BeanUtil.convertTo(logITask, LogITaskVO.class);
        logITaskVO.setDel(0);
        if (!StringUtils.isEmpty((Object)logITask.getTaskCode()) && CommonUtil.isCopyTask(logITask.getTaskCode())) {
            logITaskVO.setDel(1);
        }
        if (!CollectionUtils.isEmpty(logITask.getTaskWorkers())) {
            List<String> ips = logITask.getTaskWorkers().stream().map(w -> w.getIp()).collect(Collectors.toList());
            logITaskVO.setRouting(ConsensualEnum.getByName(logITask.getConsensual()).getDesc());
            logITaskVO.setWorkerIps(ips);
        }
        return logITaskVO;
    }
}

