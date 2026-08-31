package io.yak.framework.security.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** 锁定消息中心扩展字段，避免模型升级后 Flyway 漏列。 */
class MessageCenterMigrationTest {

  @Test
  void migrationContainsMessageCenterColumnsAndIndexes() throws IOException {
    try (InputStream input = getClass().getResourceAsStream(
            "/yak-security/db/migration/V3__evolve_message_center.sql")) {
      assertNotNull(input);
      String sql = new String(input.readAllBytes(), StandardCharsets.UTF_8);
      assertTrue(sql.contains("message_type"));
      assertTrue(sql.contains("message_level"));
      assertTrue(sql.contains("message_scope"));
      assertTrue(sql.contains("project_id"));
      assertTrue(sql.contains("source_type"));
      assertTrue(sql.contains("action_path"));
      assertTrue(sql.contains("read_time"));
      assertTrue(sql.contains("idx_message_app_user_project"));
    }
  }
}
