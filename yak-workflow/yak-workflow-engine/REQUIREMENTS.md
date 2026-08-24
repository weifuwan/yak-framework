# Yak Workflow Engine Requirements

本文件定义 `yak-workflow-engine` 当前必须保持的行为 contract。它描述纯 Java 工作流内核提供什么，不记录历史重构过程。

## 1. Definition and DAG

- `WorkflowDefinition` 是 Engine 注册和执行使用的 DAG 定义；
- Definition 必须经过节点唯一性、边合法性、环检测等校验；
- Engine 支持串行、并行、汇聚以及当前 `TriggerRule` 语义；
- Definition / Graph 不拥有宿主业务配置、数据库模型或 HTTP 模型。

## 2. Execution Truth

Engine 运行真相严格区分：

```text
WorkflowExecution
  -> NodeExecution
      -> NodeAttempt
```

- `WorkflowExecution` 表示一次工作流运行；
- `NodeExecution` 表示该运行中的一个节点；
- `NodeAttempt` 表示节点的一次实际尝试；
- retry failed node 创建新的 Attempt，但不创建新的 WorkflowExecution；
- restart / rerun-from-node 创建新的 WorkflowExecution，并通过 `sourceExecutionId` 保留来源血缘。

## 3. Command and Mailbox

- 已存在 Execution 的状态变化统一通过 immutable `WorkflowCommand` 进入；
- built-in engine 必须通过 `ExecutionMailbox` 按 executionId 串行化 command；
- stale / duplicate callback 必须依靠 attemptId fencing 成为幂等 no-op；
- convenience API 只负责把调用转换为 Command，不能产生另一套状态迁移路径。

## 4. Scheduling and Dispatch

- Scheduler 只决定哪些节点 Ready；
- Engine Runtime 负责为 Ready Node 创建 Attempt 并形成 `NodeDispatch`；
- `NodeExecutor` 负责外部执行，不拥有 WorkflowExecution 状态真相；
- dispatch 前必须先把 Attempt 状态交给 `ExecutionRepository` 持久化边界；
- Node input 由 Workflow input、直接前驱输出和 `NodeInputMapping` 确定性生成。

## 5. Failure and Retry

- 自动 retry 受 `RetryPolicy` / `RetryDecider` 控制；
- 最终失败传播受 `WorkflowFailureStrategy` / `FailurePropagationPolicy` 控制；
- failed / upstream-failed / skipped / canceled 等节点状态不能折叠成一个 boolean success；
- manual retry / continue-after-failure 不能绕过现有状态约束和 attempt fencing。

## 6. Pause and Resume

保持当前协议：

```text
RUNNING -> PAUSING -> PAUSED -> RESUMING -> RUNNING
```

- pause / resume 保持同一个 WorkflowExecution；
- executor 支持暂停时，Attempt pause / resume 保持同一个 attemptId；
- pause lifecycle 期间不得执行不兼容的 recovery command；
- 暂停期间的 timeout accounting 保持当前冻结语义。

## 7. Timeout

- Workflow Timeout、Dispatch Timeout、Node Execution Timeout 均由 Engine 状态语义决定；
- Engine 不自行启动 timer thread，宿主通过 `checkTimeouts` 驱动；
- timeout 必须保留 Attempt failure reason 和取消证据；
- timeout 与普通 executor failure 不能在领域中混为一个无类型错误。

## 8. Recovery

- Recovery 是显式宿主生命周期动作，不是普通 command；
- Recovery 只收敛已经持久化的 Execution / Attempt，不创建第二身份；
- 对 SUBMITTED / RUNNING / PAUSING / PAUSED / RESUMING Attempt 调用 `NodeExecutor.recover`；
- 对 READY / WAITING 节点按持久化状态继续调度；
- terminal Execution recovery 必须是幂等读取。

## 9. SPI Boundary

Engine 只通过 SPI 接触宿主能力：

- `WorkflowDefinitionRepository`
- `ExecutionRepository`
- `ExecutionMailbox`
- `ExecutionLock`
- `NodeExecutor`
- `IdGenerator`
- `WorkflowEventListener`

内核不依赖 Spring、数据库、MyBatis、HTTP、Quartz 或具体任务实现。

## 10. Public Compatibility

架构整理必须保持：

- `io.yak.framework.workflow.engine.api.WorkflowEngine`；
- `api.DefaultWorkflowEngine` 的现有 constructor / `inMemory` / public method；
- `api.WorkflowRecoveryCoordinator` 的现有 constructor / `recover`；
- Definition / Execution / SPI / Command 的现有 package 和 public contract；
- 现有状态枚举与行为测试语义。

需要 breaking API 时必须独立版本化，不应混进纯架构重构。
