/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  com.google.common.cache.Cache
 *  com.google.common.cache.CacheBuilder
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.CollectionUtils
 */
package com.didiglobal.logi.job.core.job.impl;

import com.didiglobal.logi.job.LogIJobProperties;
import com.didiglobal.logi.job.common.TaskResult;
import com.didiglobal.logi.job.common.domain.LogIJob;
import com.didiglobal.logi.job.common.domain.LogITask;
import com.didiglobal.logi.job.common.enums.JobStatusEnum;
import com.didiglobal.logi.job.common.enums.TaskWorkerStatusEnum;
import com.didiglobal.logi.job.common.po.LogIJobLogPO;
import com.didiglobal.logi.job.common.po.LogIJobPO;
import com.didiglobal.logi.job.common.po.LogITaskLockPO;
import com.didiglobal.logi.job.common.po.LogITaskPO;
import com.didiglobal.logi.job.common.po.LogIWorkerPO;
import com.didiglobal.logi.job.core.WorkerSingleton;
import com.didiglobal.logi.job.core.job.JobContext;
import com.didiglobal.logi.job.core.job.JobExecutor;
import com.didiglobal.logi.job.core.job.JobFactory;
import com.didiglobal.logi.job.core.job.JobManager;
import com.didiglobal.logi.job.core.task.TaskLockService;
import com.didiglobal.logi.job.mapper.LogIJobLogMapper;
import com.didiglobal.logi.job.mapper.LogIJobMapper;
import com.didiglobal.logi.job.mapper.LogITaskLockMapper;
import com.didiglobal.logi.job.mapper.LogITaskMapper;
import com.didiglobal.logi.job.mapper.LogIWorkerMapper;
import com.didiglobal.logi.job.utils.BeanUtil;
import com.didiglobal.logi.job.utils.ThreadUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class JobManagerImpl
implements JobManager {
    private static final Logger logger = LoggerFactory.getLogger(JobManagerImpl.class);
    private static final int TRY_MAX_TIMES = 3;
    private static final int STOP_SLEEP_SECONDS = 3;
    private static final Long CHECK_BEFORE_INTERVAL = 60L;
    private static final Long RENEW_INTERVAL = 60L;
    private static final Long ONE_HOUR = 3600L;
    private JobFactory jobFactory;
    private LogIJobMapper logIJobMapper;
    private LogIJobLogMapper logIJobLogMapper;
    private LogITaskMapper logITaskMapper;
    private LogIWorkerMapper logIWorkerMapper;
    private JobExecutor jobExecutor;
    private TaskLockService taskLockService;
    private LogITaskLockMapper logITaskLockMapper;
    private LogIJobProperties logIJobProperties;
    private ConcurrentHashMap<LogIJob, Future> jobFutureMap = new ConcurrentHashMap();
    private final Cache<String, String> execuedJob = CacheBuilder.newBuilder().expireAfterWrite(5L, TimeUnit.MINUTES).maximumSize(1000L).build();

    @Autowired
    public JobManagerImpl(JobFactory jobFactory, LogIJobMapper logIJobMapper, LogIJobLogMapper logIJobLogMapper, LogITaskMapper logITaskMapper, LogIWorkerMapper logIWorkerMapper, JobExecutor jobExecutor, TaskLockService taskLockService, LogITaskLockMapper logITaskLockMapper, LogIJobProperties logIJobProperties) {
        this.jobFactory = jobFactory;
        this.logIJobMapper = logIJobMapper;
        this.logIJobLogMapper = logIJobLogMapper;
        this.logITaskMapper = logITaskMapper;
        this.logIWorkerMapper = logIWorkerMapper;
        this.jobExecutor = jobExecutor;
        this.taskLockService = taskLockService;
        this.logITaskLockMapper = logITaskLockMapper;
        this.logIJobProperties = logIJobProperties;
        this.initialize();
    }

    private void initialize() {
        new Thread((Runnable)new JobFutureHandler(), "JobFutureHandler Thread").start();
        new Thread((Runnable)new LockRenewHandler(), "LockRenewHandler Thread").start();
        new Thread((Runnable)new LogCleanHandler(this.logIJobProperties.getLogExpire()), "LogCleanHandler Thread").start();
    }

    @Override
    public Future<Object> start(LogITask logITask) {
        LogIJob logIJob = this.jobFactory.newJob(logITask);
        if (null == logIJob) {
            logger.error("class=JobHandler||method=start||classname={}||msg=logIJob is null", (Object)logITask.getClassName());
            return null;
        }
        LogIJobPO job = logIJob.getAuvJob();
        this.logIJobMapper.insert(job);
        Future<Object> jobFuture = this.jobExecutor.submit(new JobHandler(logIJob, logITask));
        this.jobFutureMap.put(logIJob, jobFuture);
        LogIJobLogPO logIJobLogPO = logIJob.getAuvJobLog();
        this.logIJobLogMapper.insert(logIJobLogPO);
        return jobFuture;
    }

    @Override
    public Integer runningJobSize() {
        return this.jobFutureMap.size();
    }

    @Override
    public boolean stopByTaskCode(String taskCode) {
        for (Map.Entry<LogIJob, Future> jobFuture : this.jobFutureMap.entrySet()) {
            LogIJob logIJob = jobFuture.getKey();
            if (!Objects.equals(taskCode, logIJob.getTaskCode())) continue;
            return this.stopJob(logIJob, jobFuture.getValue());
        }
        return true;
    }

    @Override
    public boolean stopByJobCode(String jobCode) {
        for (Map.Entry<LogIJob, Future> jobFuture : this.jobFutureMap.entrySet()) {
            LogIJob logIJob = jobFuture.getKey();
            if (!Objects.equals(jobCode, logIJob.getJobCode())) continue;
            return this.stopJob(logIJob, jobFuture.getValue());
        }
        return true;
    }

    @Override
    public int stopAll() {
        AtomicInteger succeedNum = new AtomicInteger();
        for (Map.Entry<LogIJob, Future> jobFuture : this.jobFutureMap.entrySet()) {
            LogIJob logIJob = jobFuture.getKey();
            if (!this.stopJob(logIJob, jobFuture.getValue())) continue;
            succeedNum.addAndGet(1);
        }
        return succeedNum.get();
    }

    @Override
    public List<LogIJob> getJobs() {
        List<LogIJobPO> logIJobPOS = this.logIJobMapper.selectByAppName(this.logIJobProperties.getAppName());
        if (CollectionUtils.isEmpty(logIJobPOS)) {
            return null;
        }
        List<LogIJob> logIJobDTOS = logIJobPOS.stream().map(logIJobPO -> BeanUtil.convertTo(logIJobPO, LogIJob.class)).collect(Collectors.toList());
        return logIJobDTOS;
    }

    @Transactional(rollbackFor={Exception.class})
    public void reorganizeFinishedJob(LogIJob logIJob) {
        this.jobFutureMap.remove(logIJob);
        this.execuedJob.put((Object)logIJob.getTaskCode(), (Object)logIJob.getTaskCode());
        if (JobStatusEnum.CANCELED.getValue().equals(logIJob.getStatus())) {
            logIJob.setResult(new TaskResult(-1, "task job be canceld!"));
            logIJob.setError("task job be canceld!");
            LogIJobLogPO logIJobLogPO = logIJob.getAuvJobLog();
            this.logIJobLogMapper.updateByCode(logIJobLogPO);
        }
        this.logIJobMapper.deleteByCode(logIJob.getJobCode());
        LogITaskPO logITaskPO = this.logITaskMapper.selectByCode(logIJob.getTaskCode(), this.logIJobProperties.getAppName());
        List<LogITask.TaskWorker> taskWorkers = BeanUtil.convertToList(logITaskPO.getTaskWorkerStr(), LogITask.TaskWorker.class);
        long currentTime = System.currentTimeMillis();
        if (!CollectionUtils.isEmpty(taskWorkers)) {
            taskWorkers.sort((o1, o2) -> o1.getLastFireTime().after(o2.getLastFireTime()) ? 1 : -1);
            Iterator<LogITask.TaskWorker> iter = taskWorkers.iterator();
            while (iter.hasNext()) {
                LogITask.TaskWorker taskWorker = iter.next();
                if (TaskWorkerStatusEnum.WAITING.getValue().equals(taskWorker.getStatus()) && taskWorker.getLastFireTime().getTime() + 12L * ONE_HOUR * 1000L < currentTime) {
                    iter.remove();
                }
                if (!Objects.equals(taskWorker.getWorkerCode(), WorkerSingleton.getInstance().getLogIWorker().getWorkerCode())) continue;
                taskWorker.setStatus(TaskWorkerStatusEnum.WAITING.getValue());
            }
        }
        logITaskPO.setTaskWorkerStr(BeanUtil.convertToJson(taskWorkers));
        this.logITaskMapper.updateTaskWorkStrByCode(logITaskPO);
    }

    private boolean stopJob(LogIJob logIJob, Future future) {
        for (int tryTime = 0; tryTime < 3; ++tryTime) {
            if (future.isDone()) {
                logIJob.setStatus(JobStatusEnum.CANCELED.getValue());
                if (logIJob.getTaskCallback() != null) {
                    logIJob.getTaskCallback().callback(logIJob.getTaskCode());
                }
                this.reorganizeFinishedJob(logIJob);
                return true;
            }
            future.cancel(true);
            ThreadUtil.sleep(3L, TimeUnit.SECONDS);
        }
        return false;
    }

    private String printStackTraceAsString(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String error = stringWriter.toString();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.toString() + "  " + error;
    }

    class LogCleanHandler
    implements Runnable {
        private static final long JOB_LOG_DEL_INTERVAL = 3600L;
        private Integer logExpire = 7;

        public LogCleanHandler(Integer logExpire) {
            if (logExpire != null) {
                this.logExpire = logExpire;
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    while (true) {
                        ThreadUtil.sleep(3600L, TimeUnit.SECONDS);
                        logger.info("class=LogCleanHandler||method=run||msg=clean auv_job_log regular time {}", (Object)3600L);
                        String appName = JobManagerImpl.this.logIJobProperties.getAppName();
                        Timestamp deleteTime = new Timestamp(System.currentTimeMillis() - (long)(this.logExpire * 24 * 3600 * 1000));
                        int deleteRowTotal = JobManagerImpl.this.logIJobLogMapper.selectCountByAppNameAndCreateTime(appName, deleteTime);
                        int deleteRowPerTimes = deleteRowTotal / 60;
                        int deleteRowReal = 0;
                        for (int i = 0; i < 60; ++i) {
                            int count = JobManagerImpl.this.logIJobLogMapper.deleteByCreateTime(deleteTime, appName, deleteRowPerTimes);
                            deleteRowReal += count;
                        }
                        logger.info("class=LogCleanHandler||method=run||msg=clean log deleteRowTotal={}, deleteRowReal={}", (Object)deleteRowTotal, (Object)deleteRowReal);
                    }
                } catch (Exception e) {
                    logger.error("class=LogCleanHandler||method=run||msg=exception", (Throwable)e);
                    continue;
                }
                break;
            }
        }
    }

    class LockRenewHandler
    implements Runnable {
        private static final long JOB_INTERVAL = 10L;

        @Override
        public void run() {
            while (true) {
                try {
                    logger.info("class=LockRenewHandler||method=run||msg=check need renew lock at regular time {}", (Object)10L);
                    List<LogITaskLockPO> logITaskLockPOS = JobManagerImpl.this.logITaskLockMapper.selectByWorkerCode(WorkerSingleton.getInstance().getLogIWorker().getWorkerCode(), JobManagerImpl.this.logIJobProperties.getAppName());
                    if (!CollectionUtils.isEmpty(logITaskLockPOS)) {
                        long current = System.currentTimeMillis() / 1000L;
                        for (LogITaskLockPO logITaskLockPO : logITaskLockPOS) {
                            LogITaskPO logITaskPO;
                            long exTime = logITaskLockPO.getCreateTime().getTime() / 1000L + logITaskLockPO.getExpireTime();
                            if (null != JobManagerImpl.this.execuedJob.getIfPresent((Object)logITaskLockPO.getTaskCode())) {
                                if (current >= exTime || current <= exTime - CHECK_BEFORE_INTERVAL) continue;
                                logger.info("class=TaskLockServiceImpl||method=run||msg=update lock expireTime id={}, expireTime={}", (Object)logITaskLockPO.getId(), (Object)logITaskLockPO.getExpireTime());
                                JobManagerImpl.this.logITaskLockMapper.update(logITaskLockPO.getId(), logITaskLockPO.getExpireTime() + RENEW_INTERVAL);
                                continue;
                            }
                            if (current > exTime) {
                                logger.info("class=TaskLockServiceImpl||method=run||msg=lock clean lockInfo={}", (Object)BeanUtil.convertToJson(logITaskLockPO));
                                JobManagerImpl.this.logITaskLockMapper.deleteById(logITaskLockPO.getId());
                            }
                            if ((logITaskPO = JobManagerImpl.this.logITaskMapper.selectByCode(logITaskLockPO.getTaskCode(), JobManagerImpl.this.logIJobProperties.getAppName())) == null) continue;
                            List<LogITask.TaskWorker> taskWorkers = BeanUtil.convertToList(logITaskPO.getTaskWorkerStr(), LogITask.TaskWorker.class);
                            if (!CollectionUtils.isEmpty(taskWorkers)) {
                                for (LogITask.TaskWorker taskWorker : taskWorkers) {
                                    if (!Objects.equals(taskWorker.getWorkerCode(), WorkerSingleton.getInstance().getLogIWorker().getWorkerCode())) continue;
                                    taskWorker.setStatus(TaskWorkerStatusEnum.WAITING.getValue());
                                }
                            }
                            logITaskPO.setTaskWorkerStr(BeanUtil.convertToJson(taskWorkers));
                            logger.info("class=TaskLockServiceImpl||method=run||msg=update task workers status taskInfo={}", (Object)BeanUtil.convertToJson(logITaskPO));
                            JobManagerImpl.this.logITaskMapper.updateTaskWorkStrByCode(logITaskPO);
                        }
                    }
                } catch (Exception e) {
                    logger.error("class=LockRenewHandler||method=run||msg=exception!", (Throwable)e);
                }
                ThreadUtil.sleep(10L, TimeUnit.SECONDS);
            }
        }
    }

    class JobFutureHandler
    implements Runnable {
        private static final long JOB_FUTURE_CLEAN_INTERVAL = 10L;

        @Override
        public void run() {
            while (true) {
                try {
                    while (true) {
                        ThreadUtil.sleep(10L, TimeUnit.SECONDS);
                        logger.info("class=JobFutureHandler||method=run||msg=check running jobs at regular time {}", (Object)10L);
                        JobManagerImpl.this.jobFutureMap.forEach((jobInfo, future) -> {
                            if (future.isDone()) {
                                JobManagerImpl.this.reorganizeFinishedJob((LogIJob)jobInfo);
                                return;
                            }
                            Long timeout = jobInfo.getTimeout();
                            if (timeout <= 0L) {
                                return;
                            }
                            Long startTime = jobInfo.getStartTime().getTime();
                            Long now = System.currentTimeMillis();
                            Long between = (now - startTime) / 1000L;
                            if (between > timeout && !future.isDone()) {
                                jobInfo.setStatus(JobStatusEnum.CANCELED.getValue());
                                future.cancel(true);
                            }
                        });
                    }
                } catch (Exception e) {
                    logger.error("class=JobFutureHandler||method=run||msg=exception!", (Throwable)e);
                    continue;
                }
                break;
            }
        }
    }

    class JobHandler
    implements Callable {
        private LogIJob logIJob;
        private LogITask logITask;

        public JobHandler(LogIJob logIJob, LogITask logITask) {
            this.logIJob = logIJob;
            this.logITask = logITask;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public Object call() {
            TaskResult object = null;
            logger.info("class=JobHandler||method=call||msg=start job {} with classname {}", (Object)this.logIJob.getJobCode(), (Object)this.logIJob.getClassName());
            try {
                this.logIJob.setStartTime(new Timestamp(System.currentTimeMillis()));
                this.logIJob.setStatus(JobStatusEnum.SUCCEED.getValue());
                this.logIJob.setResult(new TaskResult(0, "task job is running!"));
                this.logIJob.setError("");
                LogIJobLogPO logIJobLogPO = this.logIJob.getAuvJobLog();
                JobManagerImpl.this.logIJobLogMapper.updateByCode(logIJobLogPO);
                List<LogIWorkerPO> logIWorkerPOS = JobManagerImpl.this.logIWorkerMapper.selectByAppName(JobManagerImpl.this.logIJobProperties.getAppName());
                ArrayList<String> workCodes = new ArrayList<String>();
                if (CollectionUtils.isEmpty(logIWorkerPOS)) {
                    workCodes.add(this.logIJob.getWorkerIp());
                } else {
                    workCodes.addAll(logIWorkerPOS.stream().map(LogIWorkerPO::getWorkerCode).collect(Collectors.toList()));
                }
                JobContext jobContext = new JobContext(this.logITask.getParams(), workCodes, this.logIJob.getWorkerCode());
                object = this.logIJob.getJob().execute(jobContext);
                this.logIJob.setResult(object);
                this.logIJob.setEndTime(new Timestamp(System.currentTimeMillis()));
            } catch (InterruptedException e) {
                this.logIJob.setStatus(JobStatusEnum.CANCELED.getValue());
                this.logIJob.setResult(new TaskResult(-1, "task job be canceld!"));
                String error = JobManagerImpl.this.printStackTraceAsString(e);
                this.logIJob.setError(JobManagerImpl.this.printStackTraceAsString(e));
                logger.error("class=JobHandler||method=call||classname={}||msg={}", (Object)this.logIJob.getClassName(), (Object)error);
            } catch (Exception e) {
                this.logIJob.setStatus(JobStatusEnum.FAILED.getValue());
                this.logIJob.setResult(new TaskResult(-1, "task job has exception when running!" + e));
                String error = JobManagerImpl.this.printStackTraceAsString(e);
                this.logIJob.setError(JobManagerImpl.this.printStackTraceAsString(e));
                logger.error("class=JobHandler||method=call||classname=||msg={}", (Object)this.logIJob.getClassName(), (Object)error);
            } finally {
                LogIJobLogPO logIJobLogPO = this.logIJob.getAuvJobLog();
                JobManagerImpl.this.logIJobLogMapper.updateByCode(logIJobLogPO);
                if (this.logIJob.getTaskCallback() != null) {
                    this.logIJob.getTaskCallback().callback(this.logIJob.getTaskCode());
                }
            }
            return object;
        }
    }
}

