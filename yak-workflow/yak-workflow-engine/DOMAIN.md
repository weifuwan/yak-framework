# Yak Workflow Engine Domain

本文件定义 `yak-workflow-engine` 的长期领域事实、identity 和 state ownership。

## 1. Truth Ownership

工作流内核只有一套运行真相：

```text
WorkflowDefinition  = executable DAG definition
WorkflowExecution   = one workflow run truth
NodeExecution       = one node inside that run
NodeAttempt         = one physical attempt of that node
```

`DefaultWorkflowEngine` 是 public facade，不拥有第二份状态；`DefaultWorkflowRuntime` 负责协调这些领域对象的迁移。

## 2. Identity Rules

- `WorkflowExecution.id`：一次运行身份；
- `NodeExecution.id`：一次运行内节点实例身份；
- `NodeAttempt.id`：一次实际尝试身份；
- callback 必须携带当前 attemptId；
- stale attempt callback 不能修改当前状态。

## 3. Retry vs New Run

```text
retry failed node
    -> same WorkflowExecution
    -> same NodeExecution
    -> new NodeAttempt

restart / rerun-from-node
    -> new WorkflowExecution
    -> sourceExecutionId points to old run
```

这条边界与宿主系统的业务执行元数据必须保持一致。

## 4. State Machines

`WorkflowStateMachine` 与 `NodeStateMachine` 是合法迁移规则来源。Runtime 可以编排迁移，但不能通过直接字段修改制造另一套隐式状态机。

Execution terminal、pause lifecycle、node terminal、attempt terminal 的判断以 typed state 为准。

## 5. Definition vs Execution Snapshot

Execution 创建后绑定 `definitionId`。宿主如果需要历史不可变语义，必须让对应的 `WorkflowDefinitionRepository` 以该 id 返回不可变定义快照。

Engine 不回读宿主“当前业务草稿”替换一个已经创建的 Execution 定义。

## 6. Command Truth

`WorkflowCommand` 是对已存在 Execution 的状态变化意图，不是持久化 truth。

```text
caller
 -> WorkflowCommand
 -> ExecutionMailbox
 -> DefaultWorkflowRuntime
 -> WorkflowExecution / NodeExecution / NodeAttempt
 -> ExecutionRepository
```

Mailbox 保证同一 executionId 的 command 串行化；Repository 保存状态快照；两者角色不能互换。

## 7. Node Executor Boundary

`NodeExecutor` 是外部执行边界：

- `submit` 接受 `NodeDispatch`；
- `pause / resume / cancel / recover` 返回或消费外部控制证据；
- Executor 不直接修改 Engine 内领域对象；
- 外部执行完成必须通过 Engine callback command 回来。

## 8. Recovery Truth

Recovery 不猜外部状态，也不重新生成已有 Attempt id。

已持久化 Attempt 的 `NodeRecovery` 携带原 `NodeDispatch` identity 和持久化状态，外部 Executor 决定如何重新挂接；Engine 仍以 Repository 中 Execution truth 为准。

## 9. Event Truth

`WorkflowEvent` 是生命周期通知，不是状态 truth。Event listener 失败或丢失不应反向创造另一份 WorkflowExecution 状态机。

## 10. Domain Gap Rule

出现以下需求时先定义新领域 contract，而不是往 Runtime 塞隐藏 flag：

- 一个 NodeExecution 同时存在多个 active Attempt；
- 同一 Execution 允许并行处理相互冲突的 Command；
- restart 复用旧 WorkflowExecution id；
- recovery 用新的 attemptId 替换持久化 attempt；
- 外部 Executor 直接决定 WorkflowExecution final state；
- Definition 在运行中动态漂移到另一个版本。
