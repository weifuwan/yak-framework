package io.yak.framework.security.autoconfigure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class YakSecurityOpenApiConfigurationTest {

  private final WebApplicationContextRunner contextRunner =
          new WebApplicationContextRunner()
                  .withConfiguration(AutoConfigurations.of(YakSecurityOpenApiConfiguration.class));

  @Test
  void providesDefaultOpenApiMetadata() {
    contextRunner.run(context -> {
      assertThat(context).hasSingleBean(OpenAPI.class);
      OpenAPI openAPI = context.getBean(OpenAPI.class);
      assertThat(openAPI.getInfo().getTitle()).isEqualTo("Yak Security API");
      assertThat(openAPI.getInfo().getLicense().getName()).isEqualTo("Apache License 2.0");
    });
  }

  @Test
  void backsOffWhenApplicationProvidesOpenApiBean() {
    contextRunner
            .withUserConfiguration(CustomOpenApiConfiguration.class)
            .run(context -> {
              assertThat(context).hasSingleBean(OpenAPI.class);
              assertThat(context.getBean(OpenAPI.class).getInfo().getTitle())
                      .isEqualTo("Custom API");
            });
  }

  @Test
  void canBeDisabledWithWebEndpoints() {
    contextRunner
            .withPropertyValues("yak.security.web-enabled=false")
            .run(context -> assertThat(context).doesNotHaveBean(OpenAPI.class));
  }

  @Configuration(proxyBeanMethods = false)
  static class CustomOpenApiConfiguration {
    @Bean
    OpenAPI customOpenAPI() {
      return new OpenAPI().info(new Info().title("Custom API"));
    }
  }
}
