package com.yak.security.service;
import java.util.Set;
public class AuthenticatedUser {
  private final Long id;
  private final String username;
  private final Set<String> roles;
  private final Set<String> permissions;
  public AuthenticatedUser(Long i, String u, Set<String> r, Set<String> p) {
    id = i;
    username = u;
    roles = r;
    permissions = p;
  }
  public Long getId() { return id; }
  public String getUsername() { return username; }
  public Set<String> getRoles() { return roles; }
  public Set<String> getPermissions() { return permissions; }
}
