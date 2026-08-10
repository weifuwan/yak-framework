# Yak 分页模型与边界规范

> 适用于 Yak Framework / Yak Ops 的分页查询、领域仓储和 HTTP 返回结构。

## 1. 目标

分页模型统一遵循一个核心原则：**分页语义统一，但不同工程边界不混用同一种基础设施类型。**

推荐调用链：

```text
DAO / Mapper
    IPage<PO>
        ↓ Repository Adapter
Repository / Service
    PageData<Domain>
        ↓ ViewMapper / Service
HTTP
    Result<PagingData<VO>>
```

这样可以同时做到：

- DAO 保留 MyBatis-Plus 原生分页能力；
- Repository / Service 不感知 MyBatis `IPage`；
- 各业务模块不再重复定义 `XxxPage<T>`；
- HTTP 继续保持现有 `bizData + pagination` JSON 结构；
- 前端第一阶段无需跟随修改。

## 2. DAO / Mapper：允许使用 `IPage<PO>`

`IPage` 属于 MyBatis-Plus 持久化基础设施，只允许停留在 DAO / Mapper 边界。

```java
public interface ResourceDao {
    IPage<ResourcePO> selectPage(PageQuery query);
}
```

DAO 可以使用：

- `Page<PO>`；
- `IPage<PO>`；
- DAO-local `PageQuery`；
- Mapper / LambdaWrapper。

DAO 不应把 `IPage` 返回给 Service。

## 3. Repository / Service：统一使用 `PageData<T>`

Repository 和跨业务 Service 使用 `io.yak.framework.common.PageData<T>`：

```java
public interface ResourceRepository {
    PageData<ResourceNode> page(ResourceQuery query);
}
```

Repository Adapter 负责转换：

```java
IPage<ResourcePO> page = dao.selectPage(query);

return new PageData<>(
    page.getRecords().stream().map(this::toDomain).toList(),
    page.getTotal(),
    page.getPages(),
    page.getCurrent(),
    page.getSize());
```

禁止继续创建模块私有分页容器，例如：

```text
OfflinePage<T>
DataSourcePage<T>
ResourcePage<T>
QualityDomain.Page<T>
XxxPage<T>
```

除非该类型表达的不是普通分页，而是额外包含独立业务语义。

`PageData.map(...)` 可用于只转换记录、不改变分页元数据：

```java
PageData<ResourceVO> viewPage = page.map(viewMapper::resource);
```

## 4. HTTP：统一使用 `PagingData<T>`

`PagingData<T>` 只承担 HTTP 分页输出职责。

第一阶段继续保持现有 JSON：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "bizData": [],
    "pagination": {
      "total": 100,
      "pages": 5,
      "pageNo": 1,
      "pageSize": 20
    }
  }
}
```

转换方式：

```java
PageData<UserVO> page = domainPage.map(viewMapper::user);
return PagingData.from(page);
```

不要为了分页直接让 HTTP 层感知 `IPage`。

## 5. HTTP Envelope：新接口使用 `Result<PagingData<T>>`

`PagingResult<T>` 与 `Result<T>` 重复维护 success / fail / error code 等行为，因此第一阶段开始废弃。

新接口统一：

```java
public Result<PagingData<UserVO>> page(...) {
    return Result.success(service.page(...));
}
```

不再新增：

```java
PagingResult<UserVO>
```

`PagingResult` 暂不删除，避免破坏已有源码调用。

## 6. 第一阶段兼容策略

第一阶段目标是统一内部模型，**不修改 HTTP JSON，不要求前端迁移**。

执行规则：

1. 新增 `PageData<T>` 作为统一业务分页模型；
2. Yak Ops 中已有普通 `XxxPage<T>` 迁移为 `PageData<T>`；
3. `PagingData` 增加 `PageData -> PagingData` 转换；
4. `PagingResult` 标记废弃，新代码使用 `Result<PagingData<T>>`；
5. 旧 `PagingData(IPage<?>)` 构造器暂时兼容保留并标记废弃；
6. 新代码不得再从 Service 直接传递 MyBatis `IPage`。

第一阶段暂时不做：

- 不把 `bizData` 改名为 `records`；
- 不扁平化 `pagination`；
- 不批量修改前端分页读取逻辑；
- 不立即删除所有废弃 API。

## 7. 后续阶段

当 Yak Ops 和其他下游模块都完成 `PageData` 迁移后，可以进入第二阶段：

- 删除 `PagingData(IPage<?>)` 兼容构造器；
- 从 `yak-common` 移除 MyBatis-Plus 依赖；
- 删除 `PagingResult`；
- 如确有必要，再通过明确的 API 版本升级讨论 HTTP JSON 是否从 `bizData + pagination` 调整为更扁平的 `records + total + pageNo + pageSize`。

在第二阶段之前，**现有前端协议保持稳定优先于字段命名优化。**
