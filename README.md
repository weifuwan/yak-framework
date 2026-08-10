# yak-framework

Yak Framework 使用小型 Maven 模块保持能力边界清晰：

- 根项目 `yak-framework-parent`：统一管理 JDK 21、Spring Boot、依赖版本、编译、测试、格式检查和打包标准；

- `yak-common`：跨模块共享的响应、分页、错误码与业务异常契约；
- `yak-security`：用户、认证、角色、权限和操作审计能力，依赖 `yak-common`。
- `yak-schedule`：插件化统一调度能力，提供稳定 API、核心路由、Quartz 与 XXL-JOB 插件，以及聚合全部插件的 Starter。

- `yak-notification`：Java 8 兼容的通知核心，提供站内信、邮件、Webhook 的统一发送入口、模板和发送记录扩展点。

- `yak-file`：兼容 Java 8 的本地、MinIO、OSS 统一文件服务。

`yak-common` 不接收业务 DTO、实体或领域工具，新增内容前应确认它确实是所有业务模块共享的稳定契约。

新增模块应继承统一父 POM，而不是直接继承 Spring Boot Parent：

```xml
<parent>
    <groupId>io.yak.framework</groupId>
    <artifactId>yak-framework-parent</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <relativePath>../pom.xml</relativePath>
</parent>
```

### 安装到本地
```shell
D:\baize-works\baize-tools\apache-maven-3.9.16\bin\mvn clean install -DskipTests -Dspotless.check.skip=true
```

## 上传到阿里云
```shell
D:\baize-works\baize-tools\apache-maven-3.9.16\bin\mvn clean deploy -DskipTests -Dspotless.check.skip=true
```

## 正式版本混淆

普通构建和 Snapshot 发布默认不启用 ProGuard。只有显式启用 `release-obfuscated` Profile 时，才会在 `package` 阶段对各个 JAR 模块执行混淆：

```shell
D:\baize-works\baize-tools\apache-maven-3.9.16\bin\mvn clean package -Prelease-obfuscated -DskipTests -Dspotless.check.skip=true
```

正式 Release 发布示例：

```shell
D:\baize-works\baize-tools\apache-maven-3.9.16\bin\mvn clean deploy -Prelease-obfuscated -Drevision=1.0.0 -DskipTests -Dspotless.check.skip=true
```

第一版混淆策略只进行名称混淆，不执行 shrink/optimize，并保留公共 API 与 Spring/Jackson 等运行时反射所需的元数据。各模块会在 `target/` 下生成 `proguard-mapping.txt` 和 `proguard-seeds.txt`，用于问题排查和堆栈反混淆；这些文件属于内部发布元数据，不应作为 Maven 制品发布或对外提供。
