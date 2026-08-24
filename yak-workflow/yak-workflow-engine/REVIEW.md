# Yak Workflow Engine Review

本文件定义 `yak-workflow-engine` PR 的长期 Review 标准。

## Read First

```text
REQUIREMENTS.md
DOMAIN.md
ARCHITECTURE.md
DEPENDENCIES.md
README.md
PR diff + behavior tests
```

## 1. Domain Review

重点检查：

- 是否出现第二套 WorkflowExecution / NodeExecution / Attempt truth；
- retry 是否错误创建新 WorkflowExecution；
- restart / rerun 是否错误复用旧 executionId；
- stale attempt callback 是否可能覆盖当前 Attempt；
- pause / resume 是否改变 attempt identity；
- recovery 是否生成新的 attempt identity 或猜测外部状态；
- Definition 是否在运行中漂移到新的宿主业务版本。

当前模型表达不了真实需求时报告 `Domain Gap`，不要先加隐藏 flag。

## 2. Command and Concurrency Review

- 已存在 Execution 的 command 是否仍经过 `ExecutionMailbox`；
- callback / timeout / manual control 是否存在第二条直接状态修改路径；
- attemptId fencing 是否仍对 stale / duplicate callback 生效；
- normal runtime 和 startup recovery 是否误用同一个并发入口；
- `ExecutionLock` 是否只用于明确的 recovery / compatibility 语义。

## 3. State Review

- 状态是否通过 typed StateMachine 和领域方法迁移；
- terminal / pause lifecycle / retryable 判断是否仍使用 typed state；
- timeout 是否保留 typed failure reason；
- ERROR / FAILED / CANCELED / TIMED_OUT / UPSTREAM_FAILED 等语义是否被错误折叠。

## 4. Architecture Review

- `api` 是否仍是薄 facade；
- normal runtime 是否位于 `runtime`；
- startup reconciliation 是否位于 `recovery`；
- low-level package 是否反向 import `api / runtime / recovery`；
- Scheduler / Policy 是否开始持久化或调用外部执行；
- NodeExecutor 是否开始直接修改 Engine domain；
- 是否出现 `service / helper / utils / common / base` 大桶；
- `support` 是否仍只放 local / in-memory SPI implementation。

## 5. Core Purity

不允许把下面能力带入 Engine Core：

- Spring；
- JDBC / ORM / MyBatis；
- HTTP client；
- Quartz；
- 具体任务实现；
- 宿主业务 DTO / PO。

所有宿主能力应停在 SPI。

## 6. Compatibility

架构重构默认不得破坏：

- `WorkflowEngine`；
- `DefaultWorkflowEngine` constructors / static factories / methods；
- `WorkflowRecoveryCoordinator` constructor / `recover`；
- SPI package；
- Definition / Execution / Command public values；
- state enum semantics；
- existing behavior tests。

## 7. Required Tests

高风险变更至少覆盖对应 behavior test。public API 或 package boundary 变化时，同时通过 architecture / API compatibility guard。

## Review Output

```text
Conclusion: PASS | CHANGES_REQUIRED

P0 Blocker: ...
P1 Must Fix: ...
P2 Suggestion: ...
Requirement Gap: ...
Domain Gap: ...
Missing Tests: ...
```
