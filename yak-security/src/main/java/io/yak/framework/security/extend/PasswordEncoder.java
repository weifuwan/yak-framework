package io.yak.framework.security.extend;

/** Encodes and verifies credentials used by the security module. */
public interface PasswordEncoder {
  String encode(CharSequence rawPassword);
  boolean matches(CharSequence rawPassword, String encodedPassword);
}
