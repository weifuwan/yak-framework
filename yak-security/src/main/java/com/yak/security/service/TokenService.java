package com.yak.security.service;
public interface TokenService {
  String issue(AuthenticatedUser user);
  AuthenticatedUser resolve(String token);
  void revoke(String token);
}
