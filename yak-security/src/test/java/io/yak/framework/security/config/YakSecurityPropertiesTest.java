package io.yak.framework.security.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class YakSecurityPropertiesTest {

  @Test
  void shouldUseMysqlConnectorJByDefault() {
    YakSecurityProperties properties = new YakSecurityProperties();

    assertThat(properties.getDatasource().getDriverClassName())
            .isEqualTo("com.mysql.cj.jdbc.Driver");
  }

  @Test
  void shouldKeepLegacySessionAuthenticationAsDefault() {
    YakSecurityProperties properties = new YakSecurityProperties();

    assertThat(properties.getAuthentication().getMode())
            .isEqualTo(
                    YakSecurityProperties.AuthenticationMode.SESSION);
  }

  @Test
  void shouldKeepLoginStateInMemoryUnlessRedisIsExplicitlyEnabled() {
    YakSecurityProperties properties = new YakSecurityProperties();

    assertThat(properties.getAuthentication().getStorage())
            .isEqualTo(
                    YakSecurityProperties.AuthenticationStorage.MEMORY);
    assertThat(properties.getAuthentication().getRedis().getHost())
            .isEqualTo("127.0.0.1");
    assertThat(properties.getAuthentication().getRedis().getPort())
            .isEqualTo(6379);
    assertThat(properties.getAuthentication().getRedis().getDatabase())
            .isZero();
    assertThat(properties.getAuthentication().getRedis().getMaxTotal())
            .isEqualTo(64);
  }
}
