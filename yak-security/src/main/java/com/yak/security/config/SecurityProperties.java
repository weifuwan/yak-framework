package com.yak.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "yak.security")
public class SecurityProperties {
  private boolean enabled = true;
  private String tokenHeader = "X-Yak-Token";
  private long tokenTtlSeconds = 7200;
  private int passwordIterations = 120000;
  public boolean isEnabled() { return enabled; }
  public void setEnabled(boolean enabled) { this.enabled = enabled; }
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
