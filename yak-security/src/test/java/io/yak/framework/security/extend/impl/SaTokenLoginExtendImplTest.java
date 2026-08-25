package io.yak.framework.security.extend.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.yak.framework.security.authentication.AuthenticationManager;
import io.yak.framework.security.common.dto.account.AccountLoginDTO;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.config.YakSecurityProperties;
import io.yak.framework.security.extend.PasswordEncoder;
import io.yak.framework.security.service.UserService;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class SaTokenLoginExtendImplTest {

  @Test
  void shouldEstablishSaTokenLoginAfterCredentialValidation() {
    UserService userService = mock(UserService.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    AuthenticationManager authenticationManager =
            mock(AuthenticationManager.class);
    User user = user();
    when(userService.getUserByUsername("yakadmin"))
            .thenReturn(user);
    when(passwordEncoder.matches("secret", "hash-v1"))
            .thenReturn(true);

    SaTokenLoginExtendImpl loginExtend =
            new SaTokenLoginExtendImpl(
                    userService,
                    passwordEncoder,
                    new YakSecurityProperties(),
                    authenticationManager);

    AccountLoginDTO loginDTO = new AccountLoginDTO();
    loginDTO.setUserName("yakadmin");
    loginDTO.setPw("secret");

    UserBriefVO result = loginExtend.verifyLogin(
            loginDTO,
            new MockHttpServletRequest(),
            new MockHttpServletResponse());

    assertThat(result.getId()).isEqualTo(42L);
    verify(authenticationManager).login(
            42L,
            "yakadmin",
            "hash-v1");
  }

  @Test
  void shouldReuseCredentialVersionValidationForSaTokenRequests()
          throws Exception {
    UserService userService = mock(UserService.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    AuthenticationManager authenticationManager =
            mock(AuthenticationManager.class);
    User user = user();
    when(userService.getUserByUsername("yakadmin"))
            .thenReturn(user);
    when(authenticationManager.isLogin()).thenReturn(true);
    when(authenticationManager.getLoginUserId()).thenReturn(42L);
    when(authenticationManager.getLoginUsername()).thenReturn("yakadmin");
    when(authenticationManager.getCredentialVersion())
            .thenReturn("hash-v1");

    SaTokenLoginExtendImpl loginExtend =
            new SaTokenLoginExtendImpl(
                    userService,
                    passwordEncoder,
                    new YakSecurityProperties(),
                    authenticationManager);

    boolean allowed = loginExtend.interceptorCheck(
            new MockHttpServletRequest(),
            new MockHttpServletResponse(),
            "/api/private",
            Collections.emptyList());

    assertThat(allowed).isTrue();
  }

  @Test
  void shouldLogoutWhenCredentialVersionChanges()
          throws Exception {
    UserService userService = mock(UserService.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    AuthenticationManager authenticationManager =
            mock(AuthenticationManager.class);
    User user = user();
    when(userService.getUserByUsername("yakadmin"))
            .thenReturn(user);
    when(authenticationManager.isLogin()).thenReturn(true);
    when(authenticationManager.getLoginUserId()).thenReturn(42L);
    when(authenticationManager.getLoginUsername()).thenReturn("yakadmin");
    when(authenticationManager.getCredentialVersion())
            .thenReturn("old-hash");

    SaTokenLoginExtendImpl loginExtend =
            new SaTokenLoginExtendImpl(
                    userService,
                    passwordEncoder,
                    new YakSecurityProperties(),
                    authenticationManager);
    MockHttpServletResponse response =
            new MockHttpServletResponse();

    boolean allowed = loginExtend.interceptorCheck(
            new MockHttpServletRequest(),
            response,
            "/api/private",
            Collections.emptyList());

    assertThat(allowed).isFalse();
    assertThat(response.getStatus()).isEqualTo(401);
    verify(authenticationManager).logout();
  }

  private User user() {
    User user = new User();
    user.setId(42L);
    user.setUserName("yakadmin");
    user.setPw("hash-v1");
    user.setStatus(1);
    return user;
  }
}
