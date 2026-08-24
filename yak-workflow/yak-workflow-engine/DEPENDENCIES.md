# Yak Workflow Engine Dependencies

本文件定义 Engine package 的长期依赖方向。目标：public API 稳定、internal runtime 显式、Core 无框架依赖。

## 1. Main Direction

```text
api
 -> runtime / recovery

runtime
 -> command / definition / event / execution / graph / policy / scheduler / spi / state / support

recovery
 -> definition / execution / graph / scheduler / spi / state

scheduler
 -> definition / execution / graph / policy / state

policy
 -> definition / execution / state

graph
 -> definition

execution
 -> definition / state

support
 -> command / definition / execution / spi
```

内部低层 package 不反向依赖 `api`、`runtime` 或 `recovery`。

## 2. API Corridor

Host-facing API 只允许：

- `WorkflowEngine` 暴露 command / definition / execution 值；
- `DefaultWorkflowEngine` 委托 `DefaultWorkflowRuntime`；
- `WorkflowRecoveryCoordinator` 委托 `WorkflowRecoveryRuntime`。

`api` 不直接 new Scheduler / Policy / StateMachine。

## 3. Runtime -> SPI

Runtime 只能通过 SPI 触碰宿主能力：

```text
ExecutionMailbox
ExecutionRepository
WorkflowDefinitionRepository
NodeExecutor
ExecutionLock
IdGenerator
WorkflowEventListener
```

不得引入 Spring、JDBC、MyBatis、Quartz、HTTP client 等宿主技术。

## 4. Command Corridor

已有 Execution 的 command 必须：

```text
WorkflowEngine.submit
 -> ExecutionMailbox.submit
 -> runtime handler
```

不要让 Scheduler、Policy、Executor、Repository 自己接受 public control command。

## 5. Recovery Corridor

Recovery 可以直接使用 `ExecutionLock`，因为它是启动期对持久化状态的显式 reconciliation；normal command path 仍以 Mailbox 为串行化边界。

Recovery 不依赖 `api.DefaultWorkflowEngine`，避免 public facade 与 internal lifecycle 形成反向环。

## 6. Core Purity

以下 package 必须保持 framework-free：

```text
definition
execution
graph
policy
scheduler
state
command
runtime
recovery
spi
event
```

禁止依赖 Spring / Jakarta Persistence / MyBatis。

## 7. Broad Bucket Rule

Engine production 不新增：

```text
service/
helper/
helpers/
utils/
util/
common/
base/
```

Framework 现有 `support/` 仅容纳明确的 default local / in-memory SPI implementation。

## 8. Adding a Dependency

出现新的跨 package import 时，先判断它是：

1. public API contract；
2. runtime orchestration；
3. deterministic policy / scheduler / graph logic；
4. host capability SPI；
5. default SPI implementation。

只有架构真的变化时才同步修改本文件和 architecture tests。
