package com.yak.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "yak.security")
public class YakSecurityProperties {
  private boolean enabled = true;
  private boolean databaseEnabled = true;
  private boolean webEnabled = true;
  private boolean auditEnabled = true;
  private String tokenHeader = "X-Yak-Token";
  private long tokenTtlSeconds = 7200;
  private int passwordIterations = 120000;

  public boolean isEnabled() { return enabled; }
  public void setEnabled(boolean enabled) { this.enabled = enabled; }
  public boolean isDatabaseEnabled() { return databaseEnabled; }
  public void setDatabaseEnabled(boolean databaseEnabled) {
    this.databaseEnabled = databaseEnabled;
  }
  public boolean isWebEnabled() { return webEnabled; }
  public void setWebEnabled(boolean webEnabled) { this.webEnabled = webEnabled; }
  public boolean isAuditEnabled() { return auditEnabled; }
  public void setAuditEnabled(boolean auditEnabled) {
    this.auditEnabled = auditEnabled;
  }
  public String getTokenHeader() { return tokenHeader; }
  public void setTokenHeader(String tokenHeader) {
    this.tokenHeader = tokenHeader;
  }
  public long getTokenTtlSeconds() { return tokenTtlSeconds; }
  public void setTokenTtlSeconds(long tokenTtlSeconds) {
    this.tokenTtlSeconds = tokenTtlSeconds;
  }
  public int getPasswordIterations() { return passwordIterations; }
  public void setPasswordIterations(int passwordIterations) {
    this.passwordIterations = passwordIterations;
  }
}
