# yak-file

兼容 Java 8 的统一文件模块，提供本地、MinIO、阿里云 OSS 抽象、元数据、上传/下载/删除、大小和 MIME/扩展名校验、临时文件以及访问地址。

`YakFileService` 是统一入口。本地存储直接使用 `LocalFileStorage`；MinIO 和 OSS 使用 `DelegatingObjectStorage` 的 `Client` 桥接各项目选定的 SDK
版本。生产环境应实现 `FileMetadataRepository` 持久化元数据，内存实现仅适合测试。
