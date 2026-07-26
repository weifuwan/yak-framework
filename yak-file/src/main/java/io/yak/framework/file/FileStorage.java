package io.yak.framework.file;
import java.io.InputStream; import java.net.URL; import java.util.Date;
/** 本地、MinIO、OSS 均实现的存储接口。 @author weifuwan */
public interface FileStorage {
 StorageType type(); void upload(String objectName,FileUpload file); InputStream download(String objectName); void delete(String objectName); URL accessUrl(String objectName,Date expiresAt);
}
