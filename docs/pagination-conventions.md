# Yak 分页模型与边界规范

> 适用于 Yak Framework / Yak Ops 的分页查询、领域仓储和 HTTP 返回结构。

## 1. 核心原则

分页模型统一遵循一个原则：**分页语义统一，但持久化分页、业务分页和 HTTP 分页分别停留在自己的工程边界。**

标准调用链：

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
- `yak-common` 不依赖 MyBatis-Plus；
- 各业务模块不重复定义普通 `XxxPage<T>`；
- HTTP 保持稳定的 `bizData + pagination` JSON 契约。

---

## 2. DAO / Mapper：允许使用 `IPage<PO>`

`IPage` 属于 MyBatis-Plus 持久化基础设施，只允许停留在 DAO / Mapper，以及负责持久化转换的 Repository Adapter 内部。

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

禁止：

```text
DAO -> Service 返回 IPage<PO>
Repository 接口暴露 IPage
Controller 直接处理 IPage
```

Repository Adapter 应在持久化边界完成转换：

```java
IPage<ResourcePO> page = dao.selectPage(query);

return new PageData<>(
    page.getRecords().stream().map(this::toDomain).toList(),
    page.getTotal(),
    page.getPages(),
    page.getCurrent(),
    page.getSize());
```

---

## 3. Repository / Service：统一使用 `PageData<T>`

Repository 和跨业务 Service 使用：

```java
io.yak.framework.common.PageData<T>
```

例如：

```java
public interface ResourceRepository {
    PageData<ResourceNode> page(ResourceQuery query);
}
```

`PageData<T>` 是框架无关的业务分页模型，只包含：

```text
records
总记录数 total
总页数 pages
当前页 pageNo
每页大小 pageSize
```

可以通过 `map(...)` 只转换记录类型，同时保持分页元数据：

```java
PageData<ResourceVO> viewPage = page.map(viewMapper::resource);
```

普通业务模块禁止继续创建等价包装：

```text
OfflinePage<T>
DataSourcePage<T>
ResourcePage<T>
QualityDomain.Page<T>
XxxPage<T>
```

只有当某个类型包含**独立且明确的分页业务语义**，而不是单纯复制 `records/total/pages/pageNo/pageSize` 时，才允许定义专用分页类型。

---

## 4. HTTP：使用 `PagingData<T>`

`PagingData<T>` 只承担 HTTP 分页输出职责。

现有接口继续保持：

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

标准转换：

```java
PageData<UserVO> page = domainPage.map(viewMapper::user);
return PagingData.from(page);
```

`PagingData` 不提供 MyBatis `IPage` 构造器，也不依赖任何持久化框架。

---

## 5. HTTP Envelope：统一使用 `Result<T>`

分页接口与普通接口使用同一个响应 Envelope。

推荐：

```java
public Result<PagingData<UserVO>> page(...) {
    return Result.success(service.page(...));
}
```

不再维护独立的 `PagingResult<T>`。

原因是 `PagingResult` 与 `Result` 会重复维护：

```text
success
fail
BusinessException 转换
错误码
参数校验失败
succeeded / failed
```

分页只是 `data` 的一种业务形态，不应该拥有第二套 HTTP Envelope。

---

## 6. 层级职责总结

| 边界 | 分页类型 | 是否允许 MyBatis |
|---|---|---|
| Mapper / DAO | `IPage<PO>` | 是 |
| Repository Adapter 内部 | `IPage<PO>` -> `PageData<Domain>` | 是 |
| Repository 接口 | `PageData<Domain>` | 否 |
| Service / 跨模块调用 | `PageData<T>` | 否 |
| HTTP 分页数据 | `PagingData<VO>` | 否 |
| HTTP Envelope | `Result<PagingData<VO>>` | 否 |

`yak-common` 自身必须保持持久化框架无关，不允许因为分页工具重新引入 MyBatis-Plus、JPA、JDBC Template 等持久化依赖。

---

## 7. 架构防回退

业务模块建议通过架构测试固定以下规则：

```text
Repository 方法签名不得包含 IPage / MyBatis Page
Repository 分页应返回 PageData
Service 不得直接处理 PO / IPage
跨业务模块分页使用 PageData
普通模块不得重新创建等价 XxxPage<T>
```

`yak-common` 自身至少应验证：

- `PagingData` 的公开构造器不依赖 MyBatis 类型；
- `PageData.map(...)` 保持全部分页元数据；
- `PageData -> PagingData` 保持现有 HTTP 字段语义。

---

## 8. HTTP JSON 是否需要改版

当前分页治理只统一 Java 工程边界，**不要求前端迁移**。

现有：

```text
data.bizData
data.pagination.total
data.pagination.pages
data.pagination.pageNo
data.pagination.pageSize
```

继续作为稳定 API 契约。

如果未来确实需要改为：

```text
data.records
data.total
data.pages
data.pageNo
data.pageSize
```

应作为明确的 API 版本升级单独实施，并同步评估所有前端调用方；不要把 HTTP Breaking Change 混入后端分层或分页模型重构。

---

## 9. Code Review Checklist

分页相关 PR 至少确认：

```text
□ DAO / Mapper 之外是否泄漏 IPage？
□ Repository 是否统一返回 PageData？
□ 是否新建了无业务含义的 XxxPage<T>？
□ Service 是否直接依赖 MyBatis Page / IPage？
□ HTTP 是否仍通过 PagingData 输出？
□ 新接口是否使用 Result<PagingData<T>>？
□ 是否无意修改了 bizData + pagination JSON？
□ yak-common 是否仍保持持久化框架无关？
```
