package io.yak.framework.notification;

/**
 * 渠道发送器，由业务项目按实际基础设施实现。
 *
 * @author weifuwan
 */
public interface NotificationSender {
    NotificationChannel channel();
    void send(RenderedNotification notification) throws Exception;
}
