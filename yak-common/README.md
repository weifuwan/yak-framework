# yak-common

`yak-common` 只承载各业务模块都需要的统一契约，避免业务代码依赖
`yak-security` 的包名：

- `Result`、`PagingResult`、`PagingData`：统一 API 返回与分页结构；
- `ErrorCode`：业务模块错误码的最小接口；
- `BusinessException`：携带结构化错误码的通用业务异常；
- `Assert`：基于 `BusinessException` 的轻量前置条件校验。

业务错误枚举、DTO、实体和领域工具应留在各自模块，不应放入本模块。 业务模块只需实现 `ErrorCode`：

```java
public enum OpsErrorCode implements ErrorCode {
    JOB_NOT_FOUND(40001, "作业不存在");
    // getCode() / getMessage()
}

return Result.fail(OpsErrorCode.JOB_NOT_FOUND);
```
