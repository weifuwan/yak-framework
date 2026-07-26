package io.yak.framework.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Yak Security 模块配置。 */
@ConfigurationProperties(prefix = "yak.security")
public class YakSecurityProperties {
  /** 是否启用 Yak Security。 */
  private boolean enabled = true;
  /** 是否启用数据库相关功能。 */
  private boolean databaseEnabled = true;
  /** 是否启用 Web 接口。 */
  private boolean webEnabled = true;
  /** 是否启用审计功能。 */
  private boolean auditEnabled = true;
  /** 用于隔离安全数据的应用名称。 */
  private String applicationName = "default";
  /** Yak Security 独立数据源配置。 */
  private final DataSourceProperties datasource = new DataSourceProperties();

  public boolean isEnabled() { return enabled; }
  public void setEnabled(boolean enabled) { this.enabled = enabled; }
  public boolean isDatabaseEnabled() { return databaseEnabled; }
  public void setDatabaseEnabled(boolean databaseEnabled) { this.databaseEnabled = databaseEnabled; }
  public boolean isWebEnabled() { return webEnabled; }
  public void setWebEnabled(boolean webEnabled) { this.webEnabled = webEnabled; }
  public boolean isAuditEnabled() { return auditEnabled; }
  public void setAuditEnabled(boolean auditEnabled) { this.auditEnabled = auditEnabled; }
  public String getApplicationName() { return applicationName; }
  public void setApplicationName(String applicationName) { this.applicationName = applicationName; }
  public DataSourceProperties getDatasource() { return datasource; }

  @Override
  public String toString() {
    return "YakSecurityProperties(enabled=" + enabled +
        ", databaseEnabled=" + databaseEnabled + ", webEnabled=" + webEnabled +
        ", auditEnabled=" + auditEnabled + ", applicationName=" + applicationName +
        ", datasource=" + datasource + ")";
  }

  /** Yak Security 使用的 Druid 数据源参数。 */
  public static class DataSourceProperties {
    /** 是否启用 Yak Security 独立数据源。 */
    private boolean enabled = true;
    /** 数据库 JDBC 连接地址。 */
    private String url;
    /** 数据库登录用户名。 */
    private String username;
    /** 数据库登录密码。 */
    private String password;
    /** JDBC 驱动类名。 */
    private String driverClassName;
    /** 连接池初始化连接数。 */
    private int initialSize = 0;
    /** 连接池最小空闲连接数。 */
    private int minIdle = 0;
    /** 连接池最大活跃连接数。 */
    private int maxActive = 8;
    /** 获取连接的最大等待毫秒数。 */
    private long maxWait = -1L;
    /** 检测连接有效性的 SQL。 */
    private String validationQuery;
    /** 是否在空闲连接检测时验证连接。 */
    private boolean testWhileIdle = true;
    /** 是否在借出连接时验证连接。 */
    private boolean testOnBorrow = false;
    /** 是否在归还连接时验证连接。 */
    private boolean testOnReturn = false;

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getDriverClassName() { return driverClassName; }
    public void setDriverClassName(String driverClassName) { this.driverClassName = driverClassName; }
    public int getInitialSize() { return initialSize; }
    public void setInitialSize(int initialSize) { this.initialSize = initialSize; }
    public int getMinIdle() { return minIdle; }
    public void setMinIdle(int minIdle) { this.minIdle = minIdle; }
    public int getMaxActive() { return maxActive; }
    public void setMaxActive(int maxActive) { this.maxActive = maxActive; }
    public long getMaxWait() { return maxWait; }
    public void setMaxWait(long maxWait) { this.maxWait = maxWait; }
    public String getValidationQuery() { return validationQuery; }
    public void setValidationQuery(String validationQuery) { this.validationQuery = validationQuery; }
    public boolean isTestWhileIdle() { return testWhileIdle; }
    public void setTestWhileIdle(boolean testWhileIdle) { this.testWhileIdle = testWhileIdle; }
    public boolean isTestOnBorrow() { return testOnBorrow; }
    public void setTestOnBorrow(boolean testOnBorrow) { this.testOnBorrow = testOnBorrow; }
    public boolean isTestOnReturn() { return testOnReturn; }
    public void setTestOnReturn(boolean testOnReturn) { this.testOnReturn = testOnReturn; }

    @Override
    public String toString() {
      return "DataSourceProperties(enabled=" + enabled + ", url=" + url +
          ", username=" + username + ", driverClassName=" + driverClassName +
          ", initialSize=" + initialSize + ", minIdle=" + minIdle +
          ", maxActive=" + maxActive + ", maxWait=" + maxWait + ")";
    }
  }
}
