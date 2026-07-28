package io.yak.framework.security.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class PermissionMenuMigrationTest {

  @Test
  void migrationLinksSystemActionsToMenusWithoutBusinessCatalog() throws Exception {
    ClassPathResource resource = new ClassPathResource(
        "yak-security/db/migration/V1200__link_permissions_to_menus.sql");
    String sql = resource.getContentAsString(StandardCharsets.UTF_8);

    assertThat(sql)
        .contains("ADD COLUMN menu_code")
        .contains("SET menu_code='system-users'")
        .contains("permission_code LIKE 'security:user:%'")
        .contains("SET menu_code='system-roles'")
        .contains("SET menu_code='system-permissions'")
        .contains("INSERT IGNORE INTO yak_security_role_menu")
        .contains("permission_row.menu_code")
        .doesNotContain("task:batch")
        .doesNotContain("workflow:definition")
        .doesNotContain("resource:data-source")
        .doesNotContain("quality:rule");
  }
}
