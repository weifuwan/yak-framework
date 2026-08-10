# Yak Backend 分层与持久化工程规范

> 适用于 Yak Framework / Yak Ops 及其业务模块的后端工程设计。
>
> 目标不是增加层级，而是让每一层只承担一种职责，使 HTTP 契约、业务领域、数据库模型和外部系统之间保持清晰边界。

## 1. 设计目标

Yak 后端模块默认遵循以下原则：

1. **HTTP 契约与业务模型分离**：DTO / VO 不承担领域职责。
2. **业务模型与数据库模型分离**：Domain / Entity 不直接等同于 PO。
3. **持久化细节不向上泄漏**：Service 不依赖 MyBatis `IPage`、Mapper、PO 等基础设施类型。
4. **数据库访问统一使用 MyBatis-Plus**：简单 SQL 使用 `BaseMapper` / LambdaWrapper；复杂 SQL 使用 Mapper XML。
5. **Repository 与 DAO 各司其职**：Repository 面向领域聚合，DAO 面向数据库访问。
6. **跨模块调用优先使用稳定 Domain / SPI**：禁止业务模块之间通过 HTTP DTO / VO / PO 互相调用。
7. **不过度设计**：简单 CRUD 模块可以省略 Repository；简单 SQL 不为了“形式统一”强行写 XML。
8. **架构规则可以被测试保护**：重要分层边界应通过自动化测试防止回退。

---

## 2. 推荐调用链

### 2.1 领域较复杂的业务模块

适用于 Workflow、Data Quality、Offline Sync、Datasource、Resource 等存在领域聚合、状态机、运行时行为或外部 SPI 的模块。

```text
Controller
   ↓
DTO
   ↓
Service
   ↓
Domain
   ↓
Repository
   ↓
Repository Adapter
   ↓
DAO
   ↓
BaseMapper / Mapper XML
   ↓
PO
   ↓
MySQL

Domain
   ↓
ViewMapper
   ↓
VO
   ↓
Controller
```

如果存在外部系统或插件：

```text
Service / Domain Service
        ↓
       SPI
        ↓
External System / Plugin
```

例如文件管理：

```text
ResourceService / ResourceFileOperations
                 ↓
             Storage SPI
                 ↓
        Local / MinIO / HDFS
```

### 2.2 简单 CRUD 模块

如果模块只有非常简单的单表 CRUD，没有领域聚合，也没有 Framework SPI / 外部系统边界，可以使用：

```text
Controller
   ↓
DTO
   ↓
Service
   ↓
DAO
   ↓
Mapper
   ↓
PO
```

**不要为了统一外观强行增加 Repository。**

当 Service 开始出现以下任意情况时，再引入 Repository 更合适：

- 一个业务对象跨多张表持久化；
- 需要把数据库 Snapshot 重新组装成领域对象；
- 需要实现 Framework / Domain Repository SPI；
- Service 已经频繁操作 PO、Mapper 或 MyBatis 类型；
- 同一个领域对象存在多种持久化实现；
- 领域逻辑需要和数据库结构解耦。

---

## 3. DTO / VO / PO / Domain 的职责

### 3.1 DTO：接口输入

DTO 只描述 HTTP / RPC 输入契约。

```java
public class DataSourceQueryDTO {
    private int pageNo;
    private int pageSize;
    private String keyword;
    private String dbType;
}
```

DTO 可以包含：

- Jakarta Validation 注解；
- 接口默认值；
- 前端传输格式；
- 字符串形式的枚举值。

DTO 不应该：

- 被 DAO / Mapper 使用；
- 被 Repository 暴露；
- 作为领域模型长期保存；
- 在多个业务模块之间作为内部调用协议传播。

HTTP DTO 应在 Service 边界尽快转换为 Domain Query / Command。

```text
DataSourceQueryDTO
        ↓
DataSourceQuery
```

转换过程默认**不修改原 DTO**。

---

### 3.2 VO：接口输出

VO 只描述对外响应结构。

```java
public class DataSourceVO {
    private Long id;
    private String name;
    private String dbType;
    private String connStatus;
}
```

VO 可以承担：

- JSON 序列化格式；
- 字段脱敏；
- 前端展示字段；
- 树结构 children；
- 展示型派生字段。

VO 不应该：

- 被 DAO / Mapper 直接返回；
- 被 Repository 返回；
- 作为 Runtime / Domain 的内部状态模型；
- 被业务模块之间作为核心调用模型。

推荐集中使用纯转换类：

```text
Domain → XxxViewMapper → VO
```

`ViewMapper` 不查库、不调用外部系统、不做业务判断，只负责模型转换和必要的输出脱敏。

---

### 3.3 PO：数据库表映射

PO 必须尽量忠实描述数据库中的一行。

```java
@TableName("yak_workflow_execution")
public class WorkflowExecutionPO {
    private String id;
    private String status;
    private String inputJson;
    private String outputJson;
}
```

规则：

- 一张业务表原则上对应一个 PO；
- PO 放数据库列，不放页面派生字段；
- JSON 列优先保持 `String`，对象转换在 Adapter / Codec 层完成；
- PO 不进入 Controller / Service 的公开方法签名；
- PO 不作为跨模块模型。

SQL 联查或聚合结果不应硬塞进表 PO，可以单独建立 DAO-local Row / Projection：

```java
public class DataSourceSummaryRow {
    private long total;
    private long connected;
    private long disconnected;
}
```

这种 Row 属于持久化层，不属于 VO。

---

### 3.4 Domain / Entity：业务语义

Domain 表达真实业务概念，而不是数据库行或 HTTP 请求。

例如：

```text
WorkflowDefinition
WorkflowVersion
WorkflowRunSpec
OfflineJobDefinition
DataSourceDefinition
ResourceNode
```

Domain 可以包含：

- 业务枚举；
- 状态；
- 不变量；
- 运行时需要的信息；
- 聚合关系；
- 领域行为。

Domain 不应包含：

- `@TableName`；
- MyBatis Wrapper；
- HTTP Validation 注解；
- Servlet / Controller 类型；
- 页面展示专用逻辑。

Domain 是否使用 Java Bean、record 或不可变对象，根据业务语义选择，不要求形式统一。

---

## 4. Controller 规范

Controller 的职责只包括：

1. 接收参数；
2. 参数校验；
3. 权限声明；
4. 调用 Service；
5. 返回结果；
6. 必要的 HTTP 协议处理，例如文件流写入 `HttpServletResponse`。

推荐：

```text
Controller → Service
```

禁止常规 Controller 直接依赖：

```text
Repository
DAO
Mapper
PO
JdbcTemplate
StorageOperator
Engine implementation
```

特殊 HTTP 协议处理可以留在 Controller，例如：

```text
ResourceDownload Domain
       ↓
Controller
       ↓
Content-Type / Content-Length / Content-Disposition
       ↓
HttpServletResponse
```

这属于 HTTP 适配，不属于业务逻辑。

---

## 5. Service 规范

Service 是业务用例编排层。

典型职责：

- DTO → Domain；
- 调用 Repository；
- 调用领域服务 / Engine / SPI；
- 事务边界；
- Domain → VO；
- 业务异常转换。

Service 不应该直接依赖：

```text
Mapper
PO
IPage<PO>
JdbcTemplate
NamedParameterJdbcTemplate
```

复杂模块中也尽量避免 Service 直接依赖 DAO。

推荐：

```text
Service
   ↓
Repository
```

简单 CRUD 模块可允许：

```text
Service
   ↓
DAO
```

---

## 6. Repository 规范

Repository 是**领域仓储 / 聚合适配器**，不是 DAO 的同义词。

它的接口应该使用 Domain 和框架无关的公共契约：

```java
public interface ResourceRepository {
    Optional<ResourceNode> findById(Long id);
    boolean insert(ResourceNode resource);
    boolean update(ResourceNode resource);
    PageData<ResourceNode> page(ResourceQuery query);
}
```

Repository 不应该暴露：

```text
DTO
VO
PO
IPage
MyBatis Page<T>
LambdaQueryWrapper
Mapper
ResultSet
```

Repository Adapter 可以依赖 DAO / PO：

```text
ResourceRepository
        ↑ implements
ResourceRepositoryAdapter
        ↓
ResourceDao
        ↓
ResourceMapper
```

Adapter 的典型职责：

- Domain ↔ PO；
- Snapshot ↔ Aggregate；
- 多表组装；
- JSON 编解码；
- Framework Repository SPI 适配；
- MyBatis 生成 ID 后回填 Domain；
- `IPage<PO> → PageData<Domain>` 分页边界转换。

如果一个模块没有真正的领域仓储需求，可以不创建 Repository。

---

## 7. DAO 规范

DAO 面向数据库操作，属于持久化基础设施。

```java
public interface DataSourceDao {
    DataSourcePO selectById(Long id);
    IPage<DataSourcePO> selectPage(PageQuery query);
    DataSourceSummaryRow selectSummary();
}
```

DAO 可以暴露：

- PO；
- DAO-local Query；
- DAO-local Row / Projection；
- MyBatis `IPage<PO>`。

DAO 不应该暴露：

```text
HTTP DTO
HTTP VO
Domain Service
Controller types
```

### DAO Query

不要让 DAO 直接使用 HTTP 查询 DTO：

```text
错误：
ResourceDao.selectPage(ResourceQueryDTO dto)

推荐：
ResourceDao.selectPage(ResourceDao.PageQuery query)
```

DAO Query 应只包含数据库查询真正需要的字段。

---

## 8. Mapper 规范

Mapper 原则上“一张表一个 BaseMapper”。

```java
@Mapper
public interface WorkflowExecutionMapper
        extends BaseMapper<WorkflowExecutionPO> {
}
```

复杂读模型可以额外建立 QueryMapper：

```text
QualityQueryMapper
```

数据库原子写操作可以单独建立 WriteMapper：

```text
QualityWriteMapper
OfflineWriteMapper
```

这样职责清晰：

```text
BaseMapper      → 普通单表 CRUD
QueryMapper     → 跨表 / 聚合 / 报表 / 复杂动态查询
WriteMapper     → FOR UPDATE / UPSERT / 条件 Claim 等原子写语义
```

---

## 9. MyBatis-Plus 与 Mapper XML 使用标准

### 9.1 优先使用 MyBatis-Plus 的场景

以下 SQL 默认不写 XML：

```text
selectById
insert
updateById
单表分页
简单 eq / like / in
简单排序
exists / count
字段级 LambdaUpdate
```

例如：

```java
mapper.selectList(
    Wrappers.<ResourcePO>lambdaQuery()
        .eq(ResourcePO::getParentId, parentId)
        .like(StringUtils.hasText(keyword), ResourcePO::getName, keyword)
        .orderByAsc(ResourcePO::getName));
```

**不要为了目录形式统一而强行创建 XML。**

---

### 9.2 应放 Mapper XML 的场景

出现以下情况时优先使用 XML：

- 多表 JOIN；
- 聚合报表；
- 较长的动态 SQL；
- `UNION`；
- 复杂子查询；
- MySQL `ON DUPLICATE KEY UPDATE`；
- `SELECT ... FOR UPDATE`；
- 条件 Claim；
- 批量原子更新；
- SQL 已经明显影响 Java 可读性。

目录建议：

```text
src/main/resources/
└── mapper/
    ├── workflow/
    ├── quality/
    ├── sync/
    ├── datasource/
    └── resource/
```

共享业务 SqlSessionFactory 可以统一加载：

```text
classpath*:mapper/**/*.xml
```

---

## 10. Repository 与 DAO 的区别

这是本规范中最重要的概念之一。

### DAO

DAO 关注：

> “数据库怎么查、怎么写？”

它认识：

```text
PO
SQL
IPage
Mapper
Row Projection
```

### Repository

Repository 关注：

> “业务对象怎么保存、怎么恢复？”

它认识：

```text
Domain
Aggregate
Snapshot
Domain Query
PageData
```

例如 Workflow：

```text
WorkflowExecution
        ↓ snapshot
WorkflowExecutionRepositoryAdapter
        ↓
WorkflowExecutionDao
WorkflowNodeExecutionDao
WorkflowNodeAttemptDao
        ↓
3 张表
```

不能因为 Security 模块简单 CRUD 使用 `Service → DAO → Mapper`，就把复杂 Workflow Repository 也删除掉。

**是否需要 Repository，取决于领域复杂度，不取决于目录是否想保持完全一样。**

---

## 11. 分页模型边界

分页统一使用三种明确的边界模型：

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

规则：

- MyBatis `IPage` 只属于持久化层；
- Repository Adapter 负责 `IPage<PO> → PageData<Domain>`；
- Repository / Service / 跨模块调用统一使用 `PageData<T>`；
- HTTP 分页数据使用 `PagingData<T>`，保持现有 `bizData + pagination` JSON；
- HTTP Envelope 统一使用 `Result<T>`，不再维护独立 `PagingResult<T>`；
- 普通业务模块不得重新创建 `OfflinePage`、`ResourcePage`、`DataSourcePage`、`QualityDomain.Page` 等等价包装。

例如：

```java
IPage<ResourcePO> page = resourceDao.selectPage(query);

PageData<ResourceNode> domainPage = new PageData<>(
    page.getRecords().stream().map(this::toDomain).toList(),
    page.getTotal(),
    page.getPages(),
    page.getCurrent(),
    page.getSize());
```

HTTP 边界：

```java
PageData<ResourceVO> viewPage = domainPage.map(viewMapper::node);
return Result.success(PagingData.from(viewPage));
```

`yak-common` 必须保持持久化框架无关，不因为分页工具依赖 MyBatis-Plus。

更完整的分页规范见：

```text
docs/pagination-conventions.md
```

---

## 12. 跨模块调用规范

业务模块之间禁止通过以下类型耦合：

```text
PO
Mapper
DAO
HTTP DTO
HTTP VO
```

优先顺序：

1. Domain / Application API；
2. 稳定 SPI；
3. Framework API；
4. 明确的内部契约对象。

例如：

```text
错误：
Workflow → OfflineJobDefinitionQueryDTO → OfflineJobDefinitionVO

推荐：
Workflow → OfflineDefinitionQuery → PageData<OfflineJobDefinition>
```

HTTP DTO / VO 只应该服务于 HTTP 边界。

---

## 13. 外部系统 / SPI 边界

数据库 Repository 和外部系统 SPI 是两种不同基础设施，不要混为一层。

例如文件管理：

```text
ResourceRepository → MySQL 元数据
StorageOperator    → 文件内容
```

例如 Workflow：

```text
ExecutionRepository → 工作流状态
NodeExecutor         → 外部任务执行
```

例如 DataSource：

```text
DataSourceRepository → 数据源注册信息
DataSourcePlugin     → 实际数据库连接和 Catalog 操作
```

Service 可以同时编排 Repository 和 SPI，但 Repository 本身不应该把 `MultipartFile`、`InputStream`、HTTP Request 等外部协议对象纳入接口。

---

## 14. 事务与外部系统一致性

数据库事务不能天然覆盖 MinIO、HDFS、远程任务、Git 等外部系统。

遇到数据库 + 外部系统组合操作时，需要明确一致性策略。

例如文件创建：

```text
写物理文件
   ↓
写数据库元数据
   ↓ DB 失败
补偿删除物理文件
```

文件删除：

```text
数据库事务删除元数据
   ↓ COMMIT
删除外部存储对象
```

外部通知：

```text
数据库事务
   ↓ COMMIT
Event / Sync Provider
```

原则：

- 外部调用不要无意义地拖长数据库事务；
- 事务前置还是事务后置，由失败语义决定；
- 需要补偿时必须明确补偿路径；
- Observer / EventListener 的异常原则上不能破坏已经完成的领域状态迁移。

---

## 15. 数据库模型与不可变快照

对于版本化对象、发布对象、执行快照：

> 数据库是事实来源，内存只是执行加速器。

发布后的不可变版本应采用 first-write-wins：

```text
不存在 → 写入
已存在且语义相同 → 允许
已存在且内容不同 → 拒绝覆盖
```

不要用同一个 versionId 静默覆盖已经发布的运行定义。

Editor-only 字段和 Runtime 字段应尽量分离，避免 UI 状态进入运行快照。

---

## 16. JSON 字段规范

数据库中的 JSON / LONGTEXT 字段在 PO 层优先保持原始 `String`：

```java
private String engineDefinitionJson;
private String runtimeMetadataJson;
```

对象转换放在：

```text
Repository Adapter
Codec
Compiler
```

不要让 PO 直接依赖 HTTP Request 或复杂 Domain 类型。

语义比较 JSON 时应按 JSON Tree 比较，而不是简单比较字符串，避免字段顺序导致误判。

---

## 17. 架构防回退测试

仅靠 Code Review 很容易让边界慢慢退化。

推荐关键模块增加轻量架构测试，例如反射检查：

### Repository 不允许 DTO / VO / PO / IPage

```java
for (Method method : XxxRepository.class.getDeclaredMethods()) {
    assertNoPackage(
        method.getGenericReturnType(),
        ".bean.dto.",
        ".bean.vo.",
        ".bean.po.",
        "com.baomidou.mybatisplus");
}
```

Repository 分页方法的 raw return type 应为 `PageData.class`。

### DAO 不允许 DTO / VO

```text
DAO parameter / return type
不能包含 .bean.dto. / .bean.vo.
```

### Service 不允许 DAO / PO / IPage

复杂模块可检查：

```text
Service fields / public signatures
不能包含 .dao. / .bean.po. / com.baomidou.mybatisplus
```

### Controller 不能绕过 Service

```text
Controller fields
不能依赖 Repository / DAO / Mapper / Engine implementation / StorageOperator
```

架构测试不是为了限制设计，而是为了保护已经明确的边界。

---

## 18. 推荐目录结构

复杂业务模块建议：

```text
io.yak.xxx.business.<module>
├── controller
├── service
│   ├── impl
│   └── support
├── domain
├── repository
├── dao
│   ├── impl
│   ├── mapper
│   └── model
├── config
├── exception
├── util
└── <external-spi-support>
```

Common 中：

```text
io.yak.xxx.common.bean
├── dto/<module>
├── vo/<module>
└── po/<module>
```

枚举：

```text
io.yak.xxx.common.enums/<module>
```

Mapper XML：

```text
src/main/resources/mapper/<module>/*.xml
```

Domain 默认留在业务模块，不要因为多个模块“可能以后会用”就提前放入 common。

---

## 19. 常见反例

### 19.1 Repository 直接返回 VO

```java
// 不推荐
DataSourceSummaryVO summary();
```

推荐：

```java
DataSourceSummary summary();
```

然后 Service / ViewMapper 转换为 VO。

---

### 19.2 DAO 接 HTTP DTO

```java
// 不推荐
IPage<ResourcePO> selectPage(ResourceQueryDTO dto);
```

推荐：

```java
IPage<ResourcePO> selectPage(PageQuery query);
```

---

### 19.3 Service 直接操作 PO

```java
// 不推荐
ResourcePO resource = resourceDao.selectById(id);
resource.setVersion(resource.getVersion() + 1);
```

复杂模块推荐：

```java
ResourceNode resource = repository.findById(id).orElseThrow(...);
resource.setVersion(nextVersion(resource));
repository.update(resource);
```

---

### 19.4 Mapper 直接返回 VO

```java
// 不推荐
DataSourceSummaryVO selectSummary();
```

推荐：

```java
DataSourceSummaryRow selectSummary();
```

---

### 19.5 所有 SQL 都塞 Java 注解

长 SQL：

```java
@Select("SELECT ... JOIN ... CASE WHEN ... GROUP BY ...")
```

应迁入 XML。

但简单单表 CRUD 也不要反向全部迁入 XML。

---

### 19.6 为统一目录机械增加 Repository / XML

如果业务只有：

```text
Service → DAO → BaseMapper → 单表
```

且边界已经清楚，就不需要人为增加：

```text
Repository → Adapter → QueryMapper.xml
```

**架构统一的是职责，不是文件数量。**

---

### 19.7 业务模块重新包装普通分页

```java
// 不推荐
public record ResourcePage<T>(
    List<T> records,
    long total,
    long pages,
    long pageNo,
    long pageSize) {}
```

推荐直接：

```java
PageData<ResourceNode>
```

如果只是字段名完全相同，没有新增独立业务语义，就不应再增加一层分页类型。

---

## 20. Code Review Checklist

新模块或重构模块至少检查以下内容：

- [ ] Controller 是否只通过 Service 进入业务逻辑？
- [ ] DTO 是否只停留在 HTTP / Application 输入边界？
- [ ] VO 是否只作为输出模型？
- [ ] Service 是否泄漏 Mapper / PO / IPage？
- [ ] Repository 是否只暴露 Domain / `PageData` 等框架无关契约？
- [ ] Repository 分页是否避免 `IPage` / MyBatis `Page`？
- [ ] 是否避免创建无业务语义的 `XxxPage<T>`？
- [ ] DAO 是否避免 DTO / VO？
- [ ] PO 是否忠实对应数据库表？
- [ ] 聚合 SQL 是否使用持久化 Row，而不是 VO？
- [ ] 简单 SQL 是否优先 MyBatis-Plus？
- [ ] 复杂 SQL 是否放 Mapper XML？
- [ ] 跨模块是否避免 DTO / VO / PO 耦合？
- [ ] 外部 SPI 是否与数据库 Repository 分离？
- [ ] JSON 字段是否在 Adapter / Codec 转换？
- [ ] 发布快照是否保持不可变？
- [ ] 数据库事务与外部系统失败语义是否明确？
- [ ] 是否需要架构防回退测试？
- [ ] 是否存在仅为了“层级看起来统一”而增加的无价值抽象？

---

## 21. 最终原则

Yak 的分层规范不是要求所有模块长得完全一样，而是要求：

> **同一种职责，使用同一种工程语言；不同复杂度，允许不同层级。**

最终希望保持以下依赖方向：

```text
HTTP
 ↓
Application
 ↓
Domain
 ↓
Infrastructure
```

而不是：

```text
HTTP DTO
 ↕
Service
 ↕
PO / Mapper / VO
 ↕
Database
```

当一个模块出现“DTO 被持久化层引用、VO 被 Mapper 返回、Service 直接操作 PO、跨模块传 HTTP 模型”时，应优先考虑重新恢复边界，而不是继续在现有耦合上增加功能。
