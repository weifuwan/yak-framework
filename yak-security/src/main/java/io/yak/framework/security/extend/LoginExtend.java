package io.yak.framework.security.extend;

import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.account.AccountLoginDTO;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.exception.YakSecurityException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 登录认证扩展点。自定义实现负责校验凭据、维护会话并执行请求拦截，调用方必须保持请求和响应上下文一致。
 */
public interface LoginExtend {
  public UserBriefVO verifyLogin(AccountLoginDTO var1, HttpServletRequest var2,
                                 HttpServletResponse var3)
      throws YakSecurityException;

  public Result<Boolean> logout(HttpServletRequest var1,
                                HttpServletResponse var2);

  public boolean interceptorCheck(HttpServletRequest var1,
                                  HttpServletResponse var2, String var3,
                                  List<String> var4) throws IOException;
}
