package com.yak.security.service;
public interface PasswordEncoder {
  String encode(CharSequence raw);
  boolean matches(CharSequence raw, String encoded);
}
