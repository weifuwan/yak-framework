package io.yak.framework.security.extend.impl;

import io.yak.framework.security.extend.PasswordEncoder;
import java.util.Objects;

/** Compatibility encoder retaining the module's existing plain credential semantics. */
public class DefaultPasswordEncoder implements PasswordEncoder {
  public String encode(CharSequence rawPassword) { return rawPassword == null ? null : rawPassword.toString(); }
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return Objects.equals(encode(rawPassword), encodedPassword);
  }
}
