/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.common.domain;

import com.yak.job.common.po.LogIWorkerPO;
import java.sql.Timestamp;

public class LogIWorker {
    private String workerCode;
    private String workerName;
    private String ip;
    private Integer cpu;
    private Double cpuUsed;
    private Double memory;
    private Double memoryUsed;
    private Double jvmMemory;
    private Double jvmMemoryUsed;
    private Integer jobNum;
    private Timestamp heartbeat;
    private String appName;

    public LogIWorkerPO getWorker() {
        LogIWorkerPO logIWorkerPO = new LogIWorkerPO();
        logIWorkerPO.setWorkerCode(this.workerCode);
        logIWorkerPO.setWorkerName(this.workerName);
        logIWorkerPO.setIp(this.getIp());
        logIWorkerPO.setCpu(this.cpu);
        logIWorkerPO.setCpuUsed(this.cpuUsed);
        logIWorkerPO.setMemory(this.memory);
        logIWorkerPO.setMemoryUsed(this.memoryUsed);
        logIWorkerPO.setJvmMemory(this.jvmMemory);
        logIWorkerPO.setJvmMemoryUsed(this.jvmMemoryUsed);
        logIWorkerPO.setJobNum(this.jobNum);
        logIWorkerPO.setHeartbeat(new Timestamp(System.currentTimeMillis()));
        logIWorkerPO.setAppName(this.appName);
        return logIWorkerPO;
    }

    public String getWorkerCode() {
        return this.workerCode;
    }

    public String getWorkerName() {
        return this.workerName;
    }

    public String getIp() {
        return this.ip;
    }

    public Integer getCpu() {
        return this.cpu;
    }

    public Double getCpuUsed() {
        return this.cpuUsed;
    }

    public Double getMemory() {
        return this.memory;
    }

    public Double getMemoryUsed() {
        return this.memoryUsed;
    }

    public Double getJvmMemory() {
        return this.jvmMemory;
    }

    public Double getJvmMemoryUsed() {
        return this.jvmMemoryUsed;
    }

    public Integer getJobNum() {
        return this.jobNum;
    }

    public Timestamp getHeartbeat() {
        return this.heartbeat;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public void setCpuUsed(Double cpuUsed) {
        this.cpuUsed = cpuUsed;
    }

    public void setMemory(Double memory) {
        this.memory = memory;
    }

    public void setMemoryUsed(Double memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public void setJvmMemory(Double jvmMemory) {
        this.jvmMemory = jvmMemory;
    }

    public void setJvmMemoryUsed(Double jvmMemoryUsed) {
        this.jvmMemoryUsed = jvmMemoryUsed;
    }

    public void setJobNum(Integer jobNum) {
        this.jobNum = jobNum;
    }

    public void setHeartbeat(Timestamp heartbeat) {
        this.heartbeat = heartbeat;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LogIWorker)) {
            return false;
        }
        LogIWorker other = (LogIWorker)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$cpu = this.getCpu();
        Integer other$cpu = other.getCpu();
        if (this$cpu == null ? other$cpu != null : !((Object)this$cpu).equals(other$cpu)) {
            return false;
        }
        Double this$cpuUsed = this.getCpuUsed();
        Double other$cpuUsed = other.getCpuUsed();
        if (this$cpuUsed == null ? other$cpuUsed != null : !((Object)this$cpuUsed).equals(other$cpuUsed)) {
            return false;
        }
        Double this$memory = this.getMemory();
        Double other$memory = other.getMemory();
        if (this$memory == null ? other$memory != null : !((Object)this$memory).equals(other$memory)) {
            return false;
        }
        Double this$memoryUsed = this.getMemoryUsed();
        Double other$memoryUsed = other.getMemoryUsed();
        if (this$memoryUsed == null ? other$memoryUsed != null : !((Object)this$memoryUsed).equals(other$memoryUsed)) {
            return false;
        }
        Double this$jvmMemory = this.getJvmMemory();
        Double other$jvmMemory = other.getJvmMemory();
        if (this$jvmMemory == null ? other$jvmMemory != null : !((Object)this$jvmMemory).equals(other$jvmMemory)) {
            return false;
        }
        Double this$jvmMemoryUsed = this.getJvmMemoryUsed();
        Double other$jvmMemoryUsed = other.getJvmMemoryUsed();
        if (this$jvmMemoryUsed == null ? other$jvmMemoryUsed != null : !((Object)this$jvmMemoryUsed).equals(other$jvmMemoryUsed)) {
            return false;
        }
        Integer this$jobNum = this.getJobNum();
        Integer other$jobNum = other.getJobNum();
        if (this$jobNum == null ? other$jobNum != null : !((Object)this$jobNum).equals(other$jobNum)) {
            return false;
        }
        String this$workerCode = this.getWorkerCode();
        String other$workerCode = other.getWorkerCode();
        if (this$workerCode == null ? other$workerCode != null : !this$workerCode.equals(other$workerCode)) {
            return false;
        }
        String this$workerName = this.getWorkerName();
        String other$workerName = other.getWorkerName();
        if (this$workerName == null ? other$workerName != null : !this$workerName.equals(other$workerName)) {
            return false;
        }
        String this$ip = this.getIp();
        String other$ip = other.getIp();
        if (this$ip == null ? other$ip != null : !this$ip.equals(other$ip)) {
            return false;
        }
        Timestamp this$heartbeat = this.getHeartbeat();
        Timestamp other$heartbeat = other.getHeartbeat();
        if (this$heartbeat == null ? other$heartbeat != null : !((Object)this$heartbeat).equals(other$heartbeat)) {
            return false;
        }
        String this$appName = this.getAppName();
        String other$appName = other.getAppName();
        return !(this$appName == null ? other$appName != null : !this$appName.equals(other$appName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LogIWorker;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $cpu = this.getCpu();
        result = result * 59 + ($cpu == null ? 43 : ((Object)$cpu).hashCode());
        Double $cpuUsed = this.getCpuUsed();
        result = result * 59 + ($cpuUsed == null ? 43 : ((Object)$cpuUsed).hashCode());
        Double $memory = this.getMemory();
        result = result * 59 + ($memory == null ? 43 : ((Object)$memory).hashCode());
        Double $memoryUsed = this.getMemoryUsed();
        result = result * 59 + ($memoryUsed == null ? 43 : ((Object)$memoryUsed).hashCode());
        Double $jvmMemory = this.getJvmMemory();
        result = result * 59 + ($jvmMemory == null ? 43 : ((Object)$jvmMemory).hashCode());
        Double $jvmMemoryUsed = this.getJvmMemoryUsed();
        result = result * 59 + ($jvmMemoryUsed == null ? 43 : ((Object)$jvmMemoryUsed).hashCode());
        Integer $jobNum = this.getJobNum();
        result = result * 59 + ($jobNum == null ? 43 : ((Object)$jobNum).hashCode());
        String $workerCode = this.getWorkerCode();
        result = result * 59 + ($workerCode == null ? 43 : $workerCode.hashCode());
        String $workerName = this.getWorkerName();
        result = result * 59 + ($workerName == null ? 43 : $workerName.hashCode());
        String $ip = this.getIp();
        result = result * 59 + ($ip == null ? 43 : $ip.hashCode());
        Timestamp $heartbeat = this.getHeartbeat();
        result = result * 59 + ($heartbeat == null ? 43 : ((Object)$heartbeat).hashCode());
        String $appName = this.getAppName();
        result = result * 59 + ($appName == null ? 43 : $appName.hashCode());
        return result;
    }

    public String toString() {
        return "LogIWorker(workerCode=" + this.getWorkerCode() + ", workerName=" + this.getWorkerName() + ", ip=" + this.getIp() + ", cpu=" + this.getCpu() + ", cpuUsed=" + this.getCpuUsed() + ", memory=" + this.getMemory() + ", memoryUsed=" + this.getMemoryUsed() + ", jvmMemory=" + this.getJvmMemory() + ", jvmMemoryUsed=" + this.getJvmMemoryUsed() + ", jobNum=" + this.getJobNum() + ", heartbeat=" + this.getHeartbeat() + ", appName=" + this.getAppName() + ")";
    }
}

