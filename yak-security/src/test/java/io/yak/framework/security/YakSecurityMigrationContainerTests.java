package io.yak.framework.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/** Exercises the production MariaDB dialect rather than an H2 approximation. */
@Testcontainers(disabledWithoutDocker = true)
class YakSecurityMigrationContainerTests {
  @Container
  static final MariaDBContainer<?> DATABASE =
      new MariaDBContainer<>("mariadb:10.11");

  @BeforeAll
  static void migrate() {
    Flyway.configure().dataSource(DATABASE.getJdbcUrl(), DATABASE.getUsername(),
        DATABASE.getPassword()).locations("classpath:db/migration").load().migrate();
  }

  @Test
  void migrationCreatesOnlyCoreTablesAndSeedContainsNoUser() throws Exception {
    assertEquals(14, scalar("SELECT COUNT(*) FROM information_schema.tables "
        + "WHERE table_schema=DATABASE() AND table_name LIKE 'yak_security_%'"));
    assertEquals(0, scalar("SELECT COUNT(*) FROM yak_security_user"));
    assertEquals(4, scalar("SELECT COUNT(*) FROM yak_security_permission"));
  }

  @Test
  void uniqueKeysRelationsPermissionsSoftDeleteAndIsolationWork() throws Exception {
    execute("INSERT INTO yak_security_user(user_name,pw,app_name) VALUES "
        + "('alice','$2a$10$not-a-clear-text-password','app-a'),"
        + "('alice','$2a$10$not-a-clear-text-password','app-b')");
    assertThrows(SQLException.class, () -> execute(
        "INSERT INTO yak_security_user(user_name,pw,app_name) "
            + "VALUES ('alice','hash','app-a')"));

    execute("INSERT INTO yak_security_role(role_code,role_name,app_name) "
        + "VALUES ('reader','Reader','app-a')");
    execute("INSERT INTO yak_security_permission(permission_code,permission_name,parent_id,leaf,level,app_name) "
        + "VALUES ('document:read','Read documents',0,1,1,'app-a')");
    long user = id("yak_security_user", "user_name='alice' AND app_name='app-a'");
    long role = id("yak_security_role", "role_code='reader' AND app_name='app-a'");
    long permission = id("yak_security_permission",
        "permission_code='document:read' AND app_name='app-a'");
    execute("INSERT INTO yak_security_user_role(user_id,role_id,app_name) VALUES ("
        + user + "," + role + ",'app-a')");
    execute("INSERT INTO yak_security_role_permission(role_id,permission_id,app_name) VALUES ("
        + role + "," + permission + ",'app-a')");
    assertEquals(1, scalar("SELECT COUNT(*) FROM yak_security_permission p "
        + "JOIN yak_security_role_permission rp ON rp.permission_id=p.id "
        + "AND rp.app_name=p.app_name AND rp.is_delete=0 "
        + "JOIN yak_security_user_role ur ON ur.role_id=rp.role_id "
        + "AND ur.app_name=rp.app_name AND ur.is_delete=0 "
        + "WHERE ur.user_id=" + user + " AND p.app_name='app-a' AND p.is_delete=0"));
    assertEquals(0, scalar("SELECT COUNT(*) FROM yak_security_user "
        + "WHERE app_name='app-b' AND id=" + user));

    execute("UPDATE yak_security_user SET is_delete=1 WHERE id=" + user
        + " AND app_name='app-a'");
    execute("UPDATE yak_security_user_role SET is_delete=1 WHERE user_id=" + user
        + " AND app_name='app-a'");
    execute("INSERT INTO yak_security_user(user_name,pw,app_name) "
        + "VALUES ('alice','new-hash','app-a')");
    assertEquals(1, scalar("SELECT COUNT(*) FROM yak_security_user "
        + "WHERE user_name='alice' AND app_name='app-a' AND is_delete=0"));
    assertEquals(0, scalar("SELECT COUNT(*) FROM yak_security_user_role "
        + "WHERE user_id=" + user + " AND app_name='app-a' AND is_delete=0"));
  }

  private static void execute(String sql) throws SQLException {
    try (Connection connection = DATABASE.createConnection("");
         Statement statement = connection.createStatement()) {
      statement.executeUpdate(sql);
    }
  }

  private static long scalar(String sql) throws SQLException {
    try (Connection connection = DATABASE.createConnection("");
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet result = statement.executeQuery()) {
      result.next();
      return result.getLong(1);
    }
  }

  private static long id(String table, String condition) throws SQLException {
    return scalar("SELECT id FROM " + table + " WHERE " + condition);
  }
}
