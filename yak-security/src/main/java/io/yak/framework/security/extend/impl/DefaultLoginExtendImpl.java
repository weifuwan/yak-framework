package io.yak.framework.security.extend.impl;

import io.yak.framework.security.common.Result;
import io.yak.framework.security.common.dto.account.AccountLoginDTO;
import io.yak.framework.security.common.entity.user.User;
import io.yak.framework.security.common.enums.ResultCode;
import io.yak.framework.security.common.vo.user.UserBriefVO;
import io.yak.framework.security.exception.YakSecurityException;
import io.yak.framework.security.extend.LoginExtend;
import io.yak.framework.security.extend.PasswordEncoder;
import io.yak.framework.security.service.UserService;
import io.yak.framework.security.util.CopyBeanUtil;
import io.yak.framework.security.util.HttpRequestUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.util.StringUtils;

@ConditionalOnMissingBean(LoginExtend.class)
public class DefaultLoginExtendImpl implements LoginExtend {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(DefaultLoginExtendImpl.class);
  @Autowired private UserService userService;
  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  public UserBriefVO verifyLogin(AccountLoginDTO loginDTO,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
      throws YakSecurityException {
    User user = this.userService.getUserByUserName(loginDTO.getUserName());
    if (user == null) {
      throw new YakSecurityException(ResultCode.USER_NOT_EXISTS);
    }
    if (Integer.valueOf(2).equals(user.getStatus())) {
      throw new YakSecurityException(ResultCode.USER_ACCOUNT_DISABLE);
    }
    if (!this.passwordEncoder.matches(loginDTO.getPw(), user.getPw())) {
      throw new YakSecurityException(ResultCode.USER_CREDENTIALS_ERROR);
    }
    this.initLoginContext(request, response, loginDTO.getUserName(),
                          user.getId());
    return CopyBeanUtil.copy(user, UserBriefVO.class);
  }

  @Override
  public Result<Boolean> logout(HttpServletRequest request,
                                HttpServletResponse response) {
    request.getSession().invalidate();
    response.setStatus(HttpRequestUtil.REDIRECT_CODE.intValue());
    return Result.buildSucc(Boolean.TRUE);
  }

  @Override
  public boolean
  interceptorCheck(HttpServletRequest request, HttpServletResponse response,
                   String requestMappingValue, List<String> whiteMappingValues)
      throws IOException {
    if (StringUtils.isEmpty((Object)requestMappingValue)) {
      LOGGER.error("class=LoginServiceImpl||method=interceptorCheck||msg=uri " +
                   "illegal||uri={}",
                   (Object)request.getRequestURI());
      return Boolean.FALSE;
    }
    for (String mapping : whiteMappingValues) {
      if (!requestMappingValue.contains(mapping))
        continue;
      return Boolean.TRUE;
    }
    if (!this.hasLoginValid(request)) {
      this.logout(request, response);
      return Boolean.FALSE;
    }
    String operator = HttpRequestUtil.getOperator(request);
    User user = this.userService.getUserByUserName(operator);
    if (user == null) {
      throw new YakSecurityException(ResultCode.USER_NOT_EXISTS);
    }
    this.initLoginContext(request, response, operator, user.getId());
    return Boolean.TRUE;
  }

  private void initLoginContext(HttpServletRequest request,
                                HttpServletResponse response, String userName,
                                Long userId) {
    HttpSession session = request.getSession(true);
    session.setMaxInactiveInterval(
        HttpRequestUtil.COOKIE_OR_SESSION_MAX_AGE_UNIT_SEC.intValue());
    session.setAttribute("X-SSO-USER", (Object)userName);
    session.setAttribute("X-SSO-USER-ID", (Object)userId);
    Cookie cookieUserName = new Cookie("X-SSO-USER", userName);
    cookieUserName.setMaxAge(
        HttpRequestUtil.COOKIE_OR_SESSION_MAX_AGE_UNIT_SEC.intValue());
    cookieUserName.setPath("/");
    Cookie cookieUserId = new Cookie("X-SSO-USER-ID", userId.toString());
    cookieUserId.setMaxAge(
        HttpRequestUtil.COOKIE_OR_SESSION_MAX_AGE_UNIT_SEC.intValue());
    cookieUserId.setPath("/");
    response.addCookie(cookieUserName);
    response.addCookie(cookieUserId);
  }

  private boolean hasLoginValid(HttpServletRequest request) {
    String username = HttpRequestUtil.getOperator(request);
    if (StringUtils.isEmpty((Object)username)) {
      return false;
    }
    User user = this.userService.getUserByUserName(username);
    return null != user;
  }
}
