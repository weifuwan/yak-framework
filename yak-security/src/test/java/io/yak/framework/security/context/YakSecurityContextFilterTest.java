package io.yak.framework.security.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.yak.framework.security.authentication.AuthenticationManager;
import io.yak.framework.security.dao.UserRoleDao;
import io.yak.framework.security.util.HttpRequestUtil;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class YakSecurityContextFilterTest {

  @Test
  void exposesAuthenticatedIdentityAndClearsContextAfterwards() throws Exception {
    UserRoleDao userRoleDao = mock(UserRoleDao.class);
    when(userRoleDao.selectRoleIdListByUserId(42L)).thenReturn(List.of(7L, 9L));
    AuthenticationManager manager = mock(AuthenticationManager.class);
    when(manager.isLogin()).thenReturn(true);
    when(manager.getLoginUserId()).thenReturn(42L);
    when(manager.getLoginUsername()).thenReturn("yak");

    YakSecurityContextFilter filter = new YakSecurityContextFilter(
            provider(userRoleDao), authenticationProvider(manager));
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(HttpRequestUtil.PROJECT_ID, "1001");

    filter.doFilter(request, new MockHttpServletResponse(), (servletRequest, response) -> {
      assertEquals(42L, YakSecurityContext.getCurrentUserId());
      assertEquals("yak", YakSecurityContext.getCurrentUsername());
      assertEquals(1001L, YakSecurityContext.getCurrentProjectId());
      assertEquals(List.of(7L, 9L), YakSecurityContext.getCurrentRoleIds());
      assertTrue(YakSecurityContext.isAuthenticated());
      assertEquals("yak", HttpRequestUtil.getOperator(request));
      assertEquals(42L, HttpRequestUtil.getOperatorId(request));
    });

    verify(userRoleDao).selectRoleIdListByUserId(42L);
    assertNull(YakSecurityContext.getCurrentUserId());
    assertFalse(YakSecurityContext.isAuthenticated());
  }

  @Test
  void anonymousRequestHasSafeEmptyContext() throws Exception {
    AuthenticationManager manager = mock(AuthenticationManager.class);
    when(manager.isLogin()).thenReturn(false);
    YakSecurityContextFilter filter = new YakSecurityContextFilter(
            provider(null), authenticationProvider(manager));

    filter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(),
            (request, response) -> {
              assertFalse(YakSecurityContext.isAuthenticated());
              assertNull(YakSecurityContext.getCurrentUsername());
              assertTrue(YakSecurityContext.getCurrentRoleIds().isEmpty());
            });
  }

  @SuppressWarnings("unchecked")
  private ObjectProvider<UserRoleDao> provider(UserRoleDao userRoleDao) {
    ObjectProvider<UserRoleDao> provider = mock(ObjectProvider.class);
    when(provider.getIfAvailable()).thenReturn(userRoleDao);
    return provider;
  }

  @SuppressWarnings("unchecked")
  private ObjectProvider<AuthenticationManager> authenticationProvider(
          AuthenticationManager authenticationManager) {
    ObjectProvider<AuthenticationManager> provider = mock(ObjectProvider.class);
    when(provider.getIfAvailable()).thenReturn(authenticationManager);
    return provider;
  }
}
