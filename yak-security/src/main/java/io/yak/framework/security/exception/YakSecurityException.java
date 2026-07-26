package io.yak.framework.security.exception;

import java.io.Serial;

/**
 * Yak Security 模块统一运行时异常。
 *
 * <p>用于封装安全模块中的业务异常，例如用户、角色、权限、
 * 登录认证及配置校验等相关异常。</p>
 */
public class YakSecurityException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 创建一个不包含错误信息的异常。
     */
    public YakSecurityException() {
        super();
    }

    /**
     * 根据错误码定义创建异常。
     *
     * <p>异常消息格式为：</p>
     *
     * <pre>
     * 错误码-错误消息
     * </pre>
     *
     * @param codeMsg 错误码及错误消息定义
     */
    public YakSecurityException(CodeMsg codeMsg) {
        super(codeMsg.getCode() + "-" + codeMsg.getMessage());
    }

    /**
     * 根据指定错误消息创建异常。
     *
     * @param message 异常消息
     */
    public YakSecurityException(String message) {
        super(message);
    }

    /**
     * 根据指定错误消息和原始异常创建异常。
     *
     * @param message 异常消息
     * @param cause   原始异常
     */
    public YakSecurityException(
            String message,
            Throwable cause) {
        super(message, cause);
    }

    /**
     * 根据原始异常创建异常。
     *
     * @param cause 原始异常
     */
    public YakSecurityException(Throwable cause) {
        super(cause);
    }
}