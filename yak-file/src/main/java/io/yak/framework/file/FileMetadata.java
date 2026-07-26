package io.yak.framework.file;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件元数据，可由业务仓储持久化。 @author weifuwan
 */
public class FileMetadata implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id, originalName, objectName, contentType;
    private long size;
    private StorageType storageType;
    private Date createdAt;

    public String getId() {
        return id;
    }

    public void setId(String v) {
        id = v;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String v) {
        originalName = v;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String v) {
        objectName = v;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String v) {
        contentType = v;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long v) {
        size = v;
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public void setStorageType(StorageType v) {
        storageType = v;
    }

    public Date getCreatedAt() {
        return createdAt == null ? null : new Date(createdAt.getTime());
    }

    public void setCreatedAt(Date v) {
        createdAt = v == null ? null : new Date(v.getTime());
    }
}
