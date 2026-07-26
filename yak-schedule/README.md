# yak-schedule

基于 Spring Boot Quartz 自动配置的多项目调度模块。Quartz `group` 使用项目标识，因此不同项目可以使用相同的任务名。

## 能力

- 完整任务定义和 Cron/时区管理；
- 暂停、恢复、删除和立即执行；
- `ALLOW`/`FORBID` 并发策略；
- 每次失败立即重试，最多执行 `1 + maxRetries` 次；
- 执行结果日志及保存、暂停、恢复、立即执行、删除等操作审计；
- 可替换的当前操作人、日志仓库和审计仓库 SPI；
- 自动接入 Spring Boot Quartz，支持其内存或 JDBC JobStore 配置。

## 使用

应用提供一个具名处理器：

```java
@Bean("billing")
ScheduleTaskHandler billing(){
        return parameters->billingService.settle(parameters.get("tenant"));
        }
```

通过 `ScheduleTaskService.save(...)` 注册任务，或调用
`PUT /yak-schedule/api/v1/projects/{project}/tasks/{taskName}`。应用应实现
`CurrentOperatorProvider` 连接自身登录上下文；默认操作人为 `SYSTEM`。

生产环境建议实现 `ScheduleExecutionLogRepository` 与
`ScheduleOperationAuditRepository` 持久化记录，并通过标准的
`spring.quartz.job-store-type=jdbc` 配置 Quartz 集群存储。
