package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.dto.user.UserDTO;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.dao.ProjectDao;
import io.yak.framework.security.dao.UserDao;
import io.yak.framework.security.dao.UserProjectDao;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.extend.PasswordEncoder;
import io.yak.framework.security.service.DeptService;
import io.yak.framework.security.service.PermissionService;
import io.yak.framework.security.service.RolePermissionService;
import io.yak.framework.security.service.RoleService;
import io.yak.framework.security.service.UserRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTransactionTest {

  private UserDao userDao;
  private UserRoleService userRoleService;
  private UserServiceImpl userService;

  @BeforeEach
  void setUp() {
    userDao = mock(UserDao.class);
    userRoleService = mock(UserRoleService.class);
    PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    when(passwordEncoder.encode(any())).thenReturn("encoded-password");

    userService = new UserServiceImpl(
            userDao,
            mock(PermissionService.class),
            mock(RolePermissionService.class),
            mock(DeptService.class),
            mock(RoleService.class),
            userRoleService,
            mock(UserProjectDao.class),
            mock(ProjectDao.class),
            passwordEncoder);
  }

  @Test
  void addUserRethrowsFailureSoTransactionCanRollBack() {
    UserDTO userDTO = userDTO();
    RuntimeException cause = new RuntimeException("role update failed");
    when(userDao.addUser(any())).thenReturn(1);
    doThrow(cause)
            .when(userRoleService)
            .updateUserRoleByUserId(any(), any());

    YakSecurityException exception = assertThrows(
            YakSecurityException.class,
            () -> userService.addUser(userDTO, "operator"));

    assertEquals("2010-用户注册失败", exception.getMessage());
    assertSame(cause, exception.getCause());
  }

  @Test
  void editUserRethrowsFailureSoTransactionCanRollBack() {
    UserDTO userDTO = userDTO();
    User currentUser = new User();
    currentUser.setId(1L);
    RuntimeException cause = new RuntimeException("role update failed");
    when(userDao.selectByUsername(userDTO.getUserName()))
            .thenReturn(currentUser);
    when(userDao.editUser(any())).thenReturn(1);
    doThrow(cause)
            .when(userRoleService)
            .updateUserRoleByUserId(any(), any());

    YakSecurityException exception = assertThrows(
            YakSecurityException.class,
            () -> userService.editUser(userDTO, "operator"));

    assertEquals("2018-用户更新失败", exception.getMessage());
    assertSame(cause, exception.getCause());
  }

  private UserDTO userDTO() {
    UserDTO userDTO = new UserDTO();
    userDTO.setUserName("test_user");
    userDTO.setPw("password");
    userDTO.setRoleIds(Collections.singletonList(1L));
    return userDTO;
  }
}
