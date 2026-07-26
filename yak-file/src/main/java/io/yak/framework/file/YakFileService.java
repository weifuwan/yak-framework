package io.yak.framework.file;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 负责校验、存储路由及元数据生命周期的文件服务门面。 @author weifuwan
 */
public class YakFileService {
    private final Map<StorageType, FileStorage> stores = new EnumMap<StorageType, FileStorage>(StorageType.class);
    private final FileMetadataRepository repository;
    private final FileValidator validator;

    public YakFileService(List<FileStorage> stores, FileMetadataRepository repository, FileValidator validator) {
        if (stores != null) for (FileStorage s : stores) this.stores.put(s.type(), s);
        if (repository == null) throw new IllegalArgumentException("元数据仓储不能为空");
        this.repository = repository;
        this.validator = validator == null ? FileValidator.unlimited() : validator;
    }

    private static String suffix(String n) {
        if (n == null) return "";
        int i = n.lastIndexOf('.');
        return i < 0 ? "" : n.substring(i).replaceAll("[^A-Za-z0-9.]", "");
    }

    public FileMetadata upload(StorageType type, FileUpload file) {
        validator.validate(file);
        FileStorage s = store(type);
        String id = UUID.randomUUID().toString().replace("-", "");
        String object = new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + "/" + id + suffix(file.getName());
        s.upload(object, file);
        FileMetadata m = new FileMetadata();
        m.setId(id);
        m.setOriginalName(file.getName());
        m.setObjectName(object);
        m.setContentType(file.getContentType());
        m.setSize(file.getSize());
        m.setStorageType(type);
        m.setCreatedAt(new Date());
        try {
            repository.save(m);
        } catch (RuntimeException e) {
            s.delete(object);
            throw new FileException("保存元数据失败，文件已回滚", e);
        }
        return m;
    }

    public InputStream download(String id) {
        FileMetadata m = require(id);
        return store(m.getStorageType()).download(m.getObjectName());
    }

    public URL accessUrl(String id, Date expires) {
        if (expires == null || !expires.after(new Date())) throw new FileException("过期时间必须晚于当前时间");
        FileMetadata m = require(id);
        return store(m.getStorageType()).accessUrl(m.getObjectName(), expires);
    }

    public FileMetadata metadata(String id) {
        return require(id);
    }

    public void delete(String id) {
        FileMetadata m = require(id);
        store(m.getStorageType()).delete(m.getObjectName());
        repository.delete(id);
    }

    private FileStorage store(StorageType t) {
        FileStorage s = stores.get(t);
        if (s == null) throw new FileException("未配置存储类型: " + t);
        return s;
    }

    private FileMetadata require(String id) {
        FileMetadata m = repository.find(id);
        if (m == null) throw new FileException("文件不存在: " + id);
        return m;
    }
}
