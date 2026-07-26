package io.yak.framework.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.yak.framework.security.common.dto.account.AccountLoginDTO;
import io.yak.framework.security.common.dto.user.UserDTO;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.common.po.UserPO;
import io.yak.framework.security.config.YakSecurityProperties;
import io.yak.framework.security.extend.impl.DefaultPasswordEncoder;
import io.yak.framework.security.extend.impl.DefaultLoginExtendImpl;
import io.yak.framework.security.service.UserService;
import io.yak.framework.security.util.SensitiveDataSanitizer;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PasswordSecurityTests {
  @Test
  void bcryptNeverStoresPlaintextAndCanVerify() {
    DefaultPasswordEncoder encoder = new DefaultPasswordEncoder();
    String encoded = encoder.encode("correct horse battery staple");

    assertThat(encoded).isNotEqualTo("correct horse battery staple").startsWith("$2");
    assertThat(encoder.matches("correct horse battery staple", encoded)).isTrue();
    assertThat(encoder.matches("wrong", encoded)).isFalse();
  }

  @Test
  void sensitiveObjectsNeverRenderSecrets() {
    AccountLoginDTO login = new AccountLoginDTO();
    login.setPw("login-secret");
    UserDTO dto = new UserDTO();
    dto.setPw("dto-secret");
    User user = new User();
    user.setPw("entity-secret");
    user.setSalt("entity-salt");
    UserPO po = new UserPO();
    po.setPw("po-secret");
    po.setSalt("po-salt");
    YakSecurityProperties properties = new YakSecurityProperties();
    properties.getDatasource().setPassword("database-secret");

    assertThat(login.toString()).doesNotContain("login-secret", "pw=");
    assertThat(dto.toString()).doesNotContain("dto-secret", "pw=");
    assertThat(user.toString()).doesNotContain("entity-secret", "entity-salt", "pw=", "salt=");
    assertThat(po.toString()).doesNotContain("po-secret", "po-salt", "pw=", "salt=");
    assertThat(properties.toString()).doesNotContain("database-secret", "password=");
  }

  @Test
  void auditTextRedactsPasswordsAndTokens() {
    String detail = "password=hunter2, Authorization: Bearer token-value, token=abc123";
    String sanitized = SensitiveDataSanitizer.sanitize(detail);

    assertThat(sanitized).doesNotContain("hunter2", "token-value", "abc123");
    assertThat(sanitized).contains("[REDACTED]");
  }

  @Test
  void disabledUserCannotLogin() {
    UserService users = mock(UserService.class);
    User disabled = new User();
    disabled.setStatus(2);
    disabled.setPw(new DefaultPasswordEncoder().encode("password"));
    when(users.getUserByUserName("disabled-user")).thenReturn(disabled);
    DefaultLoginExtendImpl loginService = new DefaultLoginExtendImpl();
    ReflectionTestUtils.setField(loginService, "userService", users);
    ReflectionTestUtils.setField(loginService, "passwordEncoder", new DefaultPasswordEncoder());
    AccountLoginDTO login = new AccountLoginDTO();
    login.setUserName("disabled-user");
    login.setPw("password");

    assertThatThrownBy(() -> loginService.verifyLogin(login, null, null))
        .hasMessageContaining("2005");
  }
}
