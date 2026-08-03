# Yak Schedule

Yak Schedule 是 Yak Framework 的统一调度能力。核心模块只定义调度模型、业务 Handler 和调度引擎 SPI，Quartz、XXL-JOB 等实现作为独立插件自动注册。

## 模块结构

```text
yak-schedule
├── yak-schedule-api                 稳定领域模型、ScheduleManager、ScheduleEngine SPI
├── yak-schedule-core                引擎注册与路由、Handler 分发、默认内存仓库
├── yak-schedule-plugin-quartz       Quartz 插件
├── yak-schedule-plugin-xxl-job      XXL-JOB 3.4 插件与 Executor 桥接
├── yak-schedule-plugin-all          聚合全部内置插件
└── yak-schedule-spring-boot-starter Core + Plugin All
```

业务应用通常只需要依赖：

```xml
<dependency>
    <groupId>io.yak.framework</groupId>
    <artifactId>yak-schedule-spring-boot-starter</artifactId>
</dependency>
```

`plugin-all` 会把 Quartz 和 XXL-JOB 插件都放入 classpath。每个插件通过自己的
`AutoConfiguration.imports` 注册 `ScheduleEngine` Bean，核心层自动收集插件，并根据
`yak.schedule.engine` 选择当前实现。

也可以不使用 `plugin-all`，只引入某一个具体插件：

```xml
<dependency>
    <groupId>io.yak.framework</groupId>
    <artifactId>yak-schedule-core</artifactId>
</dependency>
<dependency>
    <groupId>io.yak.framework</groupId>
    <artifactId>yak-schedule-plugin-quartz</artifactId>
</dependency>
```

## Quartz 配置

```yaml
spring:
  quartz:
    job-store-type: memory # 生产环境建议 jdbc

yak:
  schedule:
    enabled: true
    engine: quartz
```

Quartz 插件支持：

- Cron；
- 一次性触发；
- 独立时区；
- 暂停、恢复、删除和立即执行；
- `ALLOW`、`FORBID` 并发策略；
- `IGNORE`、`FIRE_ONCE_NOW` Misfire 策略。

## XXL-JOB 配置

XXL-JOB 插件针对官方 3.4.0：

```yaml
yak:
  schedule:
    enabled: true
    engine: xxl-job
    xxl-job:
      admin-address: http://127.0.0.1:8080/xxl-job-admin
      username: admin
      password: 123456
      job-group-id: 1
      author: yak-framework
      executor:
        enabled: true
        app-name: yak-schedule-executor
        port: 9999
        access-token: change-me
        log-path: ./logs/xxl-job
        log-retention-days: 30
```

XXL-JOB 插件会：

1. 启动并注册 `XxlJobSpringExecutor`；
2. 注册唯一通用 Handler：`yakScheduleHandler`；
3. 通过 Admin 管理端创建、更新、暂停、恢复、删除和立即触发任务；
4. 将 XXL-JOB 的触发参数还原成 `ScheduleExecutionContext`；
5. 调用业务应用注册的 `ScheduleHandler`。

XXL-JOB 当前插件支持 Cron、Misfire、暂停/恢复和立即执行；不支持每个任务单独设置时区、一次性计划以及 `ALLOW` 并发策略。保存不支持的定义时，核心层会直接抛出 `UnsupportedScheduleCapabilityException`。

> XXL-JOB 官方目前没有稳定的任务管理 OpenAPI。默认 `XxlJobHttpAdminClient` 对接官方 3.4 管理端表单接口并维护登录 Cookie。若使用定制版本、统一认证或网关，应声明自己的 `XxlJobAdminClient` Bean 覆盖默认实现。

> `xxl-job-core` 使用 GPL-3.0 许可证。发行产品前应根据实际分发方式完成许可证评估。

## 注册业务 Handler

离线同步：

```java
@Bean("offlineSyncScheduleHandler")
ScheduleHandler offlineSyncScheduleHandler(
        OfflineJobExecutionService executionService) {
    return context -> {
        Long definitionId = context.requiredLong("definitionId");
        OfflineJobExecution execution = executionService.executeScheduled(
                definitionId,
                context.triggerId());
        return ScheduleExecutionResult.accepted(
                execution.getId().toString());
    };
}
```

工作流：

```java
@Bean("workflowScheduleHandler")
ScheduleHandler workflowScheduleHandler(
        WorkflowExecutionService executionService) {
    return context -> {
        Long definitionId = context.requiredLong("workflowDefinitionId");
        WorkflowInstance instance = executionService.startScheduled(
                definitionId,
                context.triggerId());
        return ScheduleExecutionResult.accepted(
                instance.getId().toString());
    };
}
```

## 创建调度计划

```java
ScheduleDefinition definition = new ScheduleDefinition(
        new ScheduleKey("workflow", "daily-report"),
        "每日经营报表",
        ScheduleTrigger.cron(
                "0 0 2 * * ?",
                ZoneId.of("Asia/Shanghai")),
        new ScheduleTarget(
                "workflowScheduleHandler",
                Map.of("workflowDefinitionId", 10001L)),
        new SchedulePolicy(
                ConcurrencyPolicy.FORBID,
                MisfirePolicy.FIRE_ONCE_NOW,
                1),
        true,
        Map.of());

scheduleManager.save(definition);
```

切换引擎时业务代码不变，只修改：

```yaml
yak:
  schedule:
    engine: quartz
```

或：

```yaml
yak:
  schedule:
    engine: xxl-job
```

## 设计边界

Yak Schedule 负责：

- 计划定义；
- 时间触发；
- 引擎选择；
- Handler 分发；
- 调度操作审计；
- 调度入口调用日志。

业务模块继续负责：

- 离线同步或工作流执行实例；
- 业务状态机；
- 业务幂等；
- 业务失败重试；
- Worker 或 Executor 资源选择；
- 状态对账、指标和告警。

默认定义、引擎绑定、日志和审计仓库是内存实现。生产环境应覆盖：

- `ScheduleDefinitionRepository`；
- `ScheduleEngineBindingRepository`；
- `ScheduleExecutionLogRepository`；
- `ScheduleOperationAuditRepository`。

第三方调度引擎只需：

1. 依赖 `yak-schedule-api` 或 `yak-schedule-core`；
2. 实现 `ScheduleEngine`；
3. 声明独立 `AutoConfiguration`；
4. 在 `AutoConfiguration.imports` 中注册；
5. 由核心 `ScheduleEngineRegistry` 自动发现。
