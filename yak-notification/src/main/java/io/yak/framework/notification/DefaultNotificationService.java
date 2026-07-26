package io.yak.framework.notification;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 同步通知服务的默认实现。
 *
 * @author weifuwan
 */
public final class DefaultNotificationService implements NotificationService {
    private final NotificationTemplateRepository templateRepository;
    private final NotificationRecordRepository recordRepository;
    private final Map<NotificationChannel, NotificationSender> senders;
    private final Clock clock;

    public DefaultNotificationService(NotificationTemplateRepository templateRepository,
            NotificationRecordRepository recordRepository, List<NotificationSender> senders) {
        this(templateRepository, recordRepository, senders, Clock.systemUTC());
    }

    DefaultNotificationService(NotificationTemplateRepository templateRepository,
            NotificationRecordRepository recordRepository, List<NotificationSender> senders, Clock clock) {
        if (templateRepository == null || recordRepository == null || senders == null || clock == null) {
            throw new IllegalArgumentException("通知服务依赖不能为空");
        }
        this.templateRepository = templateRepository;
        this.recordRepository = recordRepository;
        this.clock = clock;
        this.senders = new EnumMap<NotificationChannel, NotificationSender>(NotificationChannel.class);
        for (NotificationSender sender : senders) {
            if (sender == null || sender.channel() == null) {
                throw new IllegalArgumentException("发送器及其渠道不能为空");
            }
            if (this.senders.put(sender.channel(), sender) != null) {
                throw new IllegalArgumentException("同一渠道只能注册一个发送器: " + sender.channel());
            }
        }
    }

    @Override
    public List<NotificationRecord> send(NotificationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("通知请求不能为空");
        }
        NotificationTemplate template = templateRepository.findByCode(request.getTemplateCode());
        if (template == null) {
            throw new IllegalArgumentException("通知模板不存在: " + request.getTemplateCode());
        }
        RenderedNotification rendered = new RenderedNotification(request.getRecipient(),
                render(template.getSubject(), request), render(template.getContent(), request));
        List<NotificationRecord> records = new ArrayList<NotificationRecord>();
        for (NotificationChannel channel : request.getChannels()) {
            records.add(sendOne(request, rendered, channel));
        }
        return Collections.unmodifiableList(records);
    }

    private NotificationRecord sendOne(NotificationRequest request, RenderedNotification rendered,
            NotificationChannel channel) {
        boolean success = false;
        String error = null;
        try {
            NotificationSender sender = senders.get(channel);
            if (sender == null) {
                throw new IllegalStateException("未配置渠道发送器: " + channel);
            }
            sender.send(rendered);
            success = true;
        } catch (Exception exception) {
            error = exception.getMessage() == null ? exception.getClass().getName() : exception.getMessage();
        }
        NotificationRecord record = new NotificationRecord(request.getTemplateCode(), request.getRecipient(),
                channel, success, error, Instant.now(clock));
        recordRepository.save(record);
        return record;
    }

    private String render(String text, NotificationRequest request) {
        if (text == null) {
            return null;
        }
        String result = text;
        for (Map.Entry<String, String> variable : request.getVariables().entrySet()) {
            result = result.replace("${" + variable.getKey() + "}",
                    variable.getValue() == null ? "" : variable.getValue());
        }
        return result;
    }
}
