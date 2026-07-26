package io.yak.framework.file;
/** 文件元数据仓储扩展点。 @author weifuwan */
public interface FileMetadataRepository { void save(FileMetadata value); FileMetadata find(String id); void delete(String id); }
