package io.yak.framework.notification;

/**
 * 通知模板。
 *
 * @author weifuwan
 */
public final class NotificationTemplate {
    private final String code;
    private final String subject;
    private final String content;

    public NotificationTemplate(String code, String subject, String content) {
        if (code == null || code.trim().isEmpty() || content == null) {
            throw new IllegalArgumentException("模板编码和内容不能为空");
        }
        this.code = code;
        this.subject = subject;
        this.content = content;
    }

    public String getCode() { return code; }
    public String getSubject() { return subject; }
    public String getContent() { return content; }
}
