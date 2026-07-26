package io.yak.framework.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * 默认通知服务测试。
 *
 * @author weifuwan
 */
class DefaultNotificationServiceTest {

    @Test
    void shouldRenderTemplateSendAllChannelsAndSaveRecords() {
        final List<RenderedNotification> delivered = new ArrayList<RenderedNotification>();
        final List<NotificationRecord> saved = new ArrayList<NotificationRecord>();
        NotificationTemplateRepository templates = new NotificationTemplateRepository() {
            @Override
            public NotificationTemplate findByCode(String code) {
                return new NotificationTemplate(code, "你好 ${name}", "订单 ${order} 已完成");
            }
        };
        NotificationSender inApp = sender(NotificationChannel.IN_APP, delivered, false);
        NotificationSender email = sender(NotificationChannel.EMAIL, delivered, true);
        DefaultNotificationService service = new DefaultNotificationService(
                templates, saved::add, Arrays.asList(inApp, email));
        Map<String, String> variables = new HashMap<String, String>();
        variables.put("name", "小雅");
        variables.put("order", "A-1");

        List<NotificationRecord> records = service.send(new NotificationRequest("ORDER_DONE", "user-1",
                EnumSet.of(NotificationChannel.IN_APP, NotificationChannel.EMAIL), variables));

        assertEquals(2, records.size());
        assertEquals(2, saved.size());
        assertEquals("你好 小雅", delivered.get(0).getSubject());
        assertEquals("订单 A-1 已完成", delivered.get(0).getContent());
        assertTrue(records.get(0).isSuccess());
        assertFalse(records.get(1).isSuccess());
        assertEquals("发送失败", records.get(1).getErrorMessage());
    }

    private NotificationSender sender(final NotificationChannel channel,
            final List<RenderedNotification> delivered, final boolean fail) {
        return new NotificationSender() {
            @Override
            public NotificationChannel channel() {
                return channel;
            }

            @Override
            public void send(RenderedNotification notification) throws Exception {
                delivered.add(notification);
                if (fail) {
                    throw new Exception("发送失败");
                }
            }
        };
    }
}
