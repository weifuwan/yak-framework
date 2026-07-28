package io.yak.framework.security.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class MenuMigrationIsolationTest {

  @Test
  void menuSchemaMigrationMustNotContainApplicationCatalogData()
      throws Exception {

    ClassPathResource resource = new ClassPathResource(
        "yak-security/db/migration/V100__add_menu_role_authorization.sql");
    String sql = resource.getContentAsString(StandardCharsets.UTF_8);

    assertThat(sql)
        .contains("CREATE TABLE yak_security_menu")
        .contains("CREATE TABLE yak_security_role_menu")
        .doesNotContain("INSERT INTO yak_security_menu")
        .doesNotContain("task:batch:read")
        .doesNotContain("workflow:definition:read")
        .doesNotContain("knowledge-management");
  }
}
