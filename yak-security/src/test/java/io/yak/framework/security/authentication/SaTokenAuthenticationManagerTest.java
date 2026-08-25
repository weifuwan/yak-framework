package io.yak.framework.security.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class SaTokenAuthenticationManagerTest {

  @Test
  void shouldDelegateLoginStateOperationsToSaToken() {
    StpLogic stpLogic = mock(StpLogic.class);
    SaTokenAuthenticationManager manager = new SaTokenAuthenticationManager(stpLogic);
    when(stpLogic.isLogin()).thenReturn(true);
    when(stpLogic.getLoginIdDefaultNull()).thenReturn(42L);

    manager.login(42L);
    assertThat(manager.isLogin()).isTrue();
    assertThat(manager.getLoginUserId()).isEqualTo(42L);
    manager.logout();
    manager.logoutUser(42L);

    verify(stpLogic).login(42L);
    verify(stpLogic).logout();
    verify(stpLogic).logout(42L);
  }

  @Test
  void shouldKeepUsernameMetadataAndHonorGlobalGovernance() {
    StpLogic stpLogic = mock(StpLogic.class);
    SaTokenConfig config = new SaTokenConfig()
            .setIsConcurrent(false)
            .setIsShare(true)
            .setMaxLoginCount(2);
    SaSession tokenSession = mock(SaSession.class);
    when(stpLogic.getConfigOrGlobal()).thenReturn(config);
    when(stpLogic.createSaLoginParameter())
            .thenAnswer(ignored -> new SaLoginParameter(config));
    when(stpLogic.getTokenSession(true)).thenReturn(tokenSession);

    SaTokenAuthenticationManager manager =
            new SaTokenAuthenticationManager(stpLogic, Duration.ofMinutes(30));
    manager.login(42L, "yak");

    ArgumentCaptor<SaLoginParameter> captor = ArgumentCaptor.forClass(SaLoginParameter.class);
    verify(stpLogic).login(eq(42L), captor.capture());
    assertThat(captor.getValue().getActiveTimeout()).isEqualTo(1800L);
    assertThat(captor.getValue().getIsConcurrent()).isFalse();
    assertThat(captor.getValue().getIsShare()).isTrue();
    assertThat(captor.getValue().getMaxLoginCount()).isEqualTo(2);
    verify(tokenSession).set("yak-security:username", "yak");

    when(stpLogic.isLogin()).thenReturn(true);
    when(stpLogic.getTokenSession(false)).thenReturn(tokenSession);
    when(tokenSession.get("yak-security:username")).thenReturn("yak");
    assertThat(manager.getLoginUsername()).isEqualTo("yak");
  }

  @Test
  void shouldConvertStringLoginIdToLong() {
    StpLogic stpLogic = mock(StpLogic.class);
    SaTokenAuthenticationManager manager = new SaTokenAuthenticationManager(stpLogic);
    when(stpLogic.getLoginIdDefaultNull()).thenReturn(" 1001 ");
    assertThat(manager.getLoginUserId()).isEqualTo(1001L);
  }

  @Test
  void shouldReturnNullWhenCurrentRequestIsAnonymous() {
    StpLogic stpLogic = mock(StpLogic.class);
    SaTokenAuthenticationManager manager = new SaTokenAuthenticationManager(stpLogic);
    when(stpLogic.getLoginIdDefaultNull()).thenReturn(null);
    assertThat(manager.getLoginUserId()).isNull();
    assertThat(manager.getLoginUsername()).isNull();
  }

  @Test
  void shouldRejectUnsupportedLoginId() {
    StpLogic stpLogic = mock(StpLogic.class);
    SaTokenAuthenticationManager manager = new SaTokenAuthenticationManager(stpLogic);
    when(stpLogic.getLoginIdDefaultNull()).thenReturn("not-a-number");
    assertThatThrownBy(manager::getLoginUserId)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("convertible to Long");
  }
}
