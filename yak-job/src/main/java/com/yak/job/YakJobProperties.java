/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.boot.context.properties.ConfigurationProperties
 */
package com.yak.job;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value="spring.yak-job")
public class YakJobProperties {
    private String username;
    private String password;
    private String jdbcUrl;
    private String driverClassName;
    private Long maxLifetime;
    private Boolean initSql;
    private Integer initThreadNum;
    private Integer maxThreadNum;
    private Integer logExpire;
    private String appName;
    private Boolean enable = true;

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getJdbcUrl() {
        return this.jdbcUrl;
    }

    public String getDriverClassName() {
        return this.driverClassName;
    }

    public Long getMaxLifetime() {
        return this.maxLifetime;
    }

    public Boolean getInitSql() {
        return this.initSql;
    }

    public Integer getInitThreadNum() {
        return this.initThreadNum;
    }

    public Integer getMaxThreadNum() {
        return this.maxThreadNum;
    }

    public Integer getLogExpire() {
        return this.logExpire;
    }

    public String getAppName() {
        return this.appName;
    }

    public Boolean getEnable() {
        return this.enable;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public void setMaxLifetime(Long maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    public void setInitSql(Boolean initSql) {
        this.initSql = initSql;
    }

    public void setInitThreadNum(Integer initThreadNum) {
        this.initThreadNum = initThreadNum;
    }

    public void setMaxThreadNum(Integer maxThreadNum) {
        this.maxThreadNum = maxThreadNum;
    }

    public void setLogExpire(Integer logExpire) {
        this.logExpire = logExpire;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof YakJobProperties)) {
            return false;
        }
        YakJobProperties other = (YakJobProperties)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$maxLifetime = this.getMaxLifetime();
        Long other$maxLifetime = other.getMaxLifetime();
        if (this$maxLifetime == null ? other$maxLifetime != null : !((Object)this$maxLifetime).equals(other$maxLifetime)) {
            return false;
        }
        Boolean this$initSql = this.getInitSql();
        Boolean other$initSql = other.getInitSql();
        if (this$initSql == null ? other$initSql != null : !((Object)this$initSql).equals(other$initSql)) {
            return false;
        }
        Integer this$initThreadNum = this.getInitThreadNum();
        Integer other$initThreadNum = other.getInitThreadNum();
        if (this$initThreadNum == null ? other$initThreadNum != null : !((Object)this$initThreadNum).equals(other$initThreadNum)) {
            return false;
        }
        Integer this$maxThreadNum = this.getMaxThreadNum();
        Integer other$maxThreadNum = other.getMaxThreadNum();
        if (this$maxThreadNum == null ? other$maxThreadNum != null : !((Object)this$maxThreadNum).equals(other$maxThreadNum)) {
            return false;
        }
        Integer this$logExpire = this.getLogExpire();
        Integer other$logExpire = other.getLogExpire();
        if (this$logExpire == null ? other$logExpire != null : !((Object)this$logExpire).equals(other$logExpire)) {
            return false;
        }
        Boolean this$enable = this.getEnable();
        Boolean other$enable = other.getEnable();
        if (this$enable == null ? other$enable != null : !((Object)this$enable).equals(other$enable)) {
            return false;
        }
        String this$username = this.getUsername();
        String other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) {
            return false;
        }
        String this$password = this.getPassword();
        String other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) {
            return false;
        }
        String this$jdbcUrl = this.getJdbcUrl();
        String other$jdbcUrl = other.getJdbcUrl();
        if (this$jdbcUrl == null ? other$jdbcUrl != null : !this$jdbcUrl.equals(other$jdbcUrl)) {
            return false;
        }
        String this$driverClassName = this.getDriverClassName();
        String other$driverClassName = other.getDriverClassName();
        if (this$driverClassName == null ? other$driverClassName != null : !this$driverClassName.equals(other$driverClassName)) {
            return false;
        }
        String this$appName = this.getAppName();
        String other$appName = other.getAppName();
        return !(this$appName == null ? other$appName != null : !this$appName.equals(other$appName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof YakJobProperties;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $maxLifetime = this.getMaxLifetime();
        result = result * 59 + ($maxLifetime == null ? 43 : ((Object)$maxLifetime).hashCode());
        Boolean $initSql = this.getInitSql();
        result = result * 59 + ($initSql == null ? 43 : ((Object)$initSql).hashCode());
        Integer $initThreadNum = this.getInitThreadNum();
        result = result * 59 + ($initThreadNum == null ? 43 : ((Object)$initThreadNum).hashCode());
        Integer $maxThreadNum = this.getMaxThreadNum();
        result = result * 59 + ($maxThreadNum == null ? 43 : ((Object)$maxThreadNum).hashCode());
        Integer $logExpire = this.getLogExpire();
        result = result * 59 + ($logExpire == null ? 43 : ((Object)$logExpire).hashCode());
        Boolean $enable = this.getEnable();
        result = result * 59 + ($enable == null ? 43 : ((Object)$enable).hashCode());
        String $username = this.getUsername();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $jdbcUrl = this.getJdbcUrl();
        result = result * 59 + ($jdbcUrl == null ? 43 : $jdbcUrl.hashCode());
        String $driverClassName = this.getDriverClassName();
        result = result * 59 + ($driverClassName == null ? 43 : $driverClassName.hashCode());
        String $appName = this.getAppName();
        result = result * 59 + ($appName == null ? 43 : $appName.hashCode());
        return result;
    }

    public String toString() {
        return "YakJobProperties(username=" + this.getUsername() + ", password=" + this.getPassword() + ", jdbcUrl=" + this.getJdbcUrl() + ", driverClassName=" + this.getDriverClassName() + ", maxLifetime=" + this.getMaxLifetime() + ", initSql=" + this.getInitSql() + ", initThreadNum=" + this.getInitThreadNum() + ", maxThreadNum=" + this.getMaxThreadNum() + ", logExpire=" + this.getLogExpire() + ", appName=" + this.getAppName() + ", enable=" + this.getEnable() + ")";
    }
}

