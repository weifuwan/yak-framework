package io.yak.framework.security.context;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.jupiter.api.Test;

class CurrentUserTenantCompatibilityTest {

  @Test
  void existingCurrentUserImplementationsRemainCompatible() {
    CurrentUser currentUser = new CurrentUser() {
      @Override
      public Long getUserId() {
        return 1L;
      }

      @Override
      public String getUsername() {
        return "tester";
      }

      @Override
      public Long getProjectId() {
        return null;
      }

      @Override
      public java.util.List<Long> getRoleIds() {
        return Collections.emptyList();
      }

      @Override
      public boolean isAuthenticated() {
        return true;
      }
    };

    assertThat(currentUser.getTenantId()).isNull();
    assertThat(currentUser.getTenantCode()).isNull();
    assertThat(currentUser.getTenantName()).isNull();
  }
}
