package io.yak.framework.security.service;

import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.account.AccountLoginDTO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.exception.YakSecurityException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 登录服务接口。
 */
public interface LoginService {
  /**
   * 校验登录信息并返回当前用户。
   */
  UserBriefVO verifyLogin(AccountLoginDTO var1, HttpServletRequest var2,
                                 HttpServletResponse var3)
      throws YakSecurityException;

  /**
   * 退出登录。
   */
  Result<Boolean> logout(HttpServletRequest var1,
                                HttpServletResponse var2);

  /**
   * 执行登录拦截校验。
   */
  boolean interceptorCheck(HttpServletRequest var1,
                                  HttpServletResponse var2, String var3,
                                  List<String> var4) throws IOException;
}
