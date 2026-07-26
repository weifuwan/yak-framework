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
import com.yak.job.common.domain.YakTask;
import com.yak.job.common.dto.YakTaskCopyDTO;
import com.yak.job.common.dto.YakTaskUpdateDTO;
import com.yak.job.common.dto.TaskPageQueryDTO;
import com.yak.job.common.vo.YakTaskVO;
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
@RequestMapping(value={"v1/yak-job/task"})
@Api(tags={"yak-job \u7684\u4efb\u52a1\u76f8\u5173\u63a5\u53e3"})
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
    public PagingResult<YakTaskVO> getAll(@RequestBody TaskPageQueryDTO taskPageQueryDTO) {
        taskPageQueryDTO.setTaskDesc(CommonUtil.sqlFuzzyQueryTransfer(taskPageQueryDTO.getTaskDesc()));
        taskPageQueryDTO.setClassName(CommonUtil.sqlFuzzyQueryTransfer(taskPageQueryDTO.getClassName()));
        List<YakTask> yakTasks = this.taskManager.getPagineList(taskPageQueryDTO);
        int count = this.taskManager.pagineTaskConut(taskPageQueryDTO);
        return PagingResult.buildSucc(this.yakTask2YakTaskVO(yakTasks), count, taskPageQueryDTO.getPage().intValue(), taskPageQueryDTO.getSize().intValue());
    }

    @PostMapping(value={"/{taskCode}/{status}"})
    @ApiOperation(value="\u66f4\u65b0\u8c03\u5ea6\u4efb\u52a1\u72b6\u6001", notes="")
    public Result<Boolean> status(@PathVariable String taskCode, @PathVariable Integer status) {
        return this.taskManager.updateTaskStatus(taskCode, status);
    }

    @GetMapping(value={"/{taskCode}/detail"})
    @ApiOperation(value="\u8c03\u5ea6\u4efb\u52a1\u8be6\u60c5", notes="")
    public Result<YakTaskVO> detail(@PathVariable String taskCode) {
        return Result.buildSucc(this.yakTask2YakTaskVO(this.taskManager.getByCode(taskCode)));
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
    public Result<Boolean> copy(@PathVariable String taskCode, @RequestBody YakTaskCopyDTO yakTaskCopyDTO) {
        return this.taskManager.copy(taskCode, yakTaskCopyDTO.getTaskDesc(), yakTaskCopyDTO.getWorkerIps(), yakTaskCopyDTO.getParam());
    }

    @PostMapping(value={"/{taskCode}/update"})
    @ApiOperation(value="\u7f16\u8f91\u8c03\u5ea6\u4efb\u52a1", notes="")
    public Result<Boolean> update(@PathVariable String taskCode, @RequestBody YakTaskUpdateDTO yakTaskUpdateDTO) {
        return this.taskManager.updateWorkIpsParam(taskCode, yakTaskUpdateDTO.getWorkerIps(), yakTaskUpdateDTO.getParam());
    }

    private List<YakTaskVO> yakTask2YakTaskVO(List<YakTask> yakTasks) {
        if (CollectionUtils.isEmpty(yakTasks)) {
            return new ArrayList<YakTaskVO>();
        }
        return yakTasks.stream().map(l -> this.yakTask2YakTaskVO((YakTask)l)).collect(Collectors.toList());
    }

    private YakTaskVO yakTask2YakTaskVO(YakTask yakTask) {
        YakTaskVO yakTaskVO = BeanUtil.convertTo(yakTask, YakTaskVO.class);
        yakTaskVO.setDel(0);
        if (!StringUtils.isEmpty((Object)yakTask.getTaskCode()) && CommonUtil.isCopyTask(yakTask.getTaskCode())) {
            yakTaskVO.setDel(1);
        }
        if (!CollectionUtils.isEmpty(yakTask.getTaskWorkers())) {
            List<String> ips = yakTask.getTaskWorkers().stream().map(w -> w.getIp()).collect(Collectors.toList());
            yakTaskVO.setRouting(ConsensualEnum.getByName(yakTask.getConsensual()).getDesc());
            yakTaskVO.setWorkerIps(ips);
        }
        return yakTaskVO;
    }
}

