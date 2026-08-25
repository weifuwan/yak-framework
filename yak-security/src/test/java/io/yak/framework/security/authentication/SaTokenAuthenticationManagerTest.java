package io.yak.framework.security.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cn.dev33.satoken.stp.StpLogic;
import org.junit.jupiter.api.Test;

class SaTokenAuthenticationManagerTest {

  @Test
  void shouldDelegateLoginStateOperationsToSaToken() {
    StpLogic stpLogic = mock(StpLogic.class);
    SaTokenAuthenticationManager manager =
            new SaTokenAuthenticationManager(stpLogic);

    when(stpLogic.isLogin()).thenReturn(true);
    when(stpLogic.getLoginIdDefaultNull()).thenReturn(42L);

    manager.login(42L);

    assertThat(manager.isLogin()).isTrue();
    assertThat(manager.getLoginUserId()).isEqualTo(42L);

    manager.logout();

    verify(stpLogic).login(42L);
    verify(stpLogic).logout();
  }

  @Test
  void shouldConvertStringLoginIdToLong() {
    StpLogic stpLogic = mock(StpLogic.class);
    SaTokenAuthenticationManager manager =
            new SaTokenAuthenticationManager(stpLogic);

    when(stpLogic.getLoginIdDefaultNull()).thenReturn(" 1001 ");

    assertThat(manager.getLoginUserId()).isEqualTo(1001L);
  }

  @Test
  void shouldReturnNullWhenCurrentRequestIsAnonymous() {
    StpLogic stpLogic = mock(StpLogic.class);
    SaTokenAuthenticationManager manager =
            new SaTokenAuthenticationManager(stpLogic);

    when(stpLogic.getLoginIdDefaultNull()).thenReturn(null);

    assertThat(manager.getLoginUserId()).isNull();
  }

  @Test
  void shouldRejectUnsupportedLoginId() {
    StpLogic stpLogic = mock(StpLogic.class);
    SaTokenAuthenticationManager manager =
            new SaTokenAuthenticationManager(stpLogic);

    when(stpLogic.getLoginIdDefaultNull()).thenReturn("not-a-number");

    assertThatThrownBy(manager::getLoginUserId)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("convertible to Long");
  }
}
