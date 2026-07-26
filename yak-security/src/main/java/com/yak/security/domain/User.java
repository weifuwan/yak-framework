package com.yak.security.domain;
import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;
@TableName("yak_security_user")
public class User {
  @TableId(type = IdType.AUTO) private Long id;
  private String username;
  private String password;
  private boolean enabled;
  private LocalDateTime createdAt;
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getUsername() { return username; }
  public void setUsername(String v) { username = v; }
  public String getPassword() { return password; }
  public void setPassword(String v) { password = v; }
  public boolean isEnabled() { return enabled; }
  public void setEnabled(boolean v) { enabled = v; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime v) { createdAt = v; }
}
