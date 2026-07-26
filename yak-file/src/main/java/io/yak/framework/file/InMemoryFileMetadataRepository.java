package io.yak.framework.file;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 测试和单机临时场景使用的内存仓储。 @author weifuwan
 */
public class InMemoryFileMetadataRepository implements FileMetadataRepository {
    private final ConcurrentMap<String, FileMetadata> values = new ConcurrentHashMap<String, FileMetadata>();

    public void save(FileMetadata v) {
        values.put(v.getId(), v);
    }

    public FileMetadata find(String id) {
        return values.get(id);
    }

    public void delete(String id) {
        values.remove(id);
    }
}
