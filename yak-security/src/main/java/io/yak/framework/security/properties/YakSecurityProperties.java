package io.yak.framework.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Configuration for the Yak security starter. */
@ConfigurationProperties("yak.security")
public class YakSecurityProperties {
  private boolean enabled = true;
  private boolean databaseEnabled = true;
  private boolean webEnabled = true;
  private boolean auditEnabled = true;
  private String appName = "default";
  private String username;
  private String password;
  private String jdbcUrl;
  private String driverClassName;

  public boolean isEnabled() { return enabled; }
  public void setEnabled(boolean enabled) { this.enabled = enabled; }
  public boolean isDatabaseEnabled() { return databaseEnabled; }
  public void setDatabaseEnabled(boolean value) { databaseEnabled = value; }
  public boolean isWebEnabled() { return webEnabled; }
  public void setWebEnabled(boolean value) { webEnabled = value; }
  public boolean isAuditEnabled() { return auditEnabled; }
  public void setAuditEnabled(boolean value) { auditEnabled = value; }
  public String getAppName() { return appName; }
  public void setAppName(String value) { appName = value; }
  public String getUsername() { return username; }
  public void setUsername(String value) { username = value; }
  public String getPassword() { return password; }
  public void setPassword(String value) { password = value; }
  public String getJdbcUrl() { return jdbcUrl; }
  public void setJdbcUrl(String value) { jdbcUrl = value; }
  public String getDriverClassName() { return driverClassName; }
  public void setDriverClassName(String value) { driverClassName = value; }
}
