package io.yak.framework.notification;

import java.util.List;

/**
 * 通知统一入口。
 *
 * @author weifuwan
 */
public interface NotificationService {
    List<NotificationRecord> send(NotificationRequest request);
}
