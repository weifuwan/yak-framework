package io.yak.framework.file;
import java.io.InputStream; import java.net.URL; import java.util.Date;
/**
 * MinIO/OSS SDK 适配器。应用通过客户端桥接接口隔离具体 SDK 版本。
 * @author weifuwan
 */
public class DelegatingObjectStorage implements FileStorage {
 /** 对象存储客户端的最小桥接契约。 @author weifuwan */
 public interface Client { void put(String name,FileUpload file) throws Exception; InputStream get(String name) throws Exception; void remove(String name)throws Exception; URL sign(String name,Date expiresAt)throws Exception; }
 private final StorageType type; private final Client client;
 public DelegatingObjectStorage(StorageType type,Client client){if(type!=StorageType.MINIO&&type!=StorageType.OSS)throw new IllegalArgumentException("仅支持 MINIO 或 OSS");this.type=type;this.client=client;}
 public StorageType type(){return type;} public void upload(String n,FileUpload f){try{client.put(n,f);}catch(Exception e){throw new FileException(type+" 上传失败",e);}} public InputStream download(String n){try{return client.get(n);}catch(Exception e){throw new FileException(type+" 下载失败",e);}} public void delete(String n){try{client.remove(n);}catch(Exception e){throw new FileException(type+" 删除失败",e);}} public URL accessUrl(String n,Date d){try{return client.sign(n,d);}catch(Exception e){throw new FileException(type+" 生成访问地址失败",e);}}
}
