package io.yak.framework.security.config;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class DataSourceConfigTest {

    @Test
    void flywayUsesModuleSpecificMigrationLocation() {
        YakSecurityProperties properties = new YakSecurityProperties();
        properties.setApplicationName("test-app");

        Flyway flyway = new DataSourceConfig().yakSecurityFlyway(
                mock(DataSource.class), properties);

        assertThat(flyway.getConfiguration().getLocations())
                .extracting(Object::toString)
                .containsExactly(DataSourceConfig.FLYWAY_MIGRATION_LOCATION)
                .doesNotContain("classpath:db/migration");
    }
}
