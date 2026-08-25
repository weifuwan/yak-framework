package io.yak.framework.security.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class YakSecurityPropertiesTest {

  @Test
  void shouldUseMysqlConnectorJByDefault() {
    YakSecurityProperties properties = new YakSecurityProperties();
    assertThat(properties.getDatasource().getDriverClassName())
            .isEqualTo("com.mysql.cj.jdbc.Driver");
  }

  @Test
  void shouldUseSaTokenIdleTimeoutAndMemoryStorageByDefault() {
    YakSecurityProperties properties = new YakSecurityProperties();
    assertThat(properties.getAuthentication().getIdleTimeout())
            .isEqualTo(Duration.ofMinutes(30));
    assertThat(properties.getAuthentication().getStorage())
            .isEqualTo(YakSecurityProperties.AuthenticationStorage.MEMORY);
    assertThat(properties.getAuthentication().getRedis().getHost())
            .isEqualTo("127.0.0.1");
    assertThat(properties.getAuthentication().getRedis().getPort())
            .isEqualTo(6379);
  }
}
