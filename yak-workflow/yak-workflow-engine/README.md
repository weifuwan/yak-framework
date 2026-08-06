# Yak Workflow Engine

`yak-workflow-engine` 是一个不依赖 Spring、数据库和具体任务实现的纯 Java DAG 工作流内核。

## 已实现能力

- DAG 定义校验、环检测与拓扑排序
- 串行、并行和汇聚节点推进
- `ALL_SUCCESS`、`ALL_DONE`、`NONE_FAILED`、`ONE_SUCCESS`、`ALWAYS` 触发规则
- `FAIL_FAST`、`CONTINUE_INDEPENDENT_BRANCHES`、`TERMINATE_ALL` 工作流失败策略
- `FAIL_WORKFLOW`、`BLOCK_BRANCH`、`IGNORE_FAILURE` 节点失败策略
- 自动重试与完整 Attempt 历史
- 工作流取消、失败节点手动重试、整条重启、指定节点重跑
- 执行仓库、定义仓库、节点执行器、执行锁和 ID 生成器 SPI
- 默认内存仓库和本地执行锁，便于单元测试和嵌入式使用

## 最小示例

```java
NodeExecutor executor = dispatch -> {
    // 将节点提交给线程池、远程 Worker 或任务队列。
};

DefaultWorkflowEngine engine = DefaultWorkflowEngine.inMemory(executor);

WorkflowDefinition definition = new WorkflowDefinition(
        "example",
        "Example Workflow",
        WorkflowFailureStrategy.CONTINUE_INDEPENDENT_BRANCHES,
        List.of(
                NodeDefinition.task("extract"),
                NodeDefinition.task("transform"),
                NodeDefinition.task("quality-check")),
        List.of(
                new EdgeDefinition("extract", "transform"),
                new EdgeDefinition("extract", "quality-check")));

engine.registerDefinition(definition);
WorkflowExecution execution = engine.start("example", Map.of());
```

节点执行器完成实际任务后，通过 `completeNode` 或 `failNode` 回调引擎。生产环境可自行实现 `ExecutionRepository`、`WorkflowDefinitionRepository` 和 `ExecutionLock`，对接 MySQL、Redis 或其他基础设施。
