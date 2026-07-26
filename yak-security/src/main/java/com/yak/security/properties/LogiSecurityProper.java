/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  javax.validation.constraints.NotEmpty
 *  org.springframework.boot.context.properties.ConfigurationProperties
 *  org.springframework.validation.annotation.Validated
 */
package com.yak.security.properties;

import javax.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(value="spring.logi-security")
public class LogiSecurityProper {
    @NotEmpty(message="\u914d\u7f6e\u6587\u4ef6\u914d\u7f6e\u5fc5\u987b\u8981\u914d\u7f6e[logi.security.app-name]\u5c5e\u6027")
    private @NotEmpty(message="\u914d\u7f6e\u6587\u4ef6\u914d\u7f6e\u5fc5\u987b\u8981\u914d\u7f6e[logi.security.app-name]\u5c5e\u6027") String appName;
    @NotEmpty(message="\u914d\u7f6e\u6587\u4ef6\u914d\u7f6e\u5fc5\u987b\u8981\u914d\u7f6e[logi.security.resource-extend-bean-name]\u5c5e\u6027")
    private @NotEmpty(message="\u914d\u7f6e\u6587\u4ef6\u914d\u7f6e\u5fc5\u987b\u8981\u914d\u7f6e[logi.security.resource-extend-bean-name]\u5c5e\u6027") String resourceExtendBeanName;
    private String loginExtendBeanName;
    @NotEmpty(message="\u914d\u7f6e\u6587\u4ef6\u914d\u7f6e\u5fc5\u987b\u8981\u914d\u7f6e[logi.security.username]\u5c5e\u6027")
    private @NotEmpty(message="\u914d\u7f6e\u6587\u4ef6\u914d\u7f6e\u5fc5\u987b\u8981\u914d\u7f6e[logi.security.username]\u5c5e\u6027") String username;
    @NotEmpty(message="\u914d\u7f6e\u6587\u4ef6\u914d\u7f6e\u5fc5\u987b\u8981\u914d\u7f6e[logi.security.password]\u5c5e\u6027")
    private @NotEmpty(message="\u914d\u7f6e\u6587\u4ef6\u914d\u7f6e\u5fc5\u987b\u8981\u914d\u7f6e[logi.security.password]\u5c5e\u6027") String password;
    @NotEmpty(message="\u914d\u7f6e\u6587\u4ef6\u914d\u7f6e\u5fc5\u987b\u8981\u914d\u7f6e[logi.security.url]\u5c5e\u6027")
    private @NotEmpty(message="\u914d\u7f6e\u6587\u4ef6\u914d\u7f6e\u5fc5\u987b\u8981\u914d\u7f6e[logi.security.url]\u5c5e\u6027") String jdbcUrl;
    @NotEmpty(message="\u914d\u7f6e\u6587\u4ef6\u914d\u7f6e\u5fc5\u987b\u8981\u914d\u7f6e[logi.security.driver-class-name]\u5c5e\u6027")
    private @NotEmpty(message="\u914d\u7f6e\u6587\u4ef6\u914d\u7f6e\u5fc5\u987b\u8981\u914d\u7f6e[logi.security.driver-class-name]\u5c5e\u6027") String driverClassName;

    public String getAppName() {
        return this.appName;
    }

    public String getResourceExtendBeanName() {
        return this.resourceExtendBeanName;
    }

    public String getLoginExtendBeanName() {
        return this.loginExtendBeanName;
    }

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

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setResourceExtendBeanName(String resourceExtendBeanName) {
        this.resourceExtendBeanName = resourceExtendBeanName;
    }

    public void setLoginExtendBeanName(String loginExtendBeanName) {
        this.loginExtendBeanName = loginExtendBeanName;
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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LogiSecurityProper)) {
            return false;
        }
        LogiSecurityProper other = (LogiSecurityProper)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$appName = this.getAppName();
        String other$appName = other.getAppName();
        if (this$appName == null ? other$appName != null : !this$appName.equals(other$appName)) {
            return false;
        }
        String this$resourceExtendBeanName = this.getResourceExtendBeanName();
        String other$resourceExtendBeanName = other.getResourceExtendBeanName();
        if (this$resourceExtendBeanName == null ? other$resourceExtendBeanName != null : !this$resourceExtendBeanName.equals(other$resourceExtendBeanName)) {
            return false;
        }
        String this$loginExtendBeanName = this.getLoginExtendBeanName();
        String other$loginExtendBeanName = other.getLoginExtendBeanName();
        if (this$loginExtendBeanName == null ? other$loginExtendBeanName != null : !this$loginExtendBeanName.equals(other$loginExtendBeanName)) {
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
        return !(this$driverClassName == null ? other$driverClassName != null : !this$driverClassName.equals(other$driverClassName));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LogiSecurityProper;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $appName = this.getAppName();
        result = result * 59 + ($appName == null ? 43 : $appName.hashCode());
        String $resourceExtendBeanName = this.getResourceExtendBeanName();
        result = result * 59 + ($resourceExtendBeanName == null ? 43 : $resourceExtendBeanName.hashCode());
        String $loginExtendBeanName = this.getLoginExtendBeanName();
        result = result * 59 + ($loginExtendBeanName == null ? 43 : $loginExtendBeanName.hashCode());
        String $username = this.getUsername();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        String $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        String $jdbcUrl = this.getJdbcUrl();
        result = result * 59 + ($jdbcUrl == null ? 43 : $jdbcUrl.hashCode());
        String $driverClassName = this.getDriverClassName();
        result = result * 59 + ($driverClassName == null ? 43 : $driverClassName.hashCode());
        return result;
    }

    public String toString() {
        return "LogiSecurityProper(appName=" + this.getAppName() + ", resourceExtendBeanName=" + this.getResourceExtendBeanName() + ", loginExtendBeanName=" + this.getLoginExtendBeanName() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ", jdbcUrl=" + this.getJdbcUrl() + ", driverClassName=" + this.getDriverClassName() + ")";
    }
}

