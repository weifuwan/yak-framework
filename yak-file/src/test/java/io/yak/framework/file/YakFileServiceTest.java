package io.yak.framework.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 文件服务契约测试。 @author weifuwan
 */
class YakFileServiceTest {
    @TempDir
    Path dir;

    @Test
    void localLifecycle() throws Exception {
        byte[] bytes = "yak".getBytes(StandardCharsets.UTF_8);
        YakFileService service = new YakFileService(Collections.<FileStorage>singletonList(new LocalFileStorage(dir, null)), new InMemoryFileMetadataRepository(), new FileValidator(10, Collections.singleton("text/plain"), Collections.singleton("txt")));
        FileMetadata m = service.upload(StorageType.LOCAL, new FileUpload("a.txt", "text/plain", 3, new ByteArrayInputStream(bytes)));
        byte[] actual = new byte[3];
        InputStream in = service.download(m.getId());
        assertEquals(3, in.read(actual));
        in.close();
        assertArrayEquals(bytes, actual);
        assertEquals("file", service.accessUrl(m.getId(), new Date(System.currentTimeMillis() + 1000)).getProtocol());
        service.delete(m.getId());
        assertThrows(FileException.class, () -> service.metadata(m.getId()));
    }

    @Test
    void validatesAndCleansTemporaryFile() {
        FileValidator v = new FileValidator(2, null, null);
        assertThrows(FileException.class, () -> v.validate(new FileUpload("a", null, 3, new ByteArrayInputStream(new byte[3]))));
        TemporaryFileManager.TemporaryFile f = new TemporaryFileManager(dir).create(new ByteArrayInputStream(new byte[1]), ".bin");
        Path p = f.getPath();
        assertTrue(Files.exists(p));
        f.close();
        assertFalse(Files.exists(p));
    }
}
