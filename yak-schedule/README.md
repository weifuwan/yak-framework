# yak-schedule

Yak Schedule 是面向业务系统的统一任务触发框架。业务模块只依赖稳定的
`ScheduleManager`、`ScheduleDefinition` 和 `ScheduleHandler`，Quartz、XXL-JOB
等具体实现通过 `ScheduleEngine` SPI 接入。

## 设计边界

Yak Schedule 负责：

- Cron 和一次性时间触发；
- 调度计划创建、更新、暂停、恢复、删除和立即运行；
- 调度引擎路由和能力校验；
- 调度触发上下文、触发日志和操作审计；
- 同一业务标识与外部调度引擎之间的绑定。

业务模块负责：

- 创建离线同步、工作流、质量检测等业务执行实例；
- 业务执行状态机、幂等、重试、超时和失败恢复；
- Worker、Executor 或资源节点选择；
- 业务日志、指标和告警。

调度成功表示业务 Handler 已接受本次触发，不表示离线任务或工作流已经最终成功。

## 分层

```text
io.yak.framework.schedule
├── api                  稳定业务 API、Handler 和异常
│   └── model            Provider 无关的调度领域模型
├── core                 引擎注册、路由、绑定和执行分发
├── provider.quartz      Quartz Provider
└── ScheduleTaskService  旧 Cron API 兼容门面
```

当前仍保持单个 starter，先稳定包边界和 API。后续可以不改变业务接口地拆成：

```text
yak-schedule-api
yak-schedule-core
yak-schedule-provider-quartz
yak-schedule-provider-xxl-job
yak-schedule-spring-boot-starter
```

## 配置

```yaml
yak:
  schedule:
    enabled: true
    web-enabled: false
    default-engine: quartz
    log-capacity: 10000

spring:
  quartz:
    auto-startup: true
    job-store-type: memory
```

生产环境应将 Quartz 配置为 JDBC JobStore，并替换默认的内存日志、审计和
引擎绑定仓库。

## 新 API 使用方式

### 1. 注册业务 Handler

```java
@Bean("offlineSyncScheduleHandler")
ScheduleHandler offlineSyncScheduleHandler(
        OfflineJobExecutionService executionService) {

    return context -> {
        Long definitionId =
                context.getRequiredLong("definitionId");

        OfflineJobExecutionPO execution =
                executionService.executeScheduled(
                        definitionId,
                        context.getTriggerId());

        return ScheduleExecutionResult.accepted(
                execution.getId().toString());
    };
}
```

Handler 应尽快创建并持久化业务执行实例，然后返回业务执行 ID。不要在 Quartz
线程中等待整个离线同步或工作流执行完成。

### 2. 保存调度计划

```java
ScheduleDefinition definition =
        new ScheduleDefinition(
                new ScheduleKey("offline-sync", "job-10001"),
                "订单离线同步",
                "每天凌晨同步订单数据",
                null,
                ScheduleTrigger.cron(
                        "0 0 2 * * ?",
                        ZoneId.of("Asia/Shanghai")),
                new ScheduleTarget(
                        "offlineSyncScheduleHandler",
                        Map.of("definitionId", "10001")),
                new SchedulePolicy(
                        ConcurrencyPolicy.FORBID,
                        MisfirePolicy.FIRE_ONCE_NOW,
                        1),
                true,
                3L,
                Map.of("businessType", "offline-sync"));

scheduleManager.save(definition);
```

`engineType` 为空时使用 `yak.schedule.default-engine`。未来接入 XXL-JOB 后，
可以在定义中传入 `xxl-job`，核心层会先检查 Provider 能力。

### 3. 生命周期控制

```java
ScheduleKey key =
        new ScheduleKey("offline-sync", "job-10001");

scheduleManager.pause(key);
scheduleManager.resume(key);
scheduleManager.runNow(key);
scheduleManager.delete(key);
```

## Yak Ops 接入建议

不同业务使用独立 namespace 和 Handler：

```text
offline-sync -> offlineSyncScheduleHandler
workflow     -> workflowScheduleHandler
quality      -> qualityCheckScheduleHandler
maintenance  -> maintenanceScheduleHandler
```

工作流接入示例：

```java
@Bean("workflowScheduleHandler")
ScheduleHandler workflowScheduleHandler(
        WorkflowExecutionService executionService) {

    return context -> {
        Long definitionId =
                context.getRequiredLong("workflowDefinitionId");
        Long version =
                context.getRequiredLong("workflowVersion");

        WorkflowInstance instance =
                executionService.startScheduled(
                        definitionId,
                        version,
                        context.getTriggerId());

        return ScheduleExecutionResult.accepted(
                instance.getId().toString());
    };
}
```

`triggerId` 必须传入业务执行服务，并作为幂等键保存。调度器发生恢复或短暂重试时，
业务模块应保证同一个 `triggerId` 不会创建多个执行实例。

离线同步迁移时建议：

1. 保留现有 `yak_offline_schedule` 和扫描派发器；
2. 保存任务时同步创建 Yak Schedule 定义；
3. 对比两套触发记录和幂等行为；
4. 稳定后关闭旧的 `OfflineScheduleDispatcher`；
5. 业务重试、Worker 选择和状态对账继续保留在离线模块。

## 旧 API 兼容

原有 `ScheduleTaskService`、`ScheduleTaskDefinition` 和 `ScheduleTaskHandler`
继续可用，内部已经映射到新的 `ScheduleManager`。旧接口只支持 Cron，新业务应优先
使用新 API。

```java
@Bean("billing")
ScheduleTaskHandler billing() {
    return parameters ->
            billingService.settle(parameters.get("tenant"));
}
```

## 新 Provider 接入

实现 `ScheduleEngine` 并注册为 Spring Bean：

```java
@Component
class XxlJobScheduleEngine implements ScheduleEngine {

    @Override
    public String engineType() {
        return "xxl-job";
    }

    // capabilities、save、pause、resume、delete、runNow、get、list
}
```

Provider 内部负责把通用定义转换为外部调度系统模型，不得向业务层暴露
Quartz、XXL-JOB 等专用异常和类型。
