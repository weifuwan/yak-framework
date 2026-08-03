# Yak Schedule Quartz Plugin

该插件提供 Yak Schedule 的 Quartz 调度引擎实现。

## MySQL JDBC JobStore

MySQL InnoDB 建表脚本位于：

```text
classpath:db/quartz/mysql/tables_mysql_innodb.sql
```

脚本同步自 Quartz 官方 `tables_mysql_innodb.sql`，使用 `QRTZ_` 表前缀，并要求配置 `StdJDBCDelegate`。

> 警告：官方脚本开头包含 `DROP TABLE IF EXISTS`。它只适合首次初始化或明确重建 Quartz 表，不能在生产环境每次启动时自动执行。

建议先人工或通过受控数据库迁移执行一次脚本，然后配置：

```yaml
spring:
  quartz:
    job-store-type: jdbc
    jdbc:
      initialize-schema: never
    properties:
      org.quartz.jobStore.driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
      org.quartz.jobStore.tablePrefix: QRTZ_

yak:
  schedule:
    enabled: true
    engine: quartz
```

开发或一次性初始化环境确实需要 Spring Boot 执行脚本时，可以临时指定：

```yaml
spring:
  quartz:
    jdbc:
      initialize-schema: always
      schema: classpath:db/quartz/mysql/tables_mysql_innodb.sql
```

初始化完成后应立即改回 `initialize-schema: never`，避免重启时清空调度数据。
