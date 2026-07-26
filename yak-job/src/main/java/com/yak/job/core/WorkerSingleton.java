/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  oshi.SystemInfo
 *  oshi.hardware.CentralProcessor
 *  oshi.hardware.CentralProcessor$TickType
 *  oshi.hardware.GlobalMemory
 */
package com.yak.job.core;

import com.yak.job.common.domain.YakWorker;
import com.yak.job.utils.ThreadUtil;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

public class WorkerSingleton {
    private static final int CPU_INTERVAL = 1;
    private static final Logger logger = LoggerFactory.getLogger(WorkerSingleton.class);
    private volatile YakWorker yakWorker;

    private WorkerSingleton() {
    }

    public static WorkerSingleton getInstance() {
        return Singleton.singleton;
    }

    public WorkerSingleton updateInstanceMetrics() {
        Singleton.updateWorkerMetrics();
        return Singleton.singleton;
    }

    public YakWorker getYakWorker() {
        return this.yakWorker;
    }

    public void setYakWorker(YakWorker yakWorker) {
        this.yakWorker = yakWorker;
    }

    private static class Singleton {
        static WorkerSingleton singleton = new WorkerSingleton();

        private Singleton() {
        }

        public static WorkerSingleton updateWorkerMetrics() {
            YakWorker yakWorker = singleton.getYakWorker();
            SystemInfo systemInfo = new SystemInfo();
            CentralProcessor processor = systemInfo.getHardware().getProcessor();
            long[] prevTicks = processor.getSystemCpuLoadTicks();
            ThreadUtil.sleep(1L, TimeUnit.SECONDS);
            long[] ticks = processor.getSystemCpuLoadTicks();
            long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
            long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
            long softIrq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
            long steal = 0L;
            long csys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
            long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
            long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
            long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
            long totalCpu = user + nice + csys + idle + ioWait + irq + softIrq + steal;
            yakWorker.setCpu(processor.getLogicalProcessorCount());
            yakWorker.setCpuUsed(totalCpu == 0L ? null : Double.valueOf(1.0 - (double)idle * 1.0 / (double)totalCpu));
            GlobalMemory memory = systemInfo.getHardware().getMemory();
            Double totalMemory = (double)memory.getTotal() * 1.0 / 1024.0 / 1024.0;
            DecimalFormat df = new DecimalFormat("#.000");
            yakWorker.setMemory(Double.valueOf(df.format(totalMemory)));
            Double memoryUsed = (double)(memory.getTotal() - memory.getAvailable()) * 1.0 / (double)memory.getTotal();
            yakWorker.setMemoryUsed(Double.valueOf(df.format(memoryUsed)));
            Runtime runtime = Runtime.getRuntime();
            Double jvmMemory = (double)runtime.totalMemory() * 1.0 / 1024.0 / 1024.0;
            yakWorker.setJvmMemory(Double.valueOf(df.format(jvmMemory)));
            Double jvmMemoryUsed = (double)(runtime.totalMemory() - runtime.freeMemory()) * 1.0 / (double)runtime.totalMemory();
            yakWorker.setJvmMemoryUsed(Double.valueOf(df.format(jvmMemoryUsed)));
            yakWorker.setHeartbeat(new Timestamp(System.currentTimeMillis()));
            singleton.setYakWorker(yakWorker);
            return singleton;
        }

        static {
            YakWorker yakWorker = new YakWorker();
            InetAddress inetAddress = null;
            try {
                inetAddress = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                logger.error("class=SimpleWorkerFactory||method=||url=||msg=", (Throwable)e);
            }
            yakWorker.setWorkerCode(inetAddress == null ? "INVALID_CODE" : inetAddress.getHostAddress() + "_" + inetAddress.getHostName());
            yakWorker.setWorkerName(inetAddress == null ? "INVALID_NAME" : inetAddress.getHostName());
            yakWorker.setIp(inetAddress.getHostAddress());
            singleton.setYakWorker(yakWorker);
        }
    }
}

