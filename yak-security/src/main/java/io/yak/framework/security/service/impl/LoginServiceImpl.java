package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.account.AccountLoginDTO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.extend.LoginExtend;
import io.yak.framework.security.extend.LoginExtendBeanTool;
import io.yak.framework.security.service.LoginService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * 登录服务实现类。
 *
 * <p>将登录认证、退出登录和拦截检查委派给当前配置的
 * {@link LoginExtend} 实现。
 *
 * @author weifuwan
 */
@Service("yakSecurityLoginServiceImpl")
public class LoginServiceImpl implements LoginService {

  private final LoginExtendBeanTool loginExtendBeanTool;

  /**
   * 创建登录服务。
   *
   * @param loginExtendBeanTool 登录扩展获取工具
   */
  public LoginServiceImpl(
          LoginExtendBeanTool loginExtendBeanTool) {

    this.loginExtendBeanTool = loginExtendBeanTool;
  }

  /**
   * 校验登录信息并返回当前用户。
   *
   * @param loginDTO 登录信息
   * @param request HTTP 请求
   * @param response HTTP 响应
   * @return 当前登录用户
   */
  @Override
  public UserBriefVO verifyLogin(
          AccountLoginDTO loginDTO,
          HttpServletRequest request,
          HttpServletResponse response) {

    if (loginDTO == null) {
      throw new IllegalArgumentException(
              "登录信息不能为空");
    }

    if (request == null || response == null) {
      throw new IllegalArgumentException(
              "HTTP 请求和响应不能为空");
    }

    return getLoginExtend().verifyLogin(
            loginDTO,
            request,
            response);
  }

  /**
   * 退出登录。
   *
   * @param request HTTP 请求
   * @param response HTTP 响应
   * @return 退出结果
   */
  @Override
  public Result<Boolean> logout(
          HttpServletRequest request,
          HttpServletResponse response) {

    if (request == null || response == null) {
      throw new IllegalArgumentException(
              "HTTP 请求和响应不能为空");
    }

    return getLoginExtend().logout(
            request,
            response);
  }

  /**
   * 执行登录拦截校验。
   *
   * @param request HTTP 请求
   * @param response HTTP 响应
   * @param requestMappingValue 当前请求路径
   * @param whiteMappingValues 白名单路径列表
   * @return 是否允许继续访问
   * @throws IOException 写入响应失败
   */
  @Override
  public boolean interceptorCheck(
          HttpServletRequest request,
          HttpServletResponse response,
          String requestMappingValue,
          List<String> whiteMappingValues)
          throws IOException {

    if (request == null || response == null) {
      throw new IllegalArgumentException(
              "HTTP 请求和响应不能为空");
    }

    List<String> safeWhiteMappingValues =
            whiteMappingValues == null
                    ? Collections.emptyList()
                    : whiteMappingValues;

    return getLoginExtend().interceptorCheck(
            request,
            response,
            requestMappingValue,
            safeWhiteMappingValues);
  }

  /**
   * 获取当前登录扩展实现。
   *
   * @return 登录扩展实现
   */
  private LoginExtend getLoginExtend() {
    LoginExtend loginExtend =
            loginExtendBeanTool
                    .getLoginExtendImpl();

    if (loginExtend == null) {
      throw new IllegalStateException(
              "未配置登录扩展实现");
    }

    return loginExtend;
  }
}