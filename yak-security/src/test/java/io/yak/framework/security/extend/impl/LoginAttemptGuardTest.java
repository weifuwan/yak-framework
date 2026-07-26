package io.yak.framework.security.extend.impl;

import io.yak.framework.security.config.YakSecurityProperties;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginAttemptGuardTest {

  @Test
  void blocksBothUsernameAndIpDimensionsAtThreshold() {
    YakSecurityProperties.LoginSecurityProperties properties = properties();
    LoginAttemptGuard guard = new LoginAttemptGuard(properties,
            Clock.fixed(Instant.parse("2026-07-26T00:00:00Z"), ZoneOffset.UTC));

    guard.recordFailure(" Alice ", "192.0.2.1");
    assertFalse(guard.isBlocked("alice", "192.0.2.2"));
    guard.recordFailure("ALICE", "192.0.2.2");

    assertTrue(guard.isBlocked("alice", "192.0.2.3"));
    assertTrue(guard.isBlocked("bob", "192.0.2.2"));
  }

  @Test
  void successfulLoginClearsBothDimensions() {
    LoginAttemptGuard guard = new LoginAttemptGuard(properties());
    guard.recordFailure("alice", "192.0.2.1");
    guard.recordFailure("alice", "192.0.2.1");
    guard.recordSuccess("alice", "192.0.2.1");

    assertFalse(guard.isBlocked("alice", "192.0.2.1"));
  }

  private YakSecurityProperties.LoginSecurityProperties properties() {
    YakSecurityProperties.LoginSecurityProperties properties =
            new YakSecurityProperties.LoginSecurityProperties();
    properties.setMaxFailureCount(2);
    properties.setLockDuration(Duration.ofMinutes(15));
    return properties;
  }
}
