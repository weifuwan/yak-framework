package io.yak.framework.security.service;

import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.account.AccountLoginDTO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.exception.YakSecurityException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface LoginService {
  public UserBriefVO verifyLogin(AccountLoginDTO var1, HttpServletRequest var2,
                                 HttpServletResponse var3)
      throws YakSecurityException;

  public Result<Boolean> logout(HttpServletRequest var1,
                                HttpServletResponse var2);

  public boolean interceptorCheck(HttpServletRequest var1,
                                  HttpServletResponse var2, String var3,
                                  List<String> var4) throws IOException;
}
