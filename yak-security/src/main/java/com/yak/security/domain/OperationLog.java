package com.yak.security.domain;
import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;
@TableName("yak_security_operation_log")
public class OperationLog {
  @TableId(type = IdType.AUTO) private Long id;
  private Long userId;
  private String username;
  private String operation;
  private String method;
  private boolean success;
  private String detail;
  private LocalDateTime createdAt;
  public Long getId() { return id; }
  public void setId(Long v) { id = v; }
  public Long getUserId() { return userId; }
  public void setUserId(Long v) { userId = v; }
  public String getUsername() { return username; }
  public void setUsername(String v) { username = v; }
  public String getOperation() { return operation; }
  public void setOperation(String v) { operation = v; }
  public String getMethod() { return method; }
  public void setMethod(String v) { method = v; }
  public boolean isSuccess() { return success; }
  public void setSuccess(boolean v) { success = v; }
  public String getDetail() { return detail; }
  public void setDetail(String v) { detail = v; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime v) { createdAt = v; }
}
