package io.yak.framework.schedule;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("yak.schedule")
public class YakScheduleProperties {
  private boolean enabled = true;
  private boolean webEnabled = true;
  private int logCapacity = 10_000;
  public boolean isEnabled() { return enabled; }
  public void setEnabled(boolean enabled) { this.enabled = enabled; }
  public boolean isWebEnabled() { return webEnabled; }
  public void setWebEnabled(boolean webEnabled) { this.webEnabled = webEnabled; }
  public int getLogCapacity() { return logCapacity; }
  public void setLogCapacity(int logCapacity) { this.logCapacity = logCapacity; }
}
