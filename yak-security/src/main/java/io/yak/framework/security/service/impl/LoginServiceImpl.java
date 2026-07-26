package io.yak.framework.security.service.impl;

import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.account.AccountLoginDTO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.extend.LoginExtendBeanTool;
import io.yak.framework.security.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/**
 * 登录服务入口，将认证、登出和拦截检查委派给已配置的登录扩展实现。
 */
public class LoginServiceImpl implements LoginService {
  @Autowired private LoginExtendBeanTool loginExtendBeanTool;

  @Override
  public UserBriefVO verifyLogin(AccountLoginDTO loginDTO,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
      throws YakSecurityException {
    return this.loginExtendBeanTool.getLoginExtendImpl().verifyLogin(
        loginDTO, request, response);
  }

  @Override
  public Result<Boolean> logout(HttpServletRequest request,
                                HttpServletResponse response) {
    return this.loginExtendBeanTool.getLoginExtendImpl().logout(request,
                                                                response);
  }

  @Override
  public boolean
  interceptorCheck(HttpServletRequest request, HttpServletResponse response,
                   String requestMappingValue, List<String> whiteMappingValues)
      throws IOException {
    return this.loginExtendBeanTool.getLoginExtendImpl().interceptorCheck(
        request, response, requestMappingValue, whiteMappingValues);
  }
}
