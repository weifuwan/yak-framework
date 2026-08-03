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
}
