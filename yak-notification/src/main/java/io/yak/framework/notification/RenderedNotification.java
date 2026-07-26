package io.yak.framework.notification;

/**
 * 已完成模板渲染、可交给渠道发送器的数据。
 *
 * @author weifuwan
 */
public final class RenderedNotification {
    private final String recipient;
    private final String subject;
    private final String content;

    public RenderedNotification(String recipient, String subject, String content) {
        this.recipient = recipient;
        this.subject = subject;
        this.content = content;
    }

    public String getRecipient() { return recipient; }
    public String getSubject() { return subject; }
    public String getContent() { return content; }
}
