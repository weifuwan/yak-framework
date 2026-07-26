package io.yak.framework.notification;

/**
 * 发送记录保存扩展点。
 *
 * @author weifuwan
 */
public interface NotificationRecordRepository {
    void save(NotificationRecord record);
}
