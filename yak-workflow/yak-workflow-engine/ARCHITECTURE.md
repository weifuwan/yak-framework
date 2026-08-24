# Yak Workflow Engine Architecture

本文件描述 `yak-workflow-engine` 的长期代码组织与依赖边界。

## 1. Principle

`yak-workflow-engine` 是纯 Java DAG runtime。组织方式采用大型数据系统常见的 **stable public API + explicit runtime subsystems**：public API 负责兼容，内部 package 按 Definition / Execution / Runtime / Recovery / Scheduler / Policy / SPI 等真实角色组织。

不是为了“少几个 Service”，而是让 package 本身就能回答：谁拥有运行真相、谁负责状态迁移、谁只是决策、谁是宿主能力边界。

## 2. Package Map

```text
io.yak.framework.workflow.engine
├── api          # stable host-facing facade only
├── runtime      # normal command/runtime orchestration
├── recovery     # persisted execution restart reconciliation
├── command      # immutable commands
├── definition   # DAG definition values
├── execution    # Workflow/Node/Attempt runtime domain
├── graph        # validation and graph topology
├── scheduler    # ready/completion resolution
├── policy       # trigger/retry/failure policies
├── state        # typed state + legal transition rules
├── spi          # host-owned capability ports
├── event        # lifecycle event contract
└── support      # local/in-memory default SPI implementations
```

`support` 在 Framework 中只表示明确的 local/in-memory SPI 实现，不是业务 Helper 大桶。

## 3. Public API Boundary

`api` 只保留稳定 host contract：

```text
WorkflowEngine
DefaultWorkflowEngine
WorkflowRecoveryCoordinator
```

`DefaultWorkflowEngine` 是薄 facade：

```text
Host
 -> DefaultWorkflowEngine / WorkflowEngine
 -> DefaultWorkflowRuntime
 -> domain + scheduler + policy + SPI
```

`WorkflowRecoveryCoordinator` 同样是兼容 facade：

```text
Host startup
 -> WorkflowRecoveryCoordinator
 -> WorkflowRecoveryRuntime
 -> persisted Execution + NodeExecutor.recover
```

API facade 不实现 scheduler、retry、timeout、pause/resume 等内部算法。

## 4. Normal Runtime Corridor

```text
WorkflowEngine convenience method
 -> WorkflowCommand
 -> ExecutionMailbox
 -> DefaultWorkflowRuntime
 -> WorkflowExecution / NodeExecution / NodeAttempt
 -> scheduler / policy
 -> ExecutionRepository
 -> NodeExecutor / WorkflowEventListener
```

`start` / restart / rerun 创建 Execution；对已有 Execution 的 callback/control 统一从 Mailbox 进入。

## 5. Recovery Corridor

Recovery 与 normal command runtime 分离，因为它发生在 host lifecycle，而不是某个现存 Execution 的普通用户命令。

```text
WorkflowRecoveryCoordinator
 -> WorkflowRecoveryRuntime
 -> ExecutionLock
 -> load persisted definition/execution
 -> NodeExecutor.recover(existing attempt)
 -> resume READY/WAITING scheduling
 -> save converged execution
```

Recovery 不通过 public convenience command 模拟启动恢复，也不为已有 Attempt 创建新 identity。

## 6. Scheduler and Policy

- Graph 只表达拓扑；
- Scheduler 只解析 Ready / completion；
- Policy 只表达 trigger / retry / failure decision；
- Runtime 负责把这些确定性决策应用到 Execution truth；
- NodeExecutor 是外部执行 port，不参与领域决策。

## 7. Persistence Boundary

Framework 不知道数据库。

```text
Runtime
 -> WorkflowDefinitionRepository / ExecutionRepository
 -> host adapter
 -> any persistence technology
```

默认 `support` package 提供 in-memory / local 实现，仅用于嵌入、测试和轻量运行。

## 8. Host Integration

宿主系统应该依赖：

- `api.*`：稳定调用入口；
- `spi.*`：宿主需要实现的能力；
- `definition / execution / state / command / event`：公开值对象和运行证据。

宿主不应依赖：

- `runtime.*`；
- `recovery.*` 的内部实现类；
- `scheduler / policy / graph` 的 concrete implementation。

## 9. Compatibility Rule

内部 Runtime / Recovery package 可以演进，但 `api`、SPI、Definition、Execution public contract 默认保持兼容。宿主只依赖稳定 facade / SPI，因此内部结构调整不应该迫使宿主同步大规模改包。

## 10. Change Rule

新增代码前回答：

1. 是 host-facing contract，还是 internal runtime implementation？
2. 它拥有哪个 truth，还是只做 deterministic decision？
3. 是否绕过 Mailbox 产生第二 command path？
4. 是否绕过 StateMachine 修改状态？
5. 是否把数据库 / Spring / 任务实现带进纯 Java Core？
6. 是否破坏 attempt fencing / retry identity / recovery identity？

答不清楚时不要创建新的 `Service / Helper / Utils / Common` 包。
