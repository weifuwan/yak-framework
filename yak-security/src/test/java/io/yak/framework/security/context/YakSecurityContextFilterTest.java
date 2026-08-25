package io.yak.framework.security.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.yak.framework.security.authentication.AuthenticationManager;
import io.yak.framework.security.config.YakSecurityProperties;
import io.yak.framework.security.dao.UserRoleDao;
import io.yak.framework.security.util.HttpRequestUtil;
import io.yak.framework.security.util.SecuritySessionAttributes;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class YakSecurityContextFilterTest {

  @Test
  void exposesAuthenticatedUserDuringRequestAndClearsContextAfterwards() throws Exception {
    UserRoleDao userRoleDao = mock(UserRoleDao.class);
    when(userRoleDao.selectRoleIdListByUserId(42L)).thenReturn(List.of(7L, 9L));
    ObjectProvider<UserRoleDao> provider = provider(userRoleDao);
    YakSecurityContextFilter filter = new YakSecurityContextFilter(provider);
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.getSession().setAttribute(SecuritySessionAttributes.USER_ID, 42L);
    request.getSession().setAttribute(SecuritySessionAttributes.USER_NAME, "yak");
    request.addHeader(HttpRequestUtil.PROJECT_ID, "1001");

    filter.doFilter(request, new MockHttpServletResponse(), (servletRequest, response) -> {
      assertEquals(42L, YakSecurityContext.getCurrentUserId());
      assertEquals("yak", YakSecurityContext.getCurrentUsername());
      assertEquals(1001L, YakSecurityContext.getCurrentProjectId());
      assertEquals(List.of(7L, 9L), YakSecurityContext.getCurrentRoleIds());
      assertTrue(YakSecurityContext.isAuthenticated());
      assertThrows(UnsupportedOperationException.class,
              () -> YakSecurityContext.getCurrentRoleIds().add(10L));
    });

    verify(userRoleDao).selectRoleIdListByUserId(42L);
    assertNull(YakSecurityContext.getCurrentUserId());
    assertFalse(YakSecurityContext.isAuthenticated());
  }

  @Test
  void exposesSaTokenIdentityWithoutReadingServletSession() throws Exception {
    UserRoleDao userRoleDao = mock(UserRoleDao.class);
    when(userRoleDao.selectRoleIdListByUserId(42L)).thenReturn(List.of(7L));
    AuthenticationManager authenticationManager =
            mock(AuthenticationManager.class);
    when(authenticationManager.isLogin()).thenReturn(true);
    when(authenticationManager.getLoginUserId()).thenReturn(42L);
    when(authenticationManager.getLoginUsername()).thenReturn("yak");

    YakSecurityProperties properties = new YakSecurityProperties();
    properties.getAuthentication().setMode(
            YakSecurityProperties.AuthenticationMode.SATOKEN);

    YakSecurityContextFilter filter =
            new YakSecurityContextFilter(
                    provider(userRoleDao),
                    authenticationProvider(authenticationManager),
                    properties);

    filter.doFilter(
            new MockHttpServletRequest(),
            new MockHttpServletResponse(),
            (request, response) -> {
              assertEquals(42L, YakSecurityContext.getCurrentUserId());
              assertEquals("yak", YakSecurityContext.getCurrentUsername());
              assertEquals(List.of(7L), YakSecurityContext.getCurrentRoleIds());
              assertTrue(YakSecurityContext.isAuthenticated());
            });

    verify(userRoleDao).selectRoleIdListByUserId(42L);
  }

  @Test
  void anonymousRequestHasSafeEmptyContext() throws Exception {
    YakSecurityContextFilter filter = new YakSecurityContextFilter(provider(null));

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
