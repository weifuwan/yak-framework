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
package com.yak.job.core.job.impl;

import com.yak.job.YakJobProperties;
import com.yak.job.common.TaskResult;
import com.yak.job.common.domain.YakJob;
import com.yak.job.common.domain.YakTask;
import com.yak.job.common.enums.JobStatusEnum;
import com.yak.job.common.enums.TaskWorkerStatusEnum;
import com.yak.job.common.po.YakJobLogPO;
import com.yak.job.common.po.YakJobPO;
import com.yak.job.common.po.YakTaskLockPO;
import com.yak.job.common.po.YakTaskPO;
import com.yak.job.common.po.YakWorkerPO;
import com.yak.job.core.WorkerSingleton;
import com.yak.job.core.job.JobContext;
import com.yak.job.core.job.JobExecutor;
import com.yak.job.core.job.JobFactory;
import com.yak.job.core.job.JobManager;
import com.yak.job.core.task.TaskLockService;
import com.yak.job.mapper.YakJobLogMapper;
import com.yak.job.mapper.YakJobMapper;
import com.yak.job.mapper.YakTaskLockMapper;
import com.yak.job.mapper.YakTaskMapper;
import com.yak.job.mapper.YakWorkerMapper;
import com.yak.job.utils.BeanUtil;
import com.yak.job.utils.ThreadUtil;
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
    private YakJobMapper yakJobMapper;
    private YakJobLogMapper yakJobLogMapper;
    private YakTaskMapper yakTaskMapper;
    private YakWorkerMapper yakWorkerMapper;
    private JobExecutor jobExecutor;
    private TaskLockService taskLockService;
    private YakTaskLockMapper yakTaskLockMapper;
    private YakJobProperties yakJobProperties;
    private ConcurrentHashMap<YakJob, Future> jobFutureMap = new ConcurrentHashMap();
    private final Cache<String, String> execuedJob = CacheBuilder.newBuilder().expireAfterWrite(5L, TimeUnit.MINUTES).maximumSize(1000L).build();

    @Autowired
    public JobManagerImpl(JobFactory jobFactory, YakJobMapper yakJobMapper, YakJobLogMapper yakJobLogMapper, YakTaskMapper yakTaskMapper, YakWorkerMapper yakWorkerMapper, JobExecutor jobExecutor, TaskLockService taskLockService, YakTaskLockMapper yakTaskLockMapper, YakJobProperties yakJobProperties) {
        this.jobFactory = jobFactory;
        this.yakJobMapper = yakJobMapper;
        this.yakJobLogMapper = yakJobLogMapper;
        this.yakTaskMapper = yakTaskMapper;
        this.yakWorkerMapper = yakWorkerMapper;
        this.jobExecutor = jobExecutor;
        this.taskLockService = taskLockService;
        this.yakTaskLockMapper = yakTaskLockMapper;
        this.yakJobProperties = yakJobProperties;
        this.initialize();
    }

    private void initialize() {
        new Thread((Runnable)new JobFutureHandler(), "JobFutureHandler Thread").start();
        new Thread((Runnable)new LockRenewHandler(), "LockRenewHandler Thread").start();
        new Thread((Runnable)new LogCleanHandler(this.yakJobProperties.getLogExpire()), "LogCleanHandler Thread").start();
    }

    @Override
    public Future<Object> start(YakTask yakTask) {
        YakJob yakJob = this.jobFactory.newJob(yakTask);
        if (null == yakJob) {
            logger.error("class=JobHandler||method=start||classname={}||msg=yakJob is null", (Object)yakTask.getClassName());
            return null;
        }
        YakJobPO job = yakJob.getAuvJob();
        this.yakJobMapper.insert(job);
        Future<Object> jobFuture = this.jobExecutor.submit(new JobHandler(yakJob, yakTask));
        this.jobFutureMap.put(yakJob, jobFuture);
        YakJobLogPO yakJobLogPO = yakJob.getAuvJobLog();
        this.yakJobLogMapper.insert(yakJobLogPO);
        return jobFuture;
    }

    @Override
    public Integer runningJobSize() {
        return this.jobFutureMap.size();
    }

    @Override
    public boolean stopByTaskCode(String taskCode) {
        for (Map.Entry<YakJob, Future> jobFuture : this.jobFutureMap.entrySet()) {
            YakJob yakJob = jobFuture.getKey();
            if (!Objects.equals(taskCode, yakJob.getTaskCode())) continue;
            return this.stopJob(yakJob, jobFuture.getValue());
        }
        return true;
    }

    @Override
    public boolean stopByJobCode(String jobCode) {
        for (Map.Entry<YakJob, Future> jobFuture : this.jobFutureMap.entrySet()) {
            YakJob yakJob = jobFuture.getKey();
            if (!Objects.equals(jobCode, yakJob.getJobCode())) continue;
            return this.stopJob(yakJob, jobFuture.getValue());
        }
        return true;
    }

    @Override
    public int stopAll() {
        AtomicInteger succeedNum = new AtomicInteger();
        for (Map.Entry<YakJob, Future> jobFuture : this.jobFutureMap.entrySet()) {
            YakJob yakJob = jobFuture.getKey();
            if (!this.stopJob(yakJob, jobFuture.getValue())) continue;
            succeedNum.addAndGet(1);
        }
        return succeedNum.get();
    }

    @Override
    public List<YakJob> getJobs() {
        List<YakJobPO> yakJobPOS = this.yakJobMapper.selectByAppName(this.yakJobProperties.getAppName());
        if (CollectionUtils.isEmpty(yakJobPOS)) {
            return null;
        }
        List<YakJob> yakJobDTOS = yakJobPOS.stream().map(yakJobPO -> BeanUtil.convertTo(yakJobPO, YakJob.class)).collect(Collectors.toList());
        return yakJobDTOS;
    }

    @Transactional(rollbackFor={Exception.class})
    public void reorganizeFinishedJob(YakJob yakJob) {
        this.jobFutureMap.remove(yakJob);
        this.execuedJob.put((Object)yakJob.getTaskCode(), (Object)yakJob.getTaskCode());
        if (JobStatusEnum.CANCELED.getValue().equals(yakJob.getStatus())) {
            yakJob.setResult(new TaskResult(-1, "task job be canceld!"));
            yakJob.setError("task job be canceld!");
            YakJobLogPO yakJobLogPO = yakJob.getAuvJobLog();
            this.yakJobLogMapper.updateByCode(yakJobLogPO);
        }
        this.yakJobMapper.deleteByCode(yakJob.getJobCode());
        YakTaskPO yakTaskPO = this.yakTaskMapper.selectByCode(yakJob.getTaskCode(), this.yakJobProperties.getAppName());
        List<YakTask.TaskWorker> taskWorkers = BeanUtil.convertToList(yakTaskPO.getTaskWorkerStr(), YakTask.TaskWorker.class);
        long currentTime = System.currentTimeMillis();
        if (!CollectionUtils.isEmpty(taskWorkers)) {
            taskWorkers.sort((o1, o2) -> o1.getLastFireTime().after(o2.getLastFireTime()) ? 1 : -1);
            Iterator<YakTask.TaskWorker> iter = taskWorkers.iterator();
            while (iter.hasNext()) {
                YakTask.TaskWorker taskWorker = iter.next();
                if (TaskWorkerStatusEnum.WAITING.getValue().equals(taskWorker.getStatus()) && taskWorker.getLastFireTime().getTime() + 12L * ONE_HOUR * 1000L < currentTime) {
                    iter.remove();
                }
                if (!Objects.equals(taskWorker.getWorkerCode(), WorkerSingleton.getInstance().getYakWorker().getWorkerCode())) continue;
                taskWorker.setStatus(TaskWorkerStatusEnum.WAITING.getValue());
            }
        }
        yakTaskPO.setTaskWorkerStr(BeanUtil.convertToJson(taskWorkers));
        this.yakTaskMapper.updateTaskWorkStrByCode(yakTaskPO);
    }

    private boolean stopJob(YakJob yakJob, Future future) {
        for (int tryTime = 0; tryTime < 3; ++tryTime) {
            if (future.isDone()) {
                yakJob.setStatus(JobStatusEnum.CANCELED.getValue());
                if (yakJob.getTaskCallback() != null) {
                    yakJob.getTaskCallback().callback(yakJob.getTaskCode());
                }
                this.reorganizeFinishedJob(yakJob);
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
                        String appName = JobManagerImpl.this.yakJobProperties.getAppName();
                        Timestamp deleteTime = new Timestamp(System.currentTimeMillis() - (long)(this.logExpire * 24 * 3600 * 1000));
                        int deleteRowTotal = JobManagerImpl.this.yakJobLogMapper.selectCountByAppNameAndCreateTime(appName, deleteTime);
                        int deleteRowPerTimes = deleteRowTotal / 60;
                        int deleteRowReal = 0;
                        for (int i = 0; i < 60; ++i) {
                            int count = JobManagerImpl.this.yakJobLogMapper.deleteByCreateTime(deleteTime, appName, deleteRowPerTimes);
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
                    List<YakTaskLockPO> yakTaskLockPOS = JobManagerImpl.this.yakTaskLockMapper.selectByWorkerCode(WorkerSingleton.getInstance().getYakWorker().getWorkerCode(), JobManagerImpl.this.yakJobProperties.getAppName());
                    if (!CollectionUtils.isEmpty(yakTaskLockPOS)) {
                        long current = System.currentTimeMillis() / 1000L;
                        for (YakTaskLockPO yakTaskLockPO : yakTaskLockPOS) {
                            YakTaskPO yakTaskPO;
                            long exTime = yakTaskLockPO.getCreateTime().getTime() / 1000L + yakTaskLockPO.getExpireTime();
                            if (null != JobManagerImpl.this.execuedJob.getIfPresent((Object)yakTaskLockPO.getTaskCode())) {
                                if (current >= exTime || current <= exTime - CHECK_BEFORE_INTERVAL) continue;
                                logger.info("class=TaskLockServiceImpl||method=run||msg=update lock expireTime id={}, expireTime={}", (Object)yakTaskLockPO.getId(), (Object)yakTaskLockPO.getExpireTime());
                                JobManagerImpl.this.yakTaskLockMapper.update(yakTaskLockPO.getId(), yakTaskLockPO.getExpireTime() + RENEW_INTERVAL);
                                continue;
                            }
                            if (current > exTime) {
                                logger.info("class=TaskLockServiceImpl||method=run||msg=lock clean lockInfo={}", (Object)BeanUtil.convertToJson(yakTaskLockPO));
                                JobManagerImpl.this.yakTaskLockMapper.deleteById(yakTaskLockPO.getId());
                            }
                            if ((yakTaskPO = JobManagerImpl.this.yakTaskMapper.selectByCode(yakTaskLockPO.getTaskCode(), JobManagerImpl.this.yakJobProperties.getAppName())) == null) continue;
                            List<YakTask.TaskWorker> taskWorkers = BeanUtil.convertToList(yakTaskPO.getTaskWorkerStr(), YakTask.TaskWorker.class);
                            if (!CollectionUtils.isEmpty(taskWorkers)) {
                                for (YakTask.TaskWorker taskWorker : taskWorkers) {
                                    if (!Objects.equals(taskWorker.getWorkerCode(), WorkerSingleton.getInstance().getYakWorker().getWorkerCode())) continue;
                                    taskWorker.setStatus(TaskWorkerStatusEnum.WAITING.getValue());
                                }
                            }
                            yakTaskPO.setTaskWorkerStr(BeanUtil.convertToJson(taskWorkers));
                            logger.info("class=TaskLockServiceImpl||method=run||msg=update task workers status taskInfo={}", (Object)BeanUtil.convertToJson(yakTaskPO));
                            JobManagerImpl.this.yakTaskMapper.updateTaskWorkStrByCode(yakTaskPO);
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
                                JobManagerImpl.this.reorganizeFinishedJob((YakJob)jobInfo);
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
        private YakJob yakJob;
        private YakTask yakTask;

        public JobHandler(YakJob yakJob, YakTask yakTask) {
            this.yakJob = yakJob;
            this.yakTask = yakTask;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public Object call() {
            TaskResult object = null;
            logger.info("class=JobHandler||method=call||msg=start job {} with classname {}", (Object)this.yakJob.getJobCode(), (Object)this.yakJob.getClassName());
            try {
                this.yakJob.setStartTime(new Timestamp(System.currentTimeMillis()));
                this.yakJob.setStatus(JobStatusEnum.SUCCEED.getValue());
                this.yakJob.setResult(new TaskResult(0, "task job is running!"));
                this.yakJob.setError("");
                YakJobLogPO yakJobLogPO = this.yakJob.getAuvJobLog();
                JobManagerImpl.this.yakJobLogMapper.updateByCode(yakJobLogPO);
                List<YakWorkerPO> yakWorkerPOS = JobManagerImpl.this.yakWorkerMapper.selectByAppName(JobManagerImpl.this.yakJobProperties.getAppName());
                ArrayList<String> workCodes = new ArrayList<String>();
                if (CollectionUtils.isEmpty(yakWorkerPOS)) {
                    workCodes.add(this.yakJob.getWorkerIp());
                } else {
                    workCodes.addAll(yakWorkerPOS.stream().map(YakWorkerPO::getWorkerCode).collect(Collectors.toList()));
                }
                JobContext jobContext = new JobContext(this.yakTask.getParams(), workCodes, this.yakJob.getWorkerCode());
                object = this.yakJob.getJob().execute(jobContext);
                this.yakJob.setResult(object);
                this.yakJob.setEndTime(new Timestamp(System.currentTimeMillis()));
            } catch (InterruptedException e) {
                this.yakJob.setStatus(JobStatusEnum.CANCELED.getValue());
                this.yakJob.setResult(new TaskResult(-1, "task job be canceld!"));
                String error = JobManagerImpl.this.printStackTraceAsString(e);
                this.yakJob.setError(JobManagerImpl.this.printStackTraceAsString(e));
                logger.error("class=JobHandler||method=call||classname={}||msg={}", (Object)this.yakJob.getClassName(), (Object)error);
            } catch (Exception e) {
                this.yakJob.setStatus(JobStatusEnum.FAILED.getValue());
                this.yakJob.setResult(new TaskResult(-1, "task job has exception when running!" + e));
                String error = JobManagerImpl.this.printStackTraceAsString(e);
                this.yakJob.setError(JobManagerImpl.this.printStackTraceAsString(e));
                logger.error("class=JobHandler||method=call||classname=||msg={}", (Object)this.yakJob.getClassName(), (Object)error);
            } finally {
                YakJobLogPO yakJobLogPO = this.yakJob.getAuvJobLog();
                JobManagerImpl.this.yakJobLogMapper.updateByCode(yakJobLogPO);
                if (this.yakJob.getTaskCallback() != null) {
                    this.yakJob.getTaskCallback().callback(this.yakJob.getTaskCode());
                }
            }
            return object;
        }
    }
}

