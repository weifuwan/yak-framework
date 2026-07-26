package com.yak.security.web;
import com.yak.security.annotation.AuditedOperation;
import com.yak.security.dto.*;
import com.yak.security.service.AuthenticationService;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/yak/security")
public class AuthenticationController {
  private final AuthenticationService service;
  public AuthenticationController(AuthenticationService s) { service = s; }
  @PostMapping("/login")
  @AuditedOperation("login")
  public SecurityResult<LoginResponse> login(@RequestBody LoginRequest r) {
    return service.login(r);
  }
  @PostMapping("/register")
  @AuditedOperation("register")
  public SecurityResult<LoginResponse> register(
      @RequestBody RegisterRequest r) {
    return service.register(r);
  }
}
