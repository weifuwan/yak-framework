/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  javax.annotation.PostConstruct
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.BeansException
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.config.BeanPostProcessor
 *  org.springframework.stereotype.Component
 */
package com.yak.job;

import com.yak.job.annotation.Task;
import com.yak.job.common.enums.TaskStatusEnum;
import com.yak.job.common.po.LogITaskPO;
import com.yak.job.core.job.Job;
import com.yak.job.core.job.JobFactory;
import com.yak.job.mapper.LogITaskMapper;
import com.yak.job.utils.CronExpression;
import com.yak.job.utils.IdWorker;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class TaskBeanPostProcessor
implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(TaskBeanPostProcessor.class);
    private static Map<String, LogITaskPO> taskMap = new HashMap<String, LogITaskPO>();
    @Autowired
    private LogITaskMapper logITaskMapper;
    @Autowired
    private JobFactory jobFactory;
    @Autowired
    private LogIJobProperties logIJobProperties;

    @PostConstruct
    public void init() {
        logger.info("class=TaskBeanPostProcessor||method=init");
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        try {
            if (!this.logIJobProperties.getEnable().booleanValue()) {
                return bean;
            }
            Class<?> beanClass = bean.getClass();
            if (!(bean instanceof Job)) {
                return bean;
            }
            this.jobFactory.addJob(beanClass.getCanonicalName(), (Job)bean);
            Task taskAnnotation = beanClass.getAnnotation(Task.class);
            if (taskAnnotation == null || !taskAnnotation.autoRegister()) {
                return bean;
            }
            if (!this.check(taskAnnotation)) {
                logger.error("class=TaskBeanPostProcessor||method=blacklist||url=||msg=invalid schedule {}", (Object)taskAnnotation.toString());
            }
            if (!this.contains(beanClass.getCanonicalName())) {
                LogITaskPO task = this.getNewLogTask(beanClass, taskAnnotation);
                task.setTaskCode(IdWorker.getIdStr());
                task.setStatus(TaskStatusEnum.RUNNING.getValue());
                this.logITaskMapper.insert(task);
            } else {
                LogITaskPO task = taskMap.get(beanClass.getCanonicalName());
                task = this.updateLogTask(task, beanClass, taskAnnotation);
                this.logITaskMapper.updateByCode(task);
            }
        } catch (Exception e) {
            logger.error("class=TaskBeanPostProcessor||method=postProcessAfterInitialization||beanName={}||msg=exception", (Object)beanName, (Object)e);
        }
        return bean;
    }

    private boolean check(Task schedule) {
        return CronExpression.isValidExpression(schedule.cron());
    }

    private LogITaskPO getNewLogTask(Class<?> beanClass, Task schedule) {
        LogITaskPO logITaskPO = new LogITaskPO();
        logITaskPO.setTaskName(schedule.name());
        logITaskPO.setTaskDesc(schedule.description());
        logITaskPO.setCron(schedule.cron());
        logITaskPO.setClassName(beanClass.getCanonicalName());
        logITaskPO.setParams("");
        logITaskPO.setRetryTimes(schedule.retryTimes());
        logITaskPO.setLastFireTime(new Timestamp(System.currentTimeMillis()));
        logITaskPO.setTimeout(schedule.timeout());
        logITaskPO.setSubTaskCodes("");
        logITaskPO.setConsensual(schedule.consensual().name());
        logITaskPO.setTaskWorkerStr("");
        logITaskPO.setAppName(this.logIJobProperties.getAppName());
        logITaskPO.setOwner(schedule.owner());
        return logITaskPO;
    }

    private LogITaskPO updateLogTask(LogITaskPO logITaskPO, Class<?> beanClass, Task schedule) {
        logITaskPO.setTaskName(schedule.name());
        logITaskPO.setTaskDesc(schedule.description());
        logITaskPO.setCron(schedule.cron());
        logITaskPO.setClassName(beanClass.getCanonicalName());
        logITaskPO.setParams("");
        logITaskPO.setRetryTimes(schedule.retryTimes());
        logITaskPO.setTimeout(schedule.timeout());
        logITaskPO.setConsensual(schedule.consensual().name());
        logITaskPO.setAppName(this.logIJobProperties.getAppName());
        logITaskPO.setOwner(schedule.owner());
        return logITaskPO;
    }

    private boolean contains(String className) {
        if (taskMap.isEmpty()) {
            List<LogITaskPO> logITaskPOS = this.logITaskMapper.selectByAppName(this.logIJobProperties.getAppName());
            taskMap = logITaskPOS.stream().collect(Collectors.toMap(LogITaskPO::getClassName, Function.identity()));
        }
        return taskMap.containsKey(className);
    }
}

