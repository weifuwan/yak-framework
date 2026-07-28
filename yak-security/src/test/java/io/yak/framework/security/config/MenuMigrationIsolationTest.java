package io.yak.framework.security.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class MenuMigrationIsolationTest {

  @Test
  void consolidatedBaselineMustContainFinalYakSecuritySchemaAndCatalog()
      throws Exception {

    String sql = readMigration(
        "yak-security/db/migration/V1__init_yak_security.sql");

    assertThat(sql)
        .contains("CREATE TABLE yak_security_permission")
        .contains("active TINYINT(1) NOT NULL DEFAULT 1")
        .contains("declared TINYINT(1) NOT NULL DEFAULT 0")
        .contains("CREATE TABLE yak_security_user_project")
        .contains("user_type TINYINT NOT NULL DEFAULT 0")
        .contains("CREATE TABLE yak_security_menu")
        .contains("CREATE TABLE yak_security_role_menu")
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
        .doesNotContain("ALTER TABLE yak_security_permission")
        .doesNotContain("ALTER TABLE yak_security_user_project")
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
