package io.yak.framework.file;
import java.io.InputStream;
/** 上传参数，输入流由调用方关闭。 @author weifuwan */
public class FileUpload {
 private final String name,type; private final long size; private final InputStream stream;
 public FileUpload(String name,String type,long size,InputStream stream){this.name=name;this.type=type;this.size=size;this.stream=stream;}
 public String getName(){return name;} public String getContentType(){return type;} public long getSize(){return size;} public InputStream getStream(){return stream;}
}
