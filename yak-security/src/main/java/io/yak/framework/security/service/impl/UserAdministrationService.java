package io.yak.framework.security.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.yak.framework.security.common.dto.user.UserPasswordResetDTO;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.common.enums.ResultCode;
import io.yak.framework.security.common.po.UserPO;
import io.yak.framework.security.dao.UserDao;
import io.yak.framework.security.dao.mapper.UserMapper;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.extend.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 用户管理补充服务。
 *
 * <p>承载不适合通过完整用户编辑接口完成的管理操作，避免密码重置时
 * 覆盖用户资料、角色等并发变更。
 *
 * @author weifuwan
 */
@Service
public class UserAdministrationService {

  private static final Logger LOGGER =
          LoggerFactory.getLogger(UserAdministrationService.class);

  private static final int MIN_PASSWORD_LENGTH = 8;
  private static final int MAX_PASSWORD_LENGTH = 64;

  private final UserDao userDao;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  public UserAdministrationService(
          UserDao userDao,
          UserMapper userMapper,
          PasswordEncoder passwordEncoder) {

    this.userDao = userDao;
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * 校验当前用户不能删除自己。
   *
   * @param targetUserId 待删除用户 ID
   * @param operatorId 当前登录用户 ID
   */
  public void validateDelete(
          Long targetUserId,
          Long operatorId) {

    if (targetUserId == null) {
      throw new YakSecurityException(
              ResultCode.USER_ID_CANNOT_BE_NULL);
    }

    if (operatorId != null
            && Objects.equals(targetUserId, operatorId)) {

      throw new YakSecurityException(
              "不能删除当前登录用户");
    }
  }

  /**
   * 管理员重置指定用户密码。
   *
   * @param userId 用户 ID
   * @param request 重置密码请求
   * @param operator 操作人
   */
  @Transactional(
          transactionManager =
                  "yakSecurityTransactionManager",
          rollbackFor = Exception.class)
  public void resetPassword(
          Long userId,
          UserPasswordResetDTO request,
          String operator) {

    if (userId == null) {
      throw new YakSecurityException(
              ResultCode.USER_ID_CANNOT_BE_NULL);
    }

    String password =
            request == null
                    ? null
                    : request.getPassword();

    if (!StringUtils.hasText(password)) {
      throw new YakSecurityException(
              "新密码不能为空");
    }

    if (password.length() < MIN_PASSWORD_LENGTH
            || password.length() > MAX_PASSWORD_LENGTH) {

      throw new YakSecurityException(
              "密码长度必须为 8～64 位");
    }

    User user = userDao.selectByUserId(userId);
    if (user == null) {
      throw new YakSecurityException(
              ResultCode.USER_NOT_EXISTS);
    }

    String encodedPassword =
            passwordEncoder.encode(password);

    int affectedRows =
            userMapper.update(
                    null,
                    Wrappers.<UserPO>lambdaUpdate()
                            .eq(UserPO::getId, userId)
                            .set(UserPO::getPw, encodedPassword));

    if (affectedRows != 1) {
      throw new YakSecurityException(
              ResultCode.USER_ACCOUNT_UPDATE_FAIL);
    }

    LOGGER.info(
            "管理员重置用户密码成功，用户ID={}，用户名={}，操作人={}",
            userId,
            user.getUserName(),
            operator);
  }
}
