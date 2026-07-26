package io.yak.framework.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * 创建关闭后自动删除的临时文件。 @author weifuwan
 */
public class TemporaryFileManager {
    private final Path directory;

    public TemporaryFileManager(Path directory) {
        this.directory = directory;
    }

    public TemporaryFile create(InputStream in, String suffix) {
        try {
            Files.createDirectories(directory);
            Path p = Files.createTempFile(directory, "yak-file-", suffix == null ? ".tmp" : suffix);
            Files.copy(in, p, StandardCopyOption.REPLACE_EXISTING);
            return new TemporaryFile(p);
        } catch (IOException e) {
            throw new FileException("创建临时文件失败", e);
        }
    }

    /**
     * 可自动清理的临时文件句柄。 @author weifuwan
     */
    public static final class TemporaryFile implements AutoCloseable {
        private final Path path;

        private TemporaryFile(Path p) {
            path = p;
        }

        public Path getPath() {
            return path;
        }

        public void close() {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new FileException("删除临时文件失败", e);
            }
        }
    }
}
