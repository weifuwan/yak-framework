# 构建兼容性边界

根 Maven reactor 以 Spring Boot 4.1.0 和 Java 21 为统一基线，当前只聚合
`yak-security`。该模块使用 Jakarta Servlet API 和面向 Spring Boot 4 的
MyBatis-Plus starter。

`yak-job` 暂时不在根 reactor 的 Boot 4 聚合构建边界内。它仍是一个遗留的
Spring Boot 2 / Java 8 模块，并包含 Springfox、旧 MyBatis starter 等与 Boot 4
不兼容的依赖。隔离只避免在同一个 reactor 中混用两套基线，并不表示该模块已经
完成升级；迁移前应当从 `yak-job` 目录独立维护和构建它。此选择不改变其业务逻辑。

Spring Boot 版本解析应以 Maven Central 为准。升级基线时可使用以下命令验证父 POM
确实存在，而不是仅依赖镜像缓存：

```shell
mvn -U dependency:get \
  -Dartifact=org.springframework.boot:spring-boot-starter-parent:4.1.0:pom \
  -Dtransitive=false \
  -f /tmp/empty-pom.xml
```
