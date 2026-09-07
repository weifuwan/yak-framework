# yak-framework

Yak Framework 使用小型 Maven 模块保持能力边界清晰：

- 根项目 `yak-framework-parent`：统一管理 JDK 21、Spring Boot、依赖版本、编译、测试、格式检查和打包标准；
- `yak-common`：跨模块共享的响应、分页、错误码与业务异常契约；
- `yak-security`：用户、认证、角色、权限和操作审计能力，依赖 `yak-common`；
- `yak-schedule`：插件化统一调度能力，提供稳定 API、核心路由、Quartz 与 XXL-JOB 插件，以及聚合全部插件的 Starter；
- `yak-notification`：Java 8 兼容的通知核心，提供站内信、邮件、Webhook 的统一发送入口、模板和发送记录扩展点；
- `yak-file`：兼容 Java 8 的本地、MinIO、OSS 统一文件服务。

`yak-common` 不接收业务 DTO、实体或领域工具，新增内容前应确认它确实是所有业务模块共享的稳定契约。

当前首个公开发布版本为 `0.1.0`，Git Tag 建议使用 `v0.1.0`。

## 作为父 POM 使用

新增模块应继承统一父 POM，而不是直接继承 Spring Boot Parent：

```xml
<parent>
    <groupId>io.yak.framework</groupId>
    <artifactId>yak-framework-parent</artifactId>
    <version>0.1.0</version>
</parent>
```

## 安装到本地

```shell
D:\baize-works\baize-tools\apache-maven-3.9.16\bin\mvn clean install -DskipTests -Dspotless.check.skip=true
```

## 发布到 Maven Central

项目通过 Sonatype Central Publisher Portal 发布。`central-release` Profile 会自动生成 Sources/Javadoc、执行 GPG 签名，并通过 `central-publishing-maven-plugin` 上传、验证和自动发布制品。

发布前需要完成两项本地配置：

1. 在 Central Portal 中验证 `io.yak.framework` 对应的 Namespace，并生成 User Token。
2. 本机安装可用的 GPG Key，并将公钥发布到公开 Key Server。

将 Central Portal 的 User Token 写入 Maven `settings.xml`：

```xml
<settings>
    <servers>
        <server>
            <id>central</id>
            <username>YOUR_TOKEN_USERNAME</username>
            <password>YOUR_TOKEN_PASSWORD</password>
        </server>
    </servers>
</settings>
```

发布 `0.1.0`：

```shell
D:\baize-works\baize-tools\apache-maven-3.9.16\bin\mvn clean deploy -Pcentral-release -DskipTests -Dspotless.check.skip=true
```

`central-release` 已启用自动发布并等待到 `published` 状态，因此命令成功结束后无需再到 Central Portal 手工点击 Publish。Maven Central 同步完成后，使用方无需配置仓库地址或下载凭证即可直接引用，例如：

```xml
<dependency>
    <groupId>io.yak.framework</groupId>
    <artifactId>yak-file</artifactId>
    <version>0.1.0</version>
</dependency>
```

## 正式版本混淆

普通构建默认不启用 ProGuard。只有显式启用 `release-obfuscated` Profile 时，才会在 `package` 阶段对各个 JAR 模块执行混淆：

```shell
D:\baize-works\baize-tools\apache-maven-3.9.16\bin\mvn clean package -Prelease-obfuscated -DskipTests -Dspotless.check.skip=true
```

如果发布到 Maven Central 时也需要混淆，可同时启用两个 Profile：

```shell
D:\baize-works\baize-tools\apache-maven-3.9.16\bin\mvn clean deploy -Pcentral-release,release-obfuscated -DskipTests -Dspotless.check.skip=true
```

第一版混淆策略只进行名称混淆，不执行 shrink/optimize，并保留公共 API 与 Spring/Jackson 等运行时反射所需的元数据。各模块会在 `target/` 下生成 `proguard-mapping.txt` 和 `proguard-seeds.txt`，用于问题排查和堆栈反混淆；这些文件属于内部发布元数据，不应作为 Maven 制品发布或对外提供。
