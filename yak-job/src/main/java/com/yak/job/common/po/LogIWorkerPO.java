/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.common.po;

import java.io.Serializable;
import java.sql.Timestamp;

public class LogIWorkerPO
extends BasePO
implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
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

    public Long getId() {
        return this.id;
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

    public LogIWorkerPO setId(Long id) {
        this.id = id;
        return this;
    }

    public LogIWorkerPO setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
        return this;
    }

    public LogIWorkerPO setWorkerName(String workerName) {
        this.workerName = workerName;
        return this;
    }

    public LogIWorkerPO setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public LogIWorkerPO setCpu(Integer cpu) {
        this.cpu = cpu;
        return this;
    }

    public LogIWorkerPO setCpuUsed(Double cpuUsed) {
        this.cpuUsed = cpuUsed;
        return this;
    }

    public LogIWorkerPO setMemory(Double memory) {
        this.memory = memory;
        return this;
    }

    public LogIWorkerPO setMemoryUsed(Double memoryUsed) {
        this.memoryUsed = memoryUsed;
        return this;
    }

    public LogIWorkerPO setJvmMemory(Double jvmMemory) {
        this.jvmMemory = jvmMemory;
        return this;
    }

    public LogIWorkerPO setJvmMemoryUsed(Double jvmMemoryUsed) {
        this.jvmMemoryUsed = jvmMemoryUsed;
        return this;
    }

    public LogIWorkerPO setJobNum(Integer jobNum) {
        this.jobNum = jobNum;
        return this;
    }

    public LogIWorkerPO setHeartbeat(Timestamp heartbeat) {
        this.heartbeat = heartbeat;
        return this;
    }

    public LogIWorkerPO setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    @Override
    public String toString() {
        return "LogIWorkerPO(id=" + this.getId() + ", workerCode=" + this.getWorkerCode() + ", workerName=" + this.getWorkerName() + ", ip=" + this.getIp() + ", cpu=" + this.getCpu() + ", cpuUsed=" + this.getCpuUsed() + ", memory=" + this.getMemory() + ", memoryUsed=" + this.getMemoryUsed() + ", jvmMemory=" + this.getJvmMemory() + ", jvmMemoryUsed=" + this.getJvmMemoryUsed() + ", jobNum=" + this.getJobNum() + ", heartbeat=" + this.getHeartbeat() + ", appName=" + this.getAppName() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LogIWorkerPO)) {
            return false;
        }
        LogIWorkerPO other = (LogIWorkerPO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
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

    @Override
    protected boolean canEqual(Object other) {
        return other instanceof LogIWorkerPO;
    }

    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
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
}

