# Yak Workflow Engine

`yak-workflow-engine` 是一个不依赖 Spring、数据库和具体任务实现的纯 Java DAG 工作流内核。

## 功能列表

### DAG 与调度

- DAG 定义校验、环检测、串行、并行和汇聚执行
- `ALL_SUCCESS`、`ALL_DONE`、`NONE_FAILED`、`ONE_SUCCESS`、`ALWAYS` 触发规则
- 独立分支继续、失败分支阻断、失败快速结束等传播策略

### 执行与恢复

- 节点 Attempt 模型、自动重试和完整 Attempt 历史
- Attempt fencing 与 Callback 幂等，隔离旧 Attempt 的迟到回调
- 失败节点重新执行、跳过当前失败继续下游、失败节点批量重试
- 整条工作流重启、从指定节点重新运行、工作流取消

### 生命周期与超时

- `RUNNING -> PAUSING -> PAUSED -> RESUMING -> RUNNING` 暂停/恢复协议
- Attempt 级 Pause/Resume，保持同一个 `attemptId`
- Dispatch Timeout、Node Execution Timeout、Workflow Timeout
- 暂停期间冻结 Node / Workflow 超时计时

### 执行上下文与数据流

- Workflow Input、Node Configuration、Node Input
- 直接前置节点 Output 传递与并行汇聚输入
- `NodeInputMapping` 轻量输入映射
- Workflow Input / Node Output / Execution Context 递归不可变快照

### Command / Event

- `WorkflowCommand` 统一 Callback、Timeout 和人工控制入口
- `ExecutionMailbox` 按 `workflowExecutionId` 串行处理状态变更
- `WorkflowEvent` 输出节点和工作流生命周期事件
- 默认 `LocalExecutionMailbox`，同时保留可替换 SPI

### 扩展 SPI

- `NodeExecutor`
- `ExecutionRepository`
- `WorkflowDefinitionRepository`
- `ExecutionMailbox`
- `ExecutionLock`
- `IdGenerator`
- `WorkflowEventListener`

## 最小示例

```java
NodeExecutor executor = dispatch -> {
    // 将节点提交给本地线程池、远程 Worker 或任务队列。
    // 回调时必须带回 dispatch.attemptId()。
};

DefaultWorkflowEngine engine = DefaultWorkflowEngine.inMemory(executor);

WorkflowDefinition definition = new WorkflowDefinition(
        "example",
        "Example Workflow",
        WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES,
        List.of(
                NodeDefinition.task("extract"),
                NodeDefinition.task("transform")),
        List.of(new EdgeDefinition("extract", "transform")));

engine.registerDefinition(definition);
WorkflowExecution execution = engine.start("example", Map.of("requestId", "REQ-001"));
```

节点执行完成后使用当前 Attempt 回调：

```java
engine.acknowledgeNodeStarted(executionId, nodeId, attemptId);
engine.completeNode(executionId, nodeId, attemptId, output);
// 或 engine.failNode(executionId, nodeId, attemptId, errorMessage);
```

## 当前边界

当前内核只负责工作流执行语义，不绑定数据库、Spring、Master HA、资源队列或具体任务实现。持久化、远程 Worker、定时扫描和运行治理由宿主系统通过 SPI 集成。
