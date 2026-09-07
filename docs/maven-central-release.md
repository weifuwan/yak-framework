# Yak Framework 发布到 Maven Central

本文记录 Yak Framework 发布正式版本到 Maven Central 的最简操作流程。

## 1. 注册 Namespace

进入 Maven Central：

https://central.sonatype.com/

在 **Publishing → Namespace** 中注册并验证：

```text
io.github.weifuwan
```

状态变成 `Verified` 即可。

## 2. 生成 Maven Central Token

进入：

https://central.sonatype.com/usertoken

点击 **Generate User Token**，保存生成的 Username 和 Password。

在本机 Maven `settings.xml` 中加入：

```xml
<server>
    <id>central</id>
    <username>YOUR_CENTRAL_TOKEN_USERNAME</username>
    <password>YOUR_CENTRAL_TOKEN_PASSWORD</password>
</server>
```

Windows 默认路径：

```text
C:\Users\你的用户名\.m2\settings.xml
```

> 不要把 Token 提交到 GitHub。

## 3. 安装并生成 GPG Key

Windows 可直接安装：

```bat
winget install --id GnuPG.GnuPG -e
```

重新打开终端后确认：

```bat
gpg --version
```

生成密钥：

```bat
gpg --full-generate-key
```

按提示填写 Real name、Email 和 Passphrase，确认信息时输入 `O`。

查看 Key：

```bat
gpg --list-secret-keys --keyid-format LONG
```

上传公钥：

```bat
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
```

## 4. 发布前验证

在 `yak-framework` 根目录执行：

```bat
git switch main
git pull origin main
mvn clean verify -Pcentral-release -DskipTests -Dspotless.check.skip=true
```

如果弹出 Pinentry，输入创建 GPG Key 时设置的密码。

看到：

```text
BUILD SUCCESS
```

说明本地构建和签名正常。

## 5. 正式发布

执行：

```bat
mvn clean deploy -Pcentral-release -DskipTests -Dspotless.check.skip=true
```

等待 Maven Central 自动完成校验和发布。

最终看到：

```text
BUILD SUCCESS
```

即可。

## 6. 查看发布结果

Maven Central 搜索：

https://central.sonatype.com/search

搜索：

```text
io.github.weifuwan
```

也可以直接查看原始 Maven 仓库：

```text
https://repo1.maven.org/maven2/io/github/weifuwan/
```

发布成功后，其他项目无需配置私有仓库或下载 Token，例如：

```xml
<dependency>
    <groupId>io.github.weifuwan</groupId>
    <artifactId>yak-file</artifactId>
    <version>0.1.0</version>
</dependency>
```
