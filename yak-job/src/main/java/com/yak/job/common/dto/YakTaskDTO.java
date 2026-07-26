/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 *
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.job.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "YakTask \u4efb\u52a1\u4fe1\u606f")
public class YakTaskDTO {
    @ApiModelProperty(value = "\u4efb\u52a1\u540d\u79f0")
    private String name;
    @ApiModelProperty(value = "\u4efb\u52a1\u63cf\u8ff0")
    private String description;
    @ApiModelProperty(value = "\u4efb\u52a1\u8c03\u5ea6\u65f6\u95f4\u8868\u8fbe\u5f0f")
    private String cron;
    @ApiModelProperty(value = "\u4efb\u52a1\u5bf9\u5e94\u7684\u7c7b\u540d")
    private String className;
    @ApiModelProperty(value = "\u4efb\u52a1\u6267\u884c\u53c2\u6570")
    private String params;
    @ApiModelProperty(value = "\u4efb\u52a1\u91cd\u8bd5\u6b21\u6570")
    private Integer retryTimes;
    @ApiModelProperty(value = "\u4efb\u52a1\u62a2\u5360\u6a21\u5f0f")
    private String consensual;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCron() {
        return this.cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getParams() {
        return this.params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Integer getRetryTimes() {
        return this.retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getConsensual() {
        return this.consensual;
    }

    public void setConsensual(String consensual) {
        this.consensual = consensual;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof YakTaskDTO)) {
            return false;
        }
        YakTaskDTO other = (YakTaskDTO) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$retryTimes = this.getRetryTimes();
        Integer other$retryTimes = other.getRetryTimes();
        if (this$retryTimes == null ? other$retryTimes != null : !((Object) this$retryTimes).equals(other$retryTimes)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
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
        String this$consensual = this.getConsensual();
        String other$consensual = other.getConsensual();
        return !(this$consensual == null ? other$consensual != null : !this$consensual.equals(other$consensual));
    }

    protected boolean canEqual(Object other) {
        return other instanceof YakTaskDTO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $retryTimes = this.getRetryTimes();
        result = result * 59 + ($retryTimes == null ? 43 : ((Object) $retryTimes).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        String $cron = this.getCron();
        result = result * 59 + ($cron == null ? 43 : $cron.hashCode());
        String $className = this.getClassName();
        result = result * 59 + ($className == null ? 43 : $className.hashCode());
        String $params = this.getParams();
        result = result * 59 + ($params == null ? 43 : $params.hashCode());
        String $consensual = this.getConsensual();
        result = result * 59 + ($consensual == null ? 43 : $consensual.hashCode());
        return result;
    }

    public String toString() {
        return "YakTaskDTO(name=" + this.getName() + ", description=" + this.getDescription() + ", cron=" + this.getCron() + ", className=" + this.getClassName() + ", params=" + this.getParams() + ", retryTimes=" + this.getRetryTimes() + ", consensual=" + this.getConsensual() + ")";
    }
}

