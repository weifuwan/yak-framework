# yak-common

`yak-common` 只承载各业务模块都需要的统一契约，避免业务代码依赖
`yak-security` 或具体持久化框架的包名：

- `Result`：统一 API 返回 Envelope；
- `PageData`：Repository / Service 使用的框架无关分页数据；
- `PagingData`：HTTP 分页输出结构，继续保持 `bizData + pagination` JSON 契约；
- `ErrorCode`：业务模块错误码的最小接口；
- `BusinessException`：携带结构化错误码的通用业务异常；
- `Assert`：基于 `BusinessException` 的轻量前置条件校验。

分页统一遵循：

```text
DAO / Mapper                  Repository / Service             HTTP
IPage<PO>  -- Adapter -->     PageData<Domain>  -- View -->    Result<PagingData<VO>>
```

约束：

- MyBatis `IPage` 只允许停留在 DAO / Mapper / Repository Adapter 持久化边界；
- `yak-common` 不依赖 MyBatis-Plus；
- Repository / Service 统一使用 `PageData<T>`；
- HTTP 分页统一使用 `PagingData<T>`；
- 新接口统一使用 `Result<PagingData<T>>`；
- 不再创建普通的 `OfflinePage`、`DataSourcePage`、`ResourcePage`、`XxxPage` 等模块私有分页容器。

完整规范见 `docs/pagination-conventions.md`。

业务错误枚举、DTO、实体和领域工具应留在各自模块，不应放入本模块。业务模块只需实现 `ErrorCode`：

```java
public enum OpsErrorCode implements ErrorCode {
    JOB_NOT_FOUND(40001, "作业不存在");
    // getCode() / getMessage()
}

return Result.fail(OpsErrorCode.JOB_NOT_FOUND);
```
