package io.yak.framework.notification;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 一次业务通知请求。
 *
 * @author weifuwan
 */
public final class NotificationRequest {
    private final String templateCode;
    private final String recipient;
    private final Set<NotificationChannel> channels;
    private final Map<String, String> variables;

    public NotificationRequest(String templateCode, String recipient,
            Set<NotificationChannel> channels, Map<String, String> variables) {
        if (isBlank(templateCode) || isBlank(recipient)) {
            throw new IllegalArgumentException("模板编码和接收人不能为空");
        }
        if (channels == null || channels.isEmpty()) {
            throw new IllegalArgumentException("至少需要一个通知渠道");
        }
        this.templateCode = templateCode;
        this.recipient = recipient;
        this.channels = Collections.unmodifiableSet(EnumSet.copyOf(channels));
        this.variables = variables == null
                ? Collections.<String, String>emptyMap()
                : Collections.unmodifiableMap(new HashMap<String, String>(variables));
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public String getTemplateCode() { return templateCode; }
    public String getRecipient() { return recipient; }
    public Set<NotificationChannel> getChannels() { return channels; }
    public Map<String, String> getVariables() { return variables; }
}
