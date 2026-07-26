package io.yak.framework.file;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;

/**
 * 带目录越界防护的本地文件存储。 @author weifuwan
 */
public class LocalFileStorage implements FileStorage {
    private final Path root;
    private final String baseUrl;

    public LocalFileStorage(Path root, String baseUrl) {
        this.root = root.toAbsolutePath().normalize();
        this.baseUrl = baseUrl;
        try {
            Files.createDirectories(this.root);
        } catch (IOException e) {
            throw new FileException("创建存储目录失败", e);
        }
    }

    public StorageType type() {
        return StorageType.LOCAL;
    }

    public void upload(String n, FileUpload f) {
        Path p = path(n);
        try {
            Files.createDirectories(p.getParent());
            Files.copy(f.getStream(), p, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileException("上传失败", e);
        }
    }

    public InputStream download(String n) {
        try {
            return Files.newInputStream(path(n));
        } catch (IOException e) {
            throw new FileException("下载失败", e);
        }
    }

    public void delete(String n) {
        try {
            Files.deleteIfExists(path(n));
        } catch (IOException e) {
            throw new FileException("删除失败", e);
        }
    }

    public URL accessUrl(String n, Date ignored) {
        try {
            return baseUrl == null ? path(n).toUri().toURL() : new URL(baseUrl.replaceAll("/+$", "") + "/" + n);
        } catch (MalformedURLException e) {
            throw new FileException("访问地址无效", e);
        }
    }

    private Path path(String n) {
        Path p = root.resolve(n).normalize();
        if (!p.startsWith(root)) throw new FileException("非法对象名");
        return p;
    }
}
