package io.yak.framework.security.autoconfigure;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

class SwaggerUiStartupLoggerTest {

  @Test
  void buildsDefaultSwaggerUiUrl() {
    assertThat(SwaggerUiStartupLogger.buildSwaggerUiUrl(
            new MockEnvironment(), "", 8080))
            .isEqualTo("http://localhost:8080/swagger-ui.html");
  }

  @Test
  void honorsServerAndSpringdocConfiguration() {
    MockEnvironment environment = new MockEnvironment()
            .withProperty("server.ssl.enabled", "true")
            .withProperty("server.address", "api.internal")
            .withProperty("springdoc.swagger-ui.path", "/docs");

    assertThat(SwaggerUiStartupLogger.buildSwaggerUiUrl(environment, "/service", 9443))
            .isEqualTo("https://api.internal:9443/service/docs");
  }

  @Test
  void usesLocalhostForWildcardAddressAndNormalizesPaths() {
    MockEnvironment environment = new MockEnvironment()
            .withProperty("server.address", "0.0.0.0")
            .withProperty("springdoc.swagger-ui.path", "docs");

    assertThat(SwaggerUiStartupLogger.buildSwaggerUiUrl(environment, "service", 8081))
            .isEqualTo("http://localhost:8081/service/docs");
  }
}
