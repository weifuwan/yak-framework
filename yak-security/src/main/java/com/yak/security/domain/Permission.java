package com.yak.security.domain;
import com.baomidou.mybatisplus.annotation.*;
@TableName("yak_security_permission")
public class Permission {
  @TableId(type = IdType.AUTO) private Long id;
  private String code;
  private String name;
  public Long getId() { return id; }
  public void setId(Long v) { id = v; }
  public String getCode() { return code; }
  public void setCode(String v) { code = v; }
  public String getName() { return name; }
  public void setName(String v) { name = v; }
}
