package io.yak.framework.security.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class MenuMigrationIsolationTest {

  @Test
  void menuSchemaMigrationMustNotContainCatalogData()
      throws Exception {

    String sql = readMigration(
        "yak-security/db/migration/V100__add_menu_role_authorization.sql");

    assertThat(sql)
        .contains("CREATE TABLE yak_security_menu")
        .contains("CREATE TABLE yak_security_role_menu")
        .doesNotContain("INSERT INTO yak_security_menu");
  }

  @Test
  void systemCatalogMigrationMustOwnOnlyYakSecurityData()
      throws Exception {

    String sql = readMigration(
        "yak-security/db/migration/V1100__init_yak_security_system_catalog.sql");

    assertThat(sql)
        .contains("INSERT INTO yak_security_permission")
        .contains("INSERT INTO yak_security_menu")
        .contains("'security:root'")
        .contains("'security:user:create'")
        .contains("'security:user:reset-password'")
        .contains("'security:role:assign'")
        .contains("'security:permission:import'")
        .contains("'system-users'")
        .contains("'system-operation-logs'")
        .contains("INSERT IGNORE INTO yak_security_role_permission")
        .contains("INSERT IGNORE INTO yak_security_role_menu")
        .doesNotContain("'task:batch:read'")
        .doesNotContain("'workflow:definition:read'")
        .doesNotContain("'resource:data-source:read'")
        .doesNotContain("'knowledge-management'");
  }

  private static String readMigration(String path) throws Exception {
    ClassPathResource resource = new ClassPathResource(path);
    return resource.getContentAsString(StandardCharsets.UTF_8);
  }
}