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

import com.yak.job.LogIJobProperties;
import com.yak.job.common.CommonUtil;
import com.yak.job.common.Result;
import com.yak.job.common.domain.LogITask;
import com.yak.job.common.dto.LogITaskDTO;
import com.yak.job.common.dto.TaskPageQueryDTO;
import com.yak.job.common.enums.TaskStatusEnum;
import com.yak.job.common.enums.TaskWorkerStatusEnum;
import com.yak.job.common.po.LogITaskPO;
import com.yak.job.common.po.LogIWorkerPO;
import com.yak.job.core.WorkerSingleton;
import com.yak.job.core.consensual.Consensual;
import com.yak.job.core.consensual.ConsensualEnum;
import com.yak.job.core.consensual.ConsensualFactory;
import com.yak.job.core.job.JobManager;
import com.yak.job.core.worker.WorkerManager;
import com.yak.job.mapper.LogITaskMapper;
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
    private LogITaskMapper logITaskMapper;
    private LogIJobProperties logIJobProperties;

    public TaskManagerImpl(WorkerManager workerManager, JobManager jobManager, ConsensualFactory consensualFactory, TaskLockService taskLockService, LogITaskMapper logITaskMapper, LogIJobProperties logIJobProperties) {
        this.workerManager = workerManager;
        this.jobManager = jobManager;
        this.consensualFactory = consensualFactory;
        this.taskLockService = taskLockService;
        this.logITaskMapper = logITaskMapper;
        this.logIJobProperties = logIJobProperties;
    }

    @Override
    public Result delete(String taskCode) {
        LogITaskPO logITaskPO = this.logITaskMapper.selectByCode(taskCode, this.logIJobProperties.getAppName());
        if (logITaskPO == null) {
            return Result.buildFail("\u4efb\u52a1\u4e0d\u5b58\u5728\uff01");
        }
        return Result.buildSucc(this.logITaskMapper.deleteByCode(taskCode, this.logIJobProperties.getAppName()) > 0);
    }

    @Override
    public boolean update(LogITaskDTO logITaskDTO) {
        LogITaskPO logITaskPO = BeanUtil.convertTo(logITaskDTO, LogITaskPO.class);
        return this.logITaskMapper.updateByCode(logITaskPO) > 0;
    }

    @Override
    public List<LogITask> nextTriggers(Long interval) {
        return this.nextTriggers(System.currentTimeMillis(), interval);
    }

    @Override
    public List<LogITask> nextTriggers(Long fromTime, Long interval) {
        List<LogITask> logITaskList = this.getAllRuning();
        logITaskList = logITaskList.stream().filter(taskInfo -> {
            try {
                if (ConsensualEnum.RANDOM.name().equals(taskInfo.getConsensual())) {
                    Timestamp lastFireTime = taskInfo.getLastFireTime();
                    List<LogITask.TaskWorker> taskWorkers = taskInfo.getTaskWorkers();
                    for (LogITask.TaskWorker taskWorker : taskWorkers) {
                        if (!Objects.equals(WorkerSingleton.getInstance().getLogIWorker().getWorkerCode(), taskWorker.getWorkerCode())) continue;
                        if (Objects.equals(taskWorker.getStatus(), TaskWorkerStatusEnum.WAITING.getValue())) break;
                        logger.info("class=TaskManagerImpl||method=nextTriggers||msg=has task running! taskCode={}, workerCode={}", (Object)taskInfo.getTaskCode(), (Object)taskWorker.getWorkerCode());
                        return false;
                    }
                    CronExpression cronExpression = new CronExpression(taskInfo.getCron());
                    long nextTime = cronExpression.getNextValidTimeAfter(lastFireTime).getTime();
                    taskInfo.setNextFireTime(new Timestamp(nextTime));
                    Timestamp timestamp = new Timestamp(fromTime + interval * 1000L);
                    return timestamp.after(taskInfo.getNextFireTime());
                }
                if (ConsensualEnum.BROADCAST.name().equals(taskInfo.getConsensual())) {
                    List<LogITask.TaskWorker> taskWorkers = taskInfo.getTaskWorkers();
                    Timestamp lastFireTime = new Timestamp(0L);
                    for (LogITask.TaskWorker taskWorker : taskWorkers) {
                        if (!Objects.equals(WorkerSingleton.getInstance().getLogIWorker().getWorkerCode(), taskWorker.getWorkerCode())) continue;
                        lastFireTime = taskWorker.getLastFireTime();
                    }
                    CronExpression cronExpression = new CronExpression(taskInfo.getCron());
                    long nextTime = cronExpression.getNextValidTimeAfter(lastFireTime).getTime();
                    taskInfo.setNextFireTime(new Timestamp(nextTime));
                    Timestamp timestamp = new Timestamp(fromTime + interval * 1000L);
                    if (timestamp.after(new Timestamp(nextTime))) {
                        if (nextTime + 10000L < fromTime && fromTime < nextTime + 20000L) {
                            logger.info("class=TaskManagerImpl||method=nextTriggers||nextTime={}||fromTime={}||msg=skip broadcast duplicate trigger!", (Object)nextTime, (Object)fromTime);
                            for (LogITask.TaskWorker taskWorker : taskWorkers) {
                                if (!Objects.equals(WorkerSingleton.getInstance().getLogIWorker().getWorkerCode(), taskWorker.getWorkerCode())) continue;
                                taskWorker.setLastFireTime(new Timestamp(nextTime));
                                LogITaskPO logITaskPO = BeanUtil.convertTo(taskInfo, LogITaskPO.class);
                                logITaskPO.setTaskWorkerStr(BeanUtil.convertToJson(taskWorkers));
                                this.logITaskMapper.updateTaskWorkStrByCode(logITaskPO);
                                return false;
                            }
                        }
                        return true;
                    }
                    return false;
                }
                return false;
            } catch (Exception e) {
                logger.error("class=TaskManagerImpl||method=nextTriggers||msg=exception!", (Throwable)e);
                return false;
            }
        }).collect(Collectors.toList());
        logITaskList.sort(Comparator.comparing(LogITask::getNextFireTime));
        return logITaskList;
    }

    @Override
    public void submit(List<LogITask> logITaskList) {
        if (CollectionUtils.isEmpty(logITaskList)) {
            return;
        }
        for (LogITask logITask : logITaskList) {
            Consensual consensual = this.consensualFactory.getConsensual(logITask.getConsensual());
            if (!consensual.canClaim(logITask)) continue;
            this.execute(logITask, (Boolean)false);
        }
    }

    @Override
    public Result execute(String taskCode, Boolean executeSubs) {
        LogITaskPO logITaskPO = this.logITaskMapper.selectByCode(taskCode, this.logIJobProperties.getAppName());
        if (logITaskPO == null) {
            return Result.buildFail("\u4efb\u52a1\u4e0d\u5b58\u5728\uff01");
        }
        if (!this.taskLockService.tryAcquire(taskCode).booleanValue()) {
            return Result.buildFail("\u672a\u80fd\u83b7\u53d6\u5230\u6267\u884c\u9501\uff01");
        }
        LogITask logITask = this.logITaskPO2LogITask(logITaskPO);
        logITask.setTaskCallback(code -> this.taskLockService.tryRelease(code));
        this.execute(logITask, (Boolean)false);
        return Result.buildSucc();
    }

    @Override
    public void execute(LogITask logITask, Boolean executeSubs) {
        Timestamp lastFireTime = new Timestamp(System.currentTimeMillis());
        LogITaskPO logITaskPO = BeanUtil.convertTo(logITask, LogITaskPO.class);
        List<LogITask.TaskWorker> taskWorkers = logITask.getTaskWorkers();
        boolean copyTask = CommonUtil.isCopyTask(logITask.getTaskCode());
        boolean worked = false;
        for (LogITask.TaskWorker taskWorker : taskWorkers) {
            if (!Objects.equals(taskWorker.getWorkerCode(), WorkerSingleton.getInstance().getLogIWorker().getWorkerCode())) continue;
            taskWorker.setLastFireTime(lastFireTime);
            taskWorker.setStatus(TaskWorkerStatusEnum.RUNNING.getValue());
            worked = true;
            break;
        }
        if (!copyTask && !worked) {
            taskWorkers.add(new LogITask.TaskWorker(TaskWorkerStatusEnum.RUNNING.getValue(), new Timestamp(System.currentTimeMillis()), WorkerSingleton.getInstance().getLogIWorker().getWorkerCode(), WorkerSingleton.getInstance().getLogIWorker().getIp()));
        }
        logITaskPO.setTaskWorkerStr(BeanUtil.convertToJson(taskWorkers));
        logITaskPO.setLastFireTime(lastFireTime);
        this.logITaskMapper.updateByCode(logITaskPO);
        this.executeInternal(logITask, executeSubs);
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
        LogITaskPO logITaskPO = this.logITaskMapper.selectByCode(taskCode, this.logIJobProperties.getAppName());
        if (null == logITaskPO) {
            return Result.buildFail("task \u4e0d\u5b58\u5728");
        }
        if (TaskStatusEnum.STOP.getValue() == status && !this.jobManager.stopByTaskCode(taskCode)) {
            return Result.buildFail("stop task error");
        }
        if (TaskStatusEnum.RUNNING.getValue() == status) {
            this.execute(logITaskPO.getTaskCode(), (Boolean)false);
        }
        logITaskPO.setStatus(status);
        return Result.buildSucc(this.logITaskMapper.updateByCode(logITaskPO) > 0);
    }

    @Override
    public List<LogITask> getAllRuning() {
        List<LogITaskPO> logITaskPOList = this.logITaskMapper.selectRuningByAppName(this.logIJobProperties.getAppName());
        if (CollectionUtils.isEmpty(logITaskPOList)) {
            return new ArrayList<LogITask>();
        }
        return logITaskPOList.stream().map(p -> this.logITaskPO2LogITask((LogITaskPO)p)).collect(Collectors.toList());
    }

    @Override
    public List<LogITask> getPagineList(TaskPageQueryDTO queryDTO) {
        List<LogITaskPO> logITaskPOList = this.logITaskMapper.pagineListByCondition(this.logIJobProperties.getAppName(), queryDTO.getTaskId(), queryDTO.getTaskDesc(), queryDTO.getClassName(), queryDTO.getTaskStatus(), (queryDTO.getPage() - 1) * queryDTO.getSize(), queryDTO.getSize());
        if (CollectionUtils.isEmpty(logITaskPOList)) {
            return new ArrayList<LogITask>();
        }
        return logITaskPOList.stream().map(p -> this.logITaskPO2LogITask((LogITaskPO)p)).collect(Collectors.toList());
    }

    @Override
    public int pagineTaskConut(TaskPageQueryDTO queryDTO) {
        return this.logITaskMapper.pagineCountByCondition(this.logIJobProperties.getAppName(), queryDTO.getTaskId(), queryDTO.getTaskDesc(), queryDTO.getClassName(), queryDTO.getTaskStatus());
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
    public LogITask getByCode(String taskCode) {
        LogITaskPO logITaskPO = this.logITaskMapper.selectByCode(taskCode, this.logIJobProperties.getAppName());
        return this.logITaskPO2LogITask(logITaskPO);
    }

    @Override
    public Result<Boolean> copy(String sourceTaskCode, String newTaskDesc, List<String> workerIps, String param) {
        LogITaskPO logITaskPO = this.logITaskMapper.selectByCode(sourceTaskCode, this.logIJobProperties.getAppName());
        if (null == logITaskPO) {
            return Result.buildFail("task \u4e0d\u5b58\u5728");
        }
        if (CollectionUtils.isEmpty(workerIps)) {
            return Result.buildFail("workerIps \u4e3a\u7a7a");
        }
        Map<String, LogIWorkerPO> logIWorkerMap = this.workerManager.mapAllWorkers();
        ArrayList<LogITask.TaskWorker> taskWorkers = new ArrayList<LogITask.TaskWorker>();
        for (String ip : workerIps) {
            LogITask.TaskWorker worker = new LogITask.TaskWorker();
            worker.setStatus(TaskWorkerStatusEnum.WAITING.getValue());
            worker.setIp(ip);
            worker.setWorkerCode(logIWorkerMap.getOrDefault(ip, new LogIWorkerPO()).getWorkerCode());
            taskWorkers.add(worker);
        }
        logITaskPO.setTaskDesc(newTaskDesc);
        logITaskPO.setTaskCode(sourceTaskCode + "-" + IdWorker.getIdStr());
        logITaskPO.setTaskWorkerStr(BeanUtil.convertToJson(taskWorkers));
        logITaskPO.setParams(param);
        return Result.buildSucc(this.logITaskMapper.insert(logITaskPO) > 0);
    }

    @Override
    public Result<Boolean> updateWorkIpsParam(String taskCode, List<String> workerIps, String param) {
        LogITaskPO logITaskPO = this.logITaskMapper.selectByCode(taskCode, this.logIJobProperties.getAppName());
        if (null == logITaskPO) {
            return Result.buildFail("task \u4e0d\u5b58\u5728");
        }
        if (!CollectionUtils.isEmpty(workerIps)) {
            Map<String, LogIWorkerPO> logIWorkerMap = this.workerManager.mapAllWorkers();
            ArrayList<LogITask.TaskWorker> taskWorkers = new ArrayList<LogITask.TaskWorker>();
            for (String ip : workerIps) {
                LogITask.TaskWorker worker = new LogITask.TaskWorker();
                worker.setStatus(TaskWorkerStatusEnum.WAITING.getValue());
                worker.setIp(ip);
                worker.setWorkerCode(logIWorkerMap.getOrDefault(ip, new LogIWorkerPO()).getWorkerCode());
                taskWorkers.add(worker);
            }
            logITaskPO.setTaskWorkerStr(BeanUtil.convertToJson(taskWorkers));
        }
        logITaskPO.setParams(param);
        return Result.buildSucc(this.logITaskMapper.updateByCode(logITaskPO) > 0);
    }

    private void executeInternal(LogITask logITask, Boolean executeSubs) {
        Future<Object> jobFuture = this.jobManager.start(logITask);
        if (jobFuture == null || !executeSubs.booleanValue()) {
            return;
        }
        while (!jobFuture.isDone()) {
            ThreadUtil.sleep(10L, TimeUnit.SECONDS);
        }
        if (!StringUtils.isEmpty((Object)logITask.getSubTaskCodes())) {
            String[] subTaskCodeArray = logITask.getSubTaskCodes().split(",");
            List<LogITaskPO> subTasks = this.logITaskMapper.selectByCodes(Arrays.asList(subTaskCodeArray), this.logIJobProperties.getAppName());
            List subLogITaskList = subTasks.stream().map(logITaskPO -> BeanUtil.convertTo(logITaskPO, LogITask.class)).collect(Collectors.toList());
            for (LogITask subLogITask : subLogITaskList) {
                this.execute(subLogITask, executeSubs);
            }
        }
    }

    private boolean updateTaskWorker(String taskCode, String workerCode) {
        LogITaskPO logITaskPO = this.logITaskMapper.selectByCode(taskCode, this.logIJobProperties.getAppName());
        if (logITaskPO == null) {
            return false;
        }
        List<LogITask.TaskWorker> taskWorkers = BeanUtil.convertToList(logITaskPO.getTaskWorkerStr(), LogITask.TaskWorker.class);
        boolean needUpdate = false;
        if (!CollectionUtils.isEmpty(taskWorkers)) {
            for (LogITask.TaskWorker taskWorker : taskWorkers) {
                if (!Objects.equals(taskWorker.getWorkerCode(), workerCode) || !Objects.equals(taskWorker.getStatus(), TaskWorkerStatusEnum.RUNNING.getValue())) continue;
                needUpdate = true;
                taskWorker.setStatus(TaskWorkerStatusEnum.WAITING.getValue());
            }
        }
        if (needUpdate) {
            logITaskPO.setTaskWorkerStr(BeanUtil.convertToJson(taskWorkers));
            int updateResult = this.logITaskMapper.updateTaskWorkStrByCode(logITaskPO);
            if (updateResult <= 0) {
                return false;
            }
        }
        return true;
    }

    private LogITask logITaskPO2LogITask(LogITaskPO logITaskPO) {
        List<LogITask.TaskWorker> tmpTaskWorkers;
        LogITask logITask = BeanUtil.convertTo(logITaskPO, LogITask.class);
        List<Object> taskWorkers = Lists.newArrayList();
        if (!StringUtils.isEmpty((Object)logITaskPO.getTaskWorkerStr()) && !CollectionUtils.isEmpty(tmpTaskWorkers = BeanUtil.convertToList(logITaskPO.getTaskWorkerStr(), LogITask.TaskWorker.class))) {
            taskWorkers = tmpTaskWorkers;
        }
        logITask.setTaskWorkers(taskWorkers);
        return logITask;
    }
}

