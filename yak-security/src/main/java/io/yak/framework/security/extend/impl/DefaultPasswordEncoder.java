package io.yak.framework.security.extend.impl;

import io.yak.framework.security.extend.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** One-way BCrypt encoder used for newly created and changed credentials. */
public class DefaultPasswordEncoder implements PasswordEncoder {
  private final BCryptPasswordEncoder delegate = new BCryptPasswordEncoder();

  public String encode(CharSequence rawPassword) {
    if (rawPassword == null) {
      throw new IllegalArgumentException("rawPassword cannot be null");
    }
    return delegate.encode(rawPassword);
  }
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return rawPassword != null && encodedPassword != null
        && delegate.matches(rawPassword, encodedPassword);
  }
}
