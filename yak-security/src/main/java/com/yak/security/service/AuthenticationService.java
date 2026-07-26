package com.yak.security.service;
import com.yak.security.domain.User;
import com.yak.security.dto.*;
import com.yak.security.mapper.UserMapper;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;
public class AuthenticationService {
  private final UserMapper users;
  private final PasswordEncoder encoder;
  private final TokenService tokens;
  public AuthenticationService(UserMapper u, PasswordEncoder e,
                               TokenService t) {
    users = u;
    encoder = e;
    tokens = t;
  }
  public SecurityResult<LoginResponse> login(LoginRequest r) {
    if (invalid(r))
      return SecurityResult.failure(400, "username and password are required");
    String username = r.getUsername().trim();
    User u = users.findByUsername(username);
    if (u == null || !u.isEnabled() ||
        !encoder.matches(r.getPassword(), u.getPassword()))
      return SecurityResult.failure(401, "invalid username or password");
    Set<String> roles = new HashSet<String>(users.findRoleCodes(u.getId()));
    Set<String> permissions =
        new HashSet<String>(users.findPermissionCodes(u.getId()));
    AuthenticatedUser principal = new AuthenticatedUser(
        u.getId(), u.getUsername(), Collections.unmodifiableSet(roles),
        Collections.unmodifiableSet(permissions));
    String token = tokens.issue(principal);
    return SecurityResult.success(new LoginResponse(
        u.getId(), u.getUsername(), token, principal.getPermissions()));
  }
  @Transactional
  public SecurityResult<LoginResponse> register(RegisterRequest r) {
    if (invalid(r))
      return SecurityResult.failure(400, "username and password are required");
    if (users.findByUsername(r.getUsername().trim()) != null)
      return SecurityResult.failure(409, "username already exists");
    User u = new User();
    u.setUsername(r.getUsername().trim());
    u.setPassword(encoder.encode(r.getPassword()));
    u.setEnabled(true);
    u.setCreatedAt(LocalDateTime.now());
    users.insert(u);
    return login(r);
  }
  private boolean invalid(LoginRequest r) {
    return r == null || r.getUsername() == null ||
        r.getUsername().trim().isEmpty() || r.getPassword() == null ||
        r.getPassword().isEmpty();
  }
}
