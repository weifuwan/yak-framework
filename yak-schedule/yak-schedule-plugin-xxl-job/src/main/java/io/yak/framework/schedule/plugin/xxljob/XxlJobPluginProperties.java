package io.yak.framework.schedule.plugin.xxljob;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** XXL-JOB 3.4 插件配置。 */
@ConfigurationProperties("yak.schedule.xxl-job")
public class XxlJobPluginProperties {
    private String adminAddress;
    private String username = "admin";
    private String password = "123456";
    private int jobGroupId;
    private String author = "yak-framework";
    private String alarmEmail = "";
    private String routeStrategy = "FIRST";
    private int connectTimeoutSeconds = 3;
    private int requestTimeoutSeconds = 10;
    private final Executor executor = new Executor();

    public void validate() {
        if (adminAddress == null || adminAddress.isBlank()) {
            throw new IllegalStateException(
                    "yak.schedule.xxl-job.admin-address must not be blank");
        }
        if (jobGroupId < 1) {
            throw new IllegalStateException(
                    "yak.schedule.xxl-job.job-group-id must be positive");
        }
        if (executor.appName == null || executor.appName.isBlank()) {
            throw new IllegalStateException(
                    "yak.schedule.xxl-job.executor.app-name must not be blank");
        }
    }

    public String getAdminAddress() { return adminAddress; }
    public void setAdminAddress(String adminAddress) { this.adminAddress = adminAddress; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getJobGroupId() { return jobGroupId; }
    public void setJobGroupId(int jobGroupId) { this.jobGroupId = jobGroupId; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getAlarmEmail() { return alarmEmail; }
    public void setAlarmEmail(String alarmEmail) { this.alarmEmail = alarmEmail; }
    public String getRouteStrategy() { return routeStrategy; }
    public void setRouteStrategy(String routeStrategy) { this.routeStrategy = routeStrategy; }
    public int getConnectTimeoutSeconds() { return connectTimeoutSeconds; }
    public void setConnectTimeoutSeconds(int connectTimeoutSeconds) { this.connectTimeoutSeconds = connectTimeoutSeconds; }
    public int getRequestTimeoutSeconds() { return requestTimeoutSeconds; }
    public void setRequestTimeoutSeconds(int requestTimeoutSeconds) { this.requestTimeoutSeconds = requestTimeoutSeconds; }
    public Executor getExecutor() { return executor; }

    public static class Executor {
        private boolean enabled = true;
        private String appName = "yak-schedule-executor";
        private String address;
        private String ip;
        private int port = 9999;
        private String accessToken;
        private int timeoutSeconds = 3;
        private String logPath = "./logs/xxl-job";
        private int logRetentionDays = 30;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public String getAppName() { return appName; }
        public void setAppName(String appName) { this.appName = appName; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getIp() { return ip; }
        public void setIp(String ip) { this.ip = ip; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
        public int getTimeoutSeconds() { return timeoutSeconds; }
        public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
        public String getLogPath() { return logPath; }
        public void setLogPath(String logPath) { this.logPath = logPath; }
        public int getLogRetentionDays() { return logRetentionDays; }
        public void setLogRetentionDays(int logRetentionDays) { this.logRetentionDays = logRetentionDays; }
    }
}
