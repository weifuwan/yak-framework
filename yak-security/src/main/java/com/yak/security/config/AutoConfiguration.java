/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.boot.autoconfigure.AutoConfigureAfter
 *  org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
 *  org.springframework.boot.context.properties.EnableConfigurationProperties
 *  org.springframework.context.annotation.ComponentScan
 *  org.springframework.context.annotation.Configuration
 */
package com.yak.security.config;

import com.yak.security.properties.LogiSecurityProper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(value="logiSecurityAutoConfiguration")
@EnableConfigurationProperties(value={LogiSecurityProper.class})
@AutoConfigureAfter(value={DataSourceAutoConfiguration.class})
@ComponentScan(basePackages={"com.didiglobal.logi.security"})
public class AutoConfiguration {
    private final LogiSecurityProper proper;

    public AutoConfiguration(LogiSecurityProper proper) {
        this.proper = proper;
    }

    public LogiSecurityProper getProper() {
        return this.proper;
    }
}

