package io.yak.framework.security.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.Duration;

/**
 * Yak Security 模块配置。
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = YakSecurityProperties.PREFIX)
public class YakSecurityProperties {

  public static final String PREFIX = "yak.security";

  /**
   * 是否启用 Yak Security。
   */
  private boolean enabled = true;

  /**
   * 是否启用数据库相关功能。
   */
  private boolean databaseEnabled = true;

  /**
   * 是否启用 Web 接口。
   */
  private boolean webEnabled = true;

  /**
   * 是否启用统一登录拦截。关闭后所有接口均不校验登录状态。
   */
  private boolean authenticationEnabled = true;

  /**
   * 无需登录即可访问的 Ant 风格路径。登录接口和健康检查默认公开。
   */
  private List<String> publicPaths = new ArrayList<String>(Arrays.asList(
          "/yak-security/api/v1/account/login",
          "/yak-security/api/v1/common/heart",
          "/v3/api-docs/**",
          "/swagger-ui/**",
          "/swagger-ui.html"
  ));

  /**
   * 是否启用审计功能。
   */
  private boolean auditEnabled = true;

  /**
   * 用于隔离安全数据的应用名称。
   *
   * <p>该值将写入数据库的 app_name 字段，
   * 用于不同应用之间的数据隔离。</p>
   */
  private String applicationName;

  /**
   * Yak Security 独立数据源配置。
   *
   * <p>保持为 final，避免整个数据源配置对象被替换，
   * Spring Boot 仍然可以绑定其内部属性。</p>
   */
  private final DataSourceProperties datasource =
          new DataSourceProperties();

  /** First-user administrator bootstrap settings. */
  private final BootstrapProperties bootstrap =
          new BootstrapProperties();

  /** Declarative permission synchronization settings. */
  private final PermissionRegistrationProperties permissionRegistration =
          new PermissionRegistrationProperties();

  /** User permission cache settings. */
  private final PermissionCacheProperties permissionCache =
          new PermissionCacheProperties();

  /** Login brute-force protection settings. */
  private final LoginSecurityProperties login =
          new LoginSecurityProperties();

  /** HTTP session security settings. */
  private final SessionSecurityProperties session =
          new SessionSecurityProperties();

  @Getter
  @Setter
  @ToString
  public static class LoginSecurityProperties {
    /** Failed attempts allowed for either a username or an IP address. */
    private int maxFailureCount = 5;
    /** Period during which a blocked username or IP address cannot retry. */
    private Duration lockDuration = Duration.ofMinutes(15);
    /** Do not reveal whether a submitted username exists. */
    private boolean hideAccountNotFound = true;
  }

  @Getter
  @Setter
  @ToString
  public static class SessionSecurityProperties {
    /** Maximum idle time for an authenticated session. */
    private Duration timeout = Duration.ofMinutes(30);
  }

  @Getter
  @Setter
  @ToString
  public static class PermissionCacheProperties {
    /** Whether the local permission cache is enabled. */
    private boolean enabled = true;
    /** Minutes after which an entry expires. */
    private long ttlMinutes = 20;
    /** Maximum number of users retained in memory. */
    private long maximumSize = 10_000;
  }

  @Getter
  @Setter
  @ToString
  public static class PermissionRegistrationProperties {
    /** Whether declarations are synchronized to the permission table at startup. */
    private boolean enabled = true;
  }

  @Getter
  @Setter
  @ToString
  public static class BootstrapProperties {
    /** Whether to create an administrator when the user table is empty. */
    private boolean enabled = false;
    /** Administrator login name. */
    private String username = "admin";
    /** Administrator password. There is deliberately no default value. */
    @ToString.Exclude
    private String password;
    /** Administrator display name. */
    private String realName = "系统管理员";
  }

  /**
   * 校验数据库相关配置。
   *
   * <p>仅在安全模块、数据库和独立数据源均启用时执行校验。</p>
   */
  public void validateDatabaseConfiguration() {
    if (!enabled
            || !databaseEnabled
            || !datasource.isEnabled()) {
      return;
    }

    requireText(
            applicationName,
            PREFIX + ".application-name"
    );

    datasource.validate();
  }

  /**
   * Yak Security 使用的 Druid 数据源参数。
   */
  @Getter
  @Setter
  @ToString
  public static class DataSourceProperties {

    /**
     * 是否启用 Yak Security 独立数据源。
     */
    private boolean enabled = true;

    /**
     * 数据库 JDBC 连接地址。
     */
    private String url;

    /**
     * 数据库登录用户名。
     */
    private String username;

    /**
     * 数据库登录密码。
     *
     * <p>禁止输出到日志。</p>
     */
    @ToString.Exclude
    private String password;

    /**
     * JDBC 驱动类名。
     */
    private String driverClassName =
            "com.mysql.cj.jdbc.Driver";

    /**
     * 连接池初始化连接数。
     */
    private int initialSize = 1;

    /**
     * 连接池最小空闲连接数。
     */
    private int minIdle = 1;

    /**
     * 连接池最大活跃连接数。
     */
    private int maxActive = 8;

    /**
     * 获取连接的最大等待时间，单位为毫秒。
     */
    private long maxWait = 60_000L;

    /**
     * 检测连接有效性的 SQL。
     */
    private String validationQuery = "SELECT 1";

    /**
     * 是否在空闲连接检测时验证连接。
     */
    private boolean testWhileIdle = true;

    /**
     * 是否在借出连接时验证连接。
     */
    private boolean testOnBorrow = false;

    /**
     * 是否在归还连接时验证连接。
     */
    private boolean testOnReturn = false;

    /**
     * 校验数据源配置。
     */
    private void validate() {
      requireText(
              url,
              PREFIX + ".datasource.url"
      );

      requireText(
              username,
              PREFIX + ".datasource.username"
      );

      requireText(
              driverClassName,
              PREFIX + ".datasource.driver-class-name"
      );

      if (initialSize < 0) {
        throw invalidProperty(
                PREFIX + ".datasource.initial-size",
                "must be greater than or equal to 0"
        );
      }

      if (minIdle < 0) {
        throw invalidProperty(
                PREFIX + ".datasource.min-idle",
                "must be greater than or equal to 0"
        );
      }

      if (maxActive <= 0) {
        throw invalidProperty(
                PREFIX + ".datasource.max-active",
                "must be greater than 0"
        );
      }

      if (initialSize > maxActive) {
        throw invalidProperty(
                PREFIX + ".datasource.initial-size",
                "must not be greater than max-active"
        );
      }

      if (minIdle > maxActive) {
        throw invalidProperty(
                PREFIX + ".datasource.min-idle",
                "must not be greater than max-active"
        );
      }

      /*
       * 允许：
       * -1：由 Druid 使用其特殊等待策略；
       * >= 0：明确的最大等待毫秒数。
       */
      if (maxWait < -1L) {
        throw invalidProperty(
                PREFIX + ".datasource.max-wait",
                "must be -1 or greater than or equal to 0"
        );
      }

      boolean connectionValidationEnabled =
              testWhileIdle
                      || testOnBorrow
                      || testOnReturn;

      if (connectionValidationEnabled
              && !StringUtils.hasText(validationQuery)) {

        throw invalidProperty(
                PREFIX + ".datasource.validation-query",
                "must not be blank when connection validation is enabled"
        );
      }
    }
  }

  private static void requireText(
          String value,
          String key) {

    if (!StringUtils.hasText(value)) {
      throw new IllegalStateException(
              "Missing required configuration: " + key
      );
    }
  }

  private static IllegalStateException invalidProperty(
          String key,
          String message) {

    return new IllegalStateException(
            "Invalid configuration: "
                    + key
                    + " "
                    + message
    );
  }
}
