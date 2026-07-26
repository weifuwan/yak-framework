/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.job.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;
import java.util.List;

@ApiModel(description="YakTaskVO \u4efb\u52a1\u8be6\u60c5")
public class YakTaskVO {
    @ApiModelProperty(value="taskcode")
    private Long id;
    @ApiModelProperty(value="taskcode")
    private String taskCode;
    @ApiModelProperty(value="\u4efb\u52a1\u540d\u79f0")
    private String taskName;
    @ApiModelProperty(value="\u4efb\u52a1\u8d23\u4efb\u4eba")
    private String owner;
    @ApiModelProperty(value="\u4efb\u52a1\u63cf\u8ff0")
    private String taskDesc;
    @ApiModelProperty(value="\u5b9a\u65f6\u4efb\u52a1\u8c03\u5ea6\u65f6\u95f4\u8868\u8fbe\u5f0f")
    private String cron;
    @ApiModelProperty(value="\u5b9a\u65f6\u4efb\u52a1\u8c03\u5ea6\u6267\u884c\u4ee3\u7801")
    private String className;
    @ApiModelProperty(value="\u6267\u884c\u53c2\u6570 map \u5f62\u5f0f{key1:value1,key2:value2}")
    private String params;
    @ApiModelProperty(value="\u4e0a\u6b21\u8c03\u5ea6\u6267\u884c\u65f6\u95f4")
    private Timestamp lastFireTime;
    @ApiModelProperty(value="\u4e0b\u6b21\u8c03\u5ea6\u6267\u884c\u65f6\u95f4")
    private Timestamp nextFireTime;
    @ApiModelProperty(value="\u4efb\u52a1\u72b6\u6001\uff0c1\uff1a\u6b63\u5e38\uff0c0\uff1a\u6682\u505c")
    private Integer status;
    @ApiModelProperty(value="\u8c03\u5ea6\u65b9\u5f0f\uff1a\u5355\u64ad\u3001\u5e7f\u64ad")
    private String consensual;
    @ApiModelProperty(value="\u5e94\u7528\u540d\u79f0")
    private String appName;
    @ApiModelProperty(value="\u8c03\u5ea6\u673a\u5668\u5217\u8868")
    private List<String> workerIps;
    @ApiModelProperty(value="\u8def\u7531\u7b56\u7565")
    private String routing;
    @ApiModelProperty(value="\u8fd0\u884c\u6a21\u5f0f")
    private String runningType = "BEAN\u6a21\u5f0f";
    @ApiModelProperty(value="\u963b\u585e\u7b56\u7565")
    private String blockPolicy = "\u5355\u673a\u4e32\u884c";
    @ApiModelProperty(value="\u662f\u5426\u53ef\u4ee5\u5220\u9664\uff0c1\uff1a\u53ef\u4ee5\uff0c0\uff1a\u4e0d\u53ef\u4ee5")
    private Integer del;
    @ApiModelProperty(value="\u4efb\u52a1\u88ab\u8c03\u5ea6\u65f6\u95f4")
    private Timestamp createTime;
    @ApiModelProperty(value="\u4efb\u52a1\u88ab\u8c03\u5ea6\u65f6\u95f4")
    private Timestamp updateTime;

    public Long getId() {
        return this.id;
    }

    public String getTaskCode() {
        return this.taskCode;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public String getOwner() {
        return this.owner;
    }

    public String getTaskDesc() {
        return this.taskDesc;
    }

    public String getCron() {
        return this.cron;
    }

    public String getClassName() {
        return this.className;
    }

    public String getParams() {
        return this.params;
    }

    public Timestamp getLastFireTime() {
        return this.lastFireTime;
    }

    public Timestamp getNextFireTime() {
        return this.nextFireTime;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getConsensual() {
        return this.consensual;
    }

    public String getAppName() {
        return this.appName;
    }

    public List<String> getWorkerIps() {
        return this.workerIps;
    }

    public String getRouting() {
        return this.routing;
    }

    public String getRunningType() {
        return this.runningType;
    }

    public String getBlockPolicy() {
        return this.blockPolicy;
    }

    public Integer getDel() {
        return this.del;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public Timestamp getUpdateTime() {
        return this.updateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setLastFireTime(Timestamp lastFireTime) {
        this.lastFireTime = lastFireTime;
    }

    public void setNextFireTime(Timestamp nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setConsensual(String consensual) {
        this.consensual = consensual;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setWorkerIps(List<String> workerIps) {
        this.workerIps = workerIps;
    }

    public void setRouting(String routing) {
        this.routing = routing;
    }

    public void setRunningType(String runningType) {
        this.runningType = runningType;
    }

    public void setBlockPolicy(String blockPolicy) {
        this.blockPolicy = blockPolicy;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof YakTaskVO)) {
            return false;
        }
        YakTaskVO other = (YakTaskVO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$status = this.getStatus();
        Integer other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)this$status).equals(other$status)) {
            return false;
        }
        Integer this$del = this.getDel();
        Integer other$del = other.getDel();
        if (this$del == null ? other$del != null : !((Object)this$del).equals(other$del)) {
            return false;
        }
        String this$taskCode = this.getTaskCode();
        String other$taskCode = other.getTaskCode();
        if (this$taskCode == null ? other$taskCode != null : !this$taskCode.equals(other$taskCode)) {
            return false;
        }
        String this$taskName = this.getTaskName();
        String other$taskName = other.getTaskName();
        if (this$taskName == null ? other$taskName != null : !this$taskName.equals(other$taskName)) {
            return false;
        }
        String this$owner = this.getOwner();
        String other$owner = other.getOwner();
        if (this$owner == null ? other$owner != null : !this$owner.equals(other$owner)) {
            return false;
        }
        String this$taskDesc = this.getTaskDesc();
        String other$taskDesc = other.getTaskDesc();
        if (this$taskDesc == null ? other$taskDesc != null : !this$taskDesc.equals(other$taskDesc)) {
            return false;
        }
        String this$cron = this.getCron();
        String other$cron = other.getCron();
        if (this$cron == null ? other$cron != null : !this$cron.equals(other$cron)) {
            return false;
        }
        String this$className = this.getClassName();
        String other$className = other.getClassName();
        if (this$className == null ? other$className != null : !this$className.equals(other$className)) {
            return false;
        }
        String this$params = this.getParams();
        String other$params = other.getParams();
        if (this$params == null ? other$params != null : !this$params.equals(other$params)) {
            return false;
        }
        Timestamp this$lastFireTime = this.getLastFireTime();
        Timestamp other$lastFireTime = other.getLastFireTime();
        if (this$lastFireTime == null ? other$lastFireTime != null : !((Object)this$lastFireTime).equals(other$lastFireTime)) {
            return false;
        }
        Timestamp this$nextFireTime = this.getNextFireTime();
        Timestamp other$nextFireTime = other.getNextFireTime();
        if (this$nextFireTime == null ? other$nextFireTime != null : !((Object)this$nextFireTime).equals(other$nextFireTime)) {
            return false;
        }
        String this$consensual = this.getConsensual();
        String other$consensual = other.getConsensual();
        if (this$consensual == null ? other$consensual != null : !this$consensual.equals(other$consensual)) {
            return false;
        }
        String this$appName = this.getAppName();
        String other$appName = other.getAppName();
        if (this$appName == null ? other$appName != null : !this$appName.equals(other$appName)) {
            return false;
        }
        List<String> this$workerIps = this.getWorkerIps();
        List<String> other$workerIps = other.getWorkerIps();
        if (this$workerIps == null ? other$workerIps != null : !((Object)this$workerIps).equals(other$workerIps)) {
            return false;
        }
        String this$routing = this.getRouting();
        String other$routing = other.getRouting();
        if (this$routing == null ? other$routing != null : !this$routing.equals(other$routing)) {
            return false;
        }
        String this$runningType = this.getRunningType();
        String other$runningType = other.getRunningType();
        if (this$runningType == null ? other$runningType != null : !this$runningType.equals(other$runningType)) {
            return false;
        }
        String this$blockPolicy = this.getBlockPolicy();
        String other$blockPolicy = other.getBlockPolicy();
        if (this$blockPolicy == null ? other$blockPolicy != null : !this$blockPolicy.equals(other$blockPolicy)) {
            return false;
        }
        Timestamp this$createTime = this.getCreateTime();
        Timestamp other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Timestamp this$updateTime = this.getUpdateTime();
        Timestamp other$updateTime = other.getUpdateTime();
        return !(this$updateTime == null ? other$updateTime != null : !((Object)this$updateTime).equals(other$updateTime));
    }

    protected boolean canEqual(Object other) {
        return other instanceof YakTaskVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)$status).hashCode());
        Integer $del = this.getDel();
        result = result * 59 + ($del == null ? 43 : ((Object)$del).hashCode());
        String $taskCode = this.getTaskCode();
        result = result * 59 + ($taskCode == null ? 43 : $taskCode.hashCode());
        String $taskName = this.getTaskName();
        result = result * 59 + ($taskName == null ? 43 : $taskName.hashCode());
        String $owner = this.getOwner();
        result = result * 59 + ($owner == null ? 43 : $owner.hashCode());
        String $taskDesc = this.getTaskDesc();
        result = result * 59 + ($taskDesc == null ? 43 : $taskDesc.hashCode());
        String $cron = this.getCron();
        result = result * 59 + ($cron == null ? 43 : $cron.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $params = this.getParams();
        result = result * 59 + ($params == null ? 43 : $params.hashCode());
        Timestamp $lastFireTime = this.getLastFireTime();
        result = result * 59 + ($lastFireTime == null ? 43 : ((Object)$lastFireTime).hashCode());
        Timestamp $nextFireTime = this.getNextFireTime();
        result = result * 59 + ($nextFireTime == null ? 43 : ((Object)$nextFireTime).hashCode());
        String $consensual = this.getConsensual();
        result = result * 59 + ($consensual == null ? 43 : $consensual.hashCode());
        String $appName = this.getAppName();
        result = result * 59 + ($appName == null ? 43 : $appName.hashCode());
        List<String> $workerIps = this.getWorkerIps();
        result = result * 59 + ($workerIps == null ? 43 : ((Object)$workerIps).hashCode());
        String $routing = this.getRouting();
        result = result * 59 + ($routing == null ? 43 : $routing.hashCode());
        String $runningType = this.getRunningType();
        result = result * 59 + ($runningType == null ? 43 : $runningType.hashCode());
        String $blockPolicy = this.getBlockPolicy();
        result = result * 59 + ($blockPolicy == null ? 43 : $blockPolicy.hashCode());
        Timestamp $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Timestamp $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : ((Object)$updateTime).hashCode());
        return result;
    }

    public String toString() {
        return "YakTaskVO(id=" + this.getId() + ", taskCode=" + this.getTaskCode() + ", taskName=" + this.getTaskName() + ", owner=" + this.getOwner() + ", taskDesc=" + this.getTaskDesc() + ", cron=" + this.getCron() + ", className=" + this.getClassName() + ", params=" + this.getParams() + ", lastFireTime=" + this.getLastFireTime() + ", nextFireTime=" + this.getNextFireTime() + ", status=" + this.getStatus() + ", consensual=" + this.getConsensual() + ", appName=" + this.getAppName() + ", workerIps=" + this.getWorkerIps() + ", routing=" + this.getRouting() + ", runningType=" + this.getRunningType() + ", blockPolicy=" + this.getBlockPolicy() + ", del=" + this.getDel() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ")";
    }
}

