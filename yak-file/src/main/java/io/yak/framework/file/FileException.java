package io.yak.framework.file;
/** 文件模块统一异常。 @author weifuwan */
public class FileException extends RuntimeException {
 public FileException(String message) { super(message); }
 public FileException(String message, Throwable cause) { super(message, cause); }
}
