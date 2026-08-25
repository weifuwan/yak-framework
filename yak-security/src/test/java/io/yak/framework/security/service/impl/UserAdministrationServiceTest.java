package io.yak.framework.security.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.yak.framework.security.authentication.AuthenticationManager;
import io.yak.framework.security.common.dto.user.UserPasswordResetDTO;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.dao.UserDao;
import io.yak.framework.security.dao.mapper.UserMapper;
import io.yak.framework.security.extend.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;

class UserAdministrationServiceTest {

  private UserDao userDao;
  private UserMapper userMapper;
  private AuthenticationManager authenticationManager;
  private UserAdministrationService service;

  @BeforeEach
  @SuppressWarnings("unchecked")
  void setUp() {
    userDao = mock(UserDao.class);
    userMapper = mock(UserMapper.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    authenticationManager = mock(AuthenticationManager.class);
    ObjectProvider<AuthenticationManager> provider =
            mock(ObjectProvider.class);

    when(provider.getIfAvailable())
            .thenReturn(authenticationManager);
    when(passwordEncoder.encode(any()))
            .thenReturn("encoded-password");

    service = new UserAdministrationService(
            userDao,
            userMapper,
            passwordEncoder,
            provider);
  }

  @Test
  void resetPasswordShouldInvalidateAllLoginStates() {
    User user = user(42L, "yak");
    UserPasswordResetDTO request =
            new UserPasswordResetDTO();
    request.setPassword("NewPassword123");

    when(userDao.selectByUserId(42L))
            .thenReturn(user);
    when(userMapper.update(any(), any()))
            .thenReturn(1);

    service.resetPassword(
            42L,
            request,
            "root");

    verify(authenticationManager)
            .logoutUser(42L);
  }

  @Test
  void forceLogoutShouldInvalidateAllLoginStates() {
    when(userDao.selectByUserId(42L))
            .thenReturn(user(42L, "yak"));

    service.forceLogout(42L, "root");

    verify(authenticationManager)
            .logoutUser(42L);
  }

  @Test
  void editPasswordHookShouldResolveUserAndInvalidateSessions() {
    when(userDao.selectByUsername("yak"))
            .thenReturn(user(42L, "yak"));

    service.invalidateSessionsAfterPasswordChange(
            "yak",
            "root");

    verify(authenticationManager)
            .logoutUser(42L);
  }

  private static User user(
          Long id,
          String username) {
    User user = new User();
    user.setId(id);
    user.setUserName(username);
    return user;
  }
}
