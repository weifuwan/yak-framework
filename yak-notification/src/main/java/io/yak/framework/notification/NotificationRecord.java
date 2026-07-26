package io.yak.framework.notification;

import java.time.Instant;

/**
 * 单个渠道的消息发送记录。
 *
 * @author weifuwan
 */
public final class NotificationRecord {
    private final String templateCode;
    private final String recipient;
    private final NotificationChannel channel;
    private final boolean success;
    private final String errorMessage;
    private final Instant sentAt;

    public NotificationRecord(String templateCode, String recipient, NotificationChannel channel,
            boolean success, String errorMessage, Instant sentAt) {
        this.templateCode = templateCode;
        this.recipient = recipient;
        this.channel = channel;
        this.success = success;
        this.errorMessage = errorMessage;
        this.sentAt = sentAt;
    }

    public String getTemplateCode() { return templateCode; }
    public String getRecipient() { return recipient; }
    public NotificationChannel getChannel() { return channel; }
    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
    public Instant getSentAt() { return sentAt; }
}
