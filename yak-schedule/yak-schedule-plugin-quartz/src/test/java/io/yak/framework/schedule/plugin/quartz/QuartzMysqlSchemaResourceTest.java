package io.yak.framework.schedule.plugin.quartz;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class QuartzMysqlSchemaResourceTest {

    private static final String SCHEMA_RESOURCE =
            "db/quartz/mysql/tables_mysql_innodb.sql";

    @Test
    void shouldPackageOfficialMysqlInnoDbSchema() {
        InputStream input = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(SCHEMA_RESOURCE);

        assertThat(input).as("Quartz MySQL schema resource").isNotNull();

        String sql;
        try (Scanner scanner = new Scanner(input, StandardCharsets.UTF_8.name())
                .useDelimiter("\\A")) {
            sql = scanner.hasNext() ? scanner.next() : "";
        }

        assertThat(sql)
                .contains("org.quartz.impl.jdbcjobstore.StdJDBCDelegate")
                .contains("CREATE TABLE QRTZ_JOB_DETAILS")
                .contains("CREATE TABLE QRTZ_TRIGGERS")
                .contains("CREATE TABLE QRTZ_CRON_TRIGGERS")
                .contains("CREATE TABLE QRTZ_SCHEDULER_STATE")
                .contains("CREATE TABLE QRTZ_LOCKS")
                .contains("ENGINE=InnoDB")
                .contains("DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS");
    }
}
