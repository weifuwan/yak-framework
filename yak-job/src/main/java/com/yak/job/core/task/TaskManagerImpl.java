/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Service
 *  org.springframework.util.CollectionUtils
 *  org.springframework.util.StringUtils
 */
package com.yak.job.core.task;

import com.yak.job.YakJobProperties;
import com.yak.job.common.CommonUtil;
import com.yak.job.common.Result;
import com.yak.job.common.domain.YakTask;
import com.yak.job.common.dto.YakTaskDTO;
import com.yak.job.common.dto.TaskPageQueryDTO;
import com.yak.job.common.enums.TaskStatusEnum;
import com.yak.job.common.enums.TaskWorkerStatusEnum;
import com.yak.job.common.po.YakTaskPO;
import com.yak.job.common.po.YakWorkerPO;
import com.yak.job.core.WorkerSingleton;
import com.yak.job.core.consensual.Consensual;
import com.yak.job.core.consensual.ConsensualEnum;
import com.yak.job.core.consensual.ConsensualFactory;
import com.yak.job.core.job.JobManager;
import com.yak.job.core.worker.WorkerManager;
import com.yak.job.mapper.YakTaskMapper;
import com.yak.job.utils.BeanUtil;
import com.yak.job.utils.CronExpression;
import com.yak.job.utils.IdWorker;
import com.yak.job.utils.ThreadUtil;
import com.google.common.collect.Lists;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class TaskManagerImpl
        implements TaskManager {
    private static final Logger logger = LoggerFactory.getLogger(TaskManagerImpl.class);
    private static final long WAIT_INTERVAL_SECONDS = 10L;
    private WorkerManager workerManager;
    private JobManager jobManager;
    private ConsensualFactory consensualFactory;
    private TaskLockService taskLockService;
    private YakTaskMapper yakTaskMapper;
    private YakJobProperties yakJobProperties;

    public TaskManagerImpl(WorkerManager workerManager, JobManager jobManager, ConsensualFactory consensualFactory, TaskLockService taskLockService, YakTaskMapper yakTaskMapper, YakJobProperties yakJobProperties) {
        this.workerManager = workerManager;
        this.jobManager = jobManager;
        this.consensualFactory = consensualFactory;
        this.taskLockService = taskLockService;
        this.yakTaskMapper = yakTaskMapper;
        this.yakJobProperties = yakJobProperties;
    }

    @Override
    public Result delete(String taskCode) {
        YakTaskPO yakTaskPO = this.yakTaskMapper.selectByCode(taskCode, this.yakJobProperties.getAppName());
        if (yakTaskPO == null) {
            return Result.buildFail("\u4efb\u52a1\u4e0d\u5b58\u5728\uff01");
        }
        return Result.buildSucc(this.yakTaskMapper.deleteByCode(taskCode, this.yakJobProperties.getAppName()) > 0);
    }

    @Override
    public boolean update(YakTaskDTO yakTaskDTO) {
        YakTaskPO yakTaskPO = BeanUtil.convertTo(yakTaskDTO, YakTaskPO.class);
        return this.yakTaskMapper.updateByCode(yakTaskPO) > 0;
    }

    @Override
    public List<YakTask> nextTriggers(Long interval) {
        return this.nextTriggers(System.currentTimeMillis(), interval);
    }

    @Override
    public List<YakTask> nextTriggers(Long fromTime, Long interval) {
        List<YakTask> yakTaskList = this.getAllRuning();
        yakTaskList = yakTaskList.stream().filter(taskInfo -> {
            try {
                if (ConsensualEnum.RANDOM.name().equals(taskInfo.getConsensual())) {
                    Timestamp lastFireTime = taskInfo.getLastFireTime();
                    List<YakTask.TaskWorker> taskWorkers = taskInfo.getTaskWorkers();
                    for (YakTask.TaskWorker taskWorker : taskWorkers) {
                        if (!Objects.equals(WorkerSingleton.getInstance().getYakWorker().getWorkerCode(), taskWorker.getWorkerCode()))
                            continue;
                        if (Objects.equals(taskWorker.getStatus(), TaskWorkerStatusEnum.WAITING.getValue())) break;
                        logger.info("class=TaskManagerImpl||method=nextTriggers||msg=has task running! taskCode={}, workerCode={}", (Object) taskInfo.getTaskCode(), (Object) taskWorker.getWorkerCode());
                        return false;
                    }
                    CronExpression cronExpression = new CronExpression(taskInfo.getCron());
                    long nextTime = cronExpression.getNextValidTimeAfter(lastFireTime).getTime();
                    taskInfo.setNextFireTime(new Timestamp(nextTime));
                    Timestamp timestamp = new Timestamp(fromTime + interval * 1000L);
                    return timestamp.after(taskInfo.getNextFireTime());
                }
                if (ConsensualEnum.BROADCAST.name().equals(taskInfo.getConsensual())) {
                    List<YakTask.TaskWorker> taskWorkers = taskInfo.getTaskWorkers();
                    Timestamp lastFireTime = new Timestamp(0L);
                    for (YakTask.TaskWorker taskWorker : taskWorkers) {
                        if (!Objects.equals(WorkerSingleton.getInstance().getYakWorker().getWorkerCode(), taskWorker.getWorkerCode()))
                            continue;
                        lastFireTime = taskWorker.getLastFireTime();
                    }
                    CronExpression cronExpression = new CronExpression(taskInfo.getCron());
                    long nextTime = cronExpression.getNextValidTimeAfter(lastFireTime).getTime();
                    taskInfo.setNextFireTime(new Timestamp(nextTime));
                    Timestamp timestamp = new Timestamp(fromTime + interval * 1000L);
                    if (timestamp.after(new Timestamp(nextTime))) {
                        if (nextTime + 10000L < fromTime && fromTime < nextTime + 20000L) {
                            logger.info("class=TaskManagerImpl||method=nextTriggers||nextTime={}||fromTime={}||msg=skip broadcast duplicate trigger!", (Object) nextTime, (Object) fromTime);
                            for (YakTask.TaskWorker taskWorker : taskWorkers) {
                                if (!Objects.equals(WorkerSingleton.getInstance().getYakWorker().getWorkerCode(), taskWorker.getWorkerCode()))
                                    continue;
                                taskWorker.setLastFireTime(new Timestamp(nextTime));
                                YakTaskPO yakTaskPO = BeanUtil.convertTo(taskInfo, YakTaskPO.class);
                                yakTaskPO.setTaskWorkerStr(BeanUtil.convertToJson(taskWorkers));
                                this.yakTaskMapper.updateTaskWorkStrByCode(yakTaskPO);
                                return false;
                            }
                        }
                        return true;
                    }
                    return false;
                }
                return false;
            } catch (Exception e) {
                logger.error("class=TaskManagerImpl||method=nextTriggers||msg=exception!", (Throwable) e);
                return false;
            }
        }).collect(Collectors.toList());
        yakTaskList.sort(Comparator.comparing(YakTask::getNextFireTime));
        return yakTaskList;
    }

    @Override
    public void submit(List<YakTask> yakTaskList) {
        if (CollectionUtils.isEmpty(yakTaskList)) {
            return;
        }
        for (YakTask yakTask : yakTaskList) {
            Consensual consensual = this.consensualFactory.getConsensual(yakTask.getConsensual());
            if (!consensual.canClaim(yakTask)) continue;
            this.execute(yakTask, (Boolean) false);
        }
    }

    @Override
    public Result execute(String taskCode, Boolean executeSubs) {
        YakTaskPO yakTaskPO = this.yakTaskMapper.selectByCode(taskCode, this.yakJobProperties.getAppName());
        if (yakTaskPO == null) {
            return Result.buildFail("\u4efb\u52a1\u4e0d\u5b58\u5728\uff01");
        }
        if (!this.taskLockService.tryAcquire(taskCode).booleanValue()) {
            return Result.buildFail("\u672a\u80fd\u83b7\u53d6\u5230\u6267\u884c\u9501\uff01");
        }
        YakTask yakTask = this.yakTaskPO2YakTask(yakTaskPO);
        yakTask.setTaskCallback(code -> this.taskLockService.tryRelease(code));
        this.execute(yakTask, (Boolean) false);
        return Result.buildSucc();
    }

    @Override
    public void execute(YakTask yakTask, Boolean executeSubs) {
        Timestamp lastFireTime = new Timestamp(System.currentTimeMillis());
        YakTaskPO yakTaskPO = BeanUtil.convertTo(yakTask, YakTaskPO.class);
        List<YakTask.TaskWorker> taskWorkers = yakTask.getTaskWorkers();
        boolean copyTask = CommonUtil.isCopyTask(yakTask.getTaskCode());
        boolean worked = false;
        for (YakTask.TaskWorker taskWorker : taskWorkers) {
            if (!Objects.equals(taskWorker.getWorkerCode(), WorkerSingleton.getInstance().getYakWorker().getWorkerCode()))
                continue;
            taskWorker.setLastFireTime(lastFireTime);
            taskWorker.setStatus(TaskWorkerStatusEnum.RUNNING.getValue());
            worked = true;
            break;
        }
        if (!copyTask && !worked) {
            taskWorkers.add(new YakTask.TaskWorker(TaskWorkerStatusEnum.RUNNING.getValue(), new Timestamp(System.currentTimeMillis()), WorkerSingleton.getInstance().getYakWorker().getWorkerCode(), WorkerSingleton.getInstance().getYakWorker().getIp()));
        }
        yakTaskPO.setTaskWorkerStr(BeanUtil.convertToJson(taskWorkers));
        yakTaskPO.setLastFireTime(lastFireTime);
        this.yakTaskMapper.updateByCode(yakTaskPO);
        this.executeInternal(yakTask, executeSubs);
    }

    @Override
    public int stopAll() {
        return this.jobManager.stopAll();
    }

    @Override
    public Result<Boolean> updateTaskStatus(String taskCode, int status) {
        if (!TaskStatusEnum.isValid(status)) {
            return Result.buildFail("status error");
        }
        YakTaskPO yakTaskPO = this.yakTaskMapper.selectByCode(taskCode, this.yakJobProperties.getAppName());
        if (null == yakTaskPO) {
            return Result.buildFail("task \u4e0d\u5b58\u5728");
        }
        if (TaskStatusEnum.STOP.getValue() == status && !this.jobManager.stopByTaskCode(taskCode)) {
            return Result.buildFail("stop task error");
        }
        if (TaskStatusEnum.RUNNING.getValue() == status) {
            this.execute(yakTaskPO.getTaskCode(), (Boolean) false);
        }
        yakTaskPO.setStatus(status);
        return Result.buildSucc(this.yakTaskMapper.updateByCode(yakTaskPO) > 0);
    }

    @Override
    public List<YakTask> getAllRuning() {
        List<YakTaskPO> yakTaskPOList = this.yakTaskMapper.selectRuningByAppName(this.yakJobProperties.getAppName());
        if (CollectionUtils.isEmpty(yakTaskPOList)) {
            return new ArrayList<YakTask>();
        }
        return yakTaskPOList.stream().map(p -> this.yakTaskPO2YakTask((YakTaskPO) p)).collect(Collectors.toList());
    }

    @Override
    public List<YakTask> getPagineList(TaskPageQueryDTO queryDTO) {
        List<YakTaskPO> yakTaskPOList = this.yakTaskMapper.pagineListByCondition(this.yakJobProperties.getAppName(), queryDTO.getTaskId(), queryDTO.getTaskDesc(), queryDTO.getClassName(), queryDTO.getTaskStatus(), (queryDTO.getPage() - 1) * queryDTO.getSize(), queryDTO.getSize());
        if (CollectionUtils.isEmpty(yakTaskPOList)) {
            return new ArrayList<YakTask>();
        }
        return yakTaskPOList.stream().map(p -> this.yakTaskPO2YakTask((YakTaskPO) p)).collect(Collectors.toList());
    }

    @Override
    public int pagineTaskConut(TaskPageQueryDTO queryDTO) {
        return this.yakTaskMapper.pagineCountByCondition(this.yakJobProperties.getAppName(), queryDTO.getTaskId(), queryDTO.getTaskDesc(), queryDTO.getClassName(), queryDTO.getTaskStatus());
    }

    @Override
    public Result<Boolean> release(String taskCode, String workerCode) {
        Boolean lockRet = this.taskLockService.tryRelease(taskCode, workerCode);
        if (!lockRet.booleanValue()) {
            return Result.buildFail("\u91ca\u653e\u9501\u5931\u8d25\uff01");
        }
        boolean updateResult = this.updateTaskWorker(taskCode, workerCode);
        if (!updateResult) {
            return Result.buildFail("\u66f4\u65b0\u9501\u5931\u8d25\uff01");
        }
        return Result.buildSucc();
    }

    @Override
    public YakTask getByCode(String taskCode) {
        YakTaskPO yakTaskPO = this.yakTaskMapper.selectByCode(taskCode, this.yakJobProperties.getAppName());
        return this.yakTaskPO2YakTask(yakTaskPO);
    }

    @Override
    public Result<Boolean> copy(String sourceTaskCode, String newTaskDesc, List<String> workerIps, String param) {
        YakTaskPO yakTaskPO = this.yakTaskMapper.selectByCode(sourceTaskCode, this.yakJobProperties.getAppName());
        if (null == yakTaskPO) {
            return Result.buildFail("task \u4e0d\u5b58\u5728");
        }
        if (CollectionUtils.isEmpty(workerIps)) {
            return Result.buildFail("workerIps \u4e3a\u7a7a");
        }
        Map<String, YakWorkerPO> yakWorkerMap = this.workerManager.mapAllWorkers();
        ArrayList<YakTask.TaskWorker> taskWorkers = new ArrayList<YakTask.TaskWorker>();
        for (String ip : workerIps) {
            YakTask.TaskWorker worker = new YakTask.TaskWorker();
            worker.setStatus(TaskWorkerStatusEnum.WAITING.getValue());
            worker.setIp(ip);
            worker.setWorkerCode(yakWorkerMap.getOrDefault(ip, new YakWorkerPO()).getWorkerCode());
            taskWorkers.add(worker);
        }
        yakTaskPO.setTaskDesc(newTaskDesc);
        yakTaskPO.setTaskCode(sourceTaskCode + "-" + IdWorker.getIdStr());
        yakTaskPO.setTaskWorkerStr(BeanUtil.convertToJson(taskWorkers));
        yakTaskPO.setParams(param);
        return Result.buildSucc(this.yakTaskMapper.insert(yakTaskPO) > 0);
    }

    @Override
    public Result<Boolean> updateWorkIpsParam(String taskCode, List<String> workerIps, String param) {
        YakTaskPO yakTaskPO = this.yakTaskMapper.selectByCode(taskCode, this.yakJobProperties.getAppName());
        if (null == yakTaskPO) {
            return Result.buildFail("task \u4e0d\u5b58\u5728");
        }
        if (!CollectionUtils.isEmpty(workerIps)) {
            Map<String, YakWorkerPO> yakWorkerMap = this.workerManager.mapAllWorkers();
            ArrayList<YakTask.TaskWorker> taskWorkers = new ArrayList<YakTask.TaskWorker>();
            for (String ip : workerIps) {
                YakTask.TaskWorker worker = new YakTask.TaskWorker();
                worker.setStatus(TaskWorkerStatusEnum.WAITING.getValue());
                worker.setIp(ip);
                worker.setWorkerCode(yakWorkerMap.getOrDefault(ip, new YakWorkerPO()).getWorkerCode());
                taskWorkers.add(worker);
            }
            yakTaskPO.setTaskWorkerStr(BeanUtil.convertToJson(taskWorkers));
        }
        yakTaskPO.setParams(param);
        return Result.buildSucc(this.yakTaskMapper.updateByCode(yakTaskPO) > 0);
    }

    private void executeInternal(YakTask yakTask, Boolean executeSubs) {
        Future<Object> jobFuture = this.jobManager.start(yakTask);
        if (jobFuture == null || !executeSubs.booleanValue()) {
            return;
        }
        while (!jobFuture.isDone()) {
            ThreadUtil.sleep(10L, TimeUnit.SECONDS);
        }
        if (!StringUtils.isEmpty((Object) yakTask.getSubTaskCodes())) {
            String[] subTaskCodeArray = yakTask.getSubTaskCodes().split(",");
            List<YakTaskPO> subTasks = this.yakTaskMapper.selectByCodes(Arrays.asList(subTaskCodeArray), this.yakJobProperties.getAppName());
            List subYakTaskList = subTasks.stream().map(yakTaskPO -> BeanUtil.convertTo(yakTaskPO, YakTask.class)).collect(Collectors.toList());
            for (YakTask subYakTask : subYakTaskList) {
                this.execute(subYakTask, executeSubs);
            }
        }
    }

    private boolean updateTaskWorker(String taskCode, String workerCode) {
        YakTaskPO yakTaskPO = this.yakTaskMapper.selectByCode(taskCode, this.yakJobProperties.getAppName());
        if (yakTaskPO == null) {
            return false;
        }
        List<YakTask.TaskWorker> taskWorkers = BeanUtil.convertToList(yakTaskPO.getTaskWorkerStr(), YakTask.TaskWorker.class);
        boolean needUpdate = false;
        if (!CollectionUtils.isEmpty(taskWorkers)) {
            for (YakTask.TaskWorker taskWorker : taskWorkers) {
                if (!Objects.equals(taskWorker.getWorkerCode(), workerCode) || !Objects.equals(taskWorker.getStatus(), TaskWorkerStatusEnum.RUNNING.getValue()))
                    continue;
                needUpdate = true;
                taskWorker.setStatus(TaskWorkerStatusEnum.WAITING.getValue());
            }
        }
        if (needUpdate) {
            yakTaskPO.setTaskWorkerStr(BeanUtil.convertToJson(taskWorkers));
            int updateResult = this.yakTaskMapper.updateTaskWorkStrByCode(yakTaskPO);
            if (updateResult <= 0) {
                return false;
            }
        }
        return true;
    }

    private YakTask yakTaskPO2YakTask(YakTaskPO yakTaskPO) {
        List<YakTask.TaskWorker> tmpTaskWorkers;
        YakTask yakTask = BeanUtil.convertTo(yakTaskPO, YakTask.class);
        List<Object> taskWorkers = Lists.newArrayList();
        if (!StringUtils.isEmpty((Object) yakTaskPO.getTaskWorkerStr()) && !CollectionUtils.isEmpty(tmpTaskWorkers = BeanUtil.convertToList(yakTaskPO.getTaskWorkerStr(), YakTask.TaskWorker.class))) {
            taskWorkers = tmpTaskWorkers;
        }
        yakTask.setTaskWorkers(taskWorkers);
        return yakTask;
    }
}

