package io.yak.framework.security.autoconfigure;

import io.yak.framework.security.controller.v1.CommonController;
import io.yak.framework.security.controller.v1.ConfigController;
import io.yak.framework.security.controller.v1.DeptController;
import io.yak.framework.security.controller.v1.LoginController;
import io.yak.framework.security.controller.v1.MessageController;
import io.yak.framework.security.controller.v1.PermissionController;
import io.yak.framework.security.controller.v1.ProjectController;
import io.yak.framework.security.controller.v1.ResourceController;
import io.yak.framework.security.controller.v1.RoleController;
import io.yak.framework.security.controller.v1.UserController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(
        prefix = "yak.security",
        name = {
                "database-enabled",
                "web-enabled"
        },
        havingValue = "true",
        matchIfMissing = true
)
@Import({
        CommonController.class,
        ConfigController.class,
        DeptController.class,
        LoginController.class,
        MessageController.class,
        PermissionController.class,
        ProjectController.class,
        ResourceController.class,
        RoleController.class,
        UserController.class
})
public class YakSecurityWebConfiguration {
}