# yak-framework

Yak Framework 使用小型 Maven 模块保持能力边界清晰：

- 根项目 `yak-framework-parent`：统一管理 JDK 21、Spring Boot、依赖版本、编译、测试、格式检查和打包标准；

- `yak-common`：跨模块共享的响应、分页、错误码与业务异常契约；
- `yak-security`：用户、认证、角色、权限和操作审计能力，依赖 `yak-common`。
- `yak-schedule`：面向多项目的 Quartz 调度、Cron 管理、执行控制、日志、并发、重试和操作人审计能力。

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