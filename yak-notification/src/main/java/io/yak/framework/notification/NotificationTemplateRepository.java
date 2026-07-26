package io.yak.framework.notification;

/**
 * 模板查询扩展点，业务项目可接入数据库或配置中心。
 *
 * @author weifuwan
 */
public interface NotificationTemplateRepository {
    NotificationTemplate findByCode(String code);
}
