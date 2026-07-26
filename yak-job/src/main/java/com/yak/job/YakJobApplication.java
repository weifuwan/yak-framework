package com.yak.job;

import com.yak.job.core.SimpleScheduler;
import com.yak.job.core.monitor.BeatMonitor;
import com.yak.job.core.monitor.MisfireMonitor;
import com.yak.job.core.monitor.TaskMonitor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.yak.job"})
public class YakJobApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(new Class[]{YakJobApplication.class});
        BeatMonitor beatMonitor = (BeatMonitor) applicationContext.getBean(BeatMonitor.class);
        TaskMonitor taskMonitor = (TaskMonitor) applicationContext.getBean(TaskMonitor.class);
        MisfireMonitor misfireMonitor = (MisfireMonitor) applicationContext.getBean(MisfireMonitor.class);
        SimpleScheduler simpleScheduler = new SimpleScheduler(beatMonitor, taskMonitor, misfireMonitor);
        simpleScheduler.startup();
    }
}

