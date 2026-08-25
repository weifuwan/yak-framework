package io.yak.framework.security.extend.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.yak.framework.security.authentication.AuthenticationManager;
import io.yak.framework.security.common.dto.account.AccountLoginDTO;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.config.YakSecurityProperties;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.extend.PasswordEncoder;
import io.yak.framework.security.service.UserService;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class DefaultLoginExtendImplTest {

  @Test
  void authenticationRejectsClientIdentityHeaders() throws Exception {
    AuthenticationManager manager = mock(AuthenticationManager.class);
    DefaultLoginExtendImpl extension = extension(
            mock(UserService.class), mock(PasswordEncoder.class), manager);
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/private");
    request.addHeader("X-SSO-USER", "attacker");
    request.addHeader("X-SSO-USER-ID", "999");

    assertFalse(extension.interceptorCheck(
            request,
            new MockHttpServletResponse(),
            "/private",
            Collections.emptyList()));
  }

  @Test
  void missingAccountUsesSameErrorAsBadPassword() {
    UserService userService = mock(UserService.class);
    DefaultLoginExtendImpl extension = extension(
            userService, mock(PasswordEncoder.class), mock(AuthenticationManager.class));

    YakSecurityException exception = assertThrows(
            YakSecurityException.class,
            () -> extension.verifyLogin(
                    login("unknown", "secret"),
                    new MockHttpServletRequest(),
                    new MockHttpServletResponse()));

    assertEquals("2003-密码错误", exception.getMessage());
  }

  @Test
  void successfulLoginDelegatesStateToAuthenticationManager() {
    UserService userService = mock(UserService.class);
    PasswordEncoder encoder = mock(PasswordEncoder.class);
    AuthenticationManager manager = mock(AuthenticationManager.class);
    User user = new User();
    user.setId(7L);
    user.setUserName("alice");
    user.setPw("hash-v1");
    when(userService.getUserByUsername("alice")).thenReturn(user);
    when(encoder.matches("secret", "hash-v1")).thenReturn(true);

    DefaultLoginExtendImpl extension = extension(userService, encoder, manager);
    extension.verifyLogin(
            login("alice", "secret"),
            new MockHttpServletRequest(),
            new MockHttpServletResponse());

    verify(manager).login(7L, "alice");
  }

  private DefaultLoginExtendImpl extension(
          UserService userService,
          PasswordEncoder encoder,
          AuthenticationManager manager) {
    return new DefaultLoginExtendImpl(
            userService,
            encoder,
            new YakSecurityProperties(),
            manager);
  }

  private AccountLoginDTO login(String username, String password) {
    AccountLoginDTO login = new AccountLoginDTO();
    login.setUserName(username);
    login.setPw(password);
    return login;
  }
}
