package io.yak.framework.security.config;

import io.yak.framework.security.properties.YakSecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "yakSecurityAutoConfiguration")
@EnableConfigurationProperties(value = {YakSecurityProperties.class})
@AutoConfigureAfter(value = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"io.yak.framework.security"})
public class AutoConfiguration {
  private final YakSecurityProperties proper;

  public AutoConfiguration(YakSecurityProperties proper) {
    this.proper = proper;
  }

  public YakSecurityProperties getProper() { return this.proper; }
}
