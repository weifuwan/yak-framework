# yak-framework

Yak Framework 使用小型 Maven 模块保持能力边界清晰：

- `yak-common`：跨模块共享的响应、分页、错误码与业务异常契约；
- `yak-security`：用户、认证、角色、权限和操作审计能力，依赖 `yak-common`。

`yak-common` 不接收业务 DTO、实体或领域工具，新增内容前应确认它确实是所有业务模块共享的稳定契约。
