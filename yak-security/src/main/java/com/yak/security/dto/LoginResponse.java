package com.yak.security.dto;
import java.util.Set;
public class LoginResponse {
  private final Long userId;
  private final String username;
  private final String token;
  private final Set<String> permissions;
  public LoginResponse(Long i, String u, String t, Set<String> p) {
    userId = i;
    username = u;
    token = t;
    permissions = p;
  }
  public Long getUserId() { return userId; }
  public String getUsername() { return username; }
  public String getToken() { return token; }
  public Set<String> getPermissions() { return permissions; }
}
