package io.yak.framework.file;
import java.util.Collections; import java.util.HashSet; import java.util.Locale; import java.util.Set;
/** 文件大小、MIME 类型和扩展名白名单校验器。 @author weifuwan */
public class FileValidator {
 private final long max; private final Set<String> types,extensions;
 public FileValidator(long max,Set<String> types,Set<String> extensions){this.max=max;this.types=lower(types);this.extensions=lower(extensions);}
 public static FileValidator unlimited(){return new FileValidator(-1,null,null);}
 public void validate(FileUpload f){if(f==null||f.getStream()==null)throw new FileException("上传文件不能为空");if(f.getSize()<0)throw new FileException("文件大小不能小于 0");if(max>=0&&f.getSize()>max)throw new FileException("文件大小超过限制: "+max);if(!types.isEmpty()&&!types.contains(low(f.getContentType())))throw new FileException("不支持的文件类型");String n=low(f.getName());int i=n.lastIndexOf('.');String x=i<0?"":n.substring(i+1);if(!extensions.isEmpty()&&!extensions.contains(x))throw new FileException("不支持的文件扩展名");}
 private static Set<String> lower(Set<String> s){if(s==null)return Collections.emptySet();Set<String> r=new HashSet<String>();for(String v:s)if(v!=null)r.add(low(v).replaceFirst("^\\.",""));return r;} private static String low(String s){return s==null?"":s.toLowerCase(Locale.ROOT);}
}
