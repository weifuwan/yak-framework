ALTER TABLE yak_security_permission
  ADD COLUMN active TINYINT(1) NOT NULL DEFAULT 1 COMMENT '权限是否有效：0 否，1 是' AFTER description,
  ADD COLUMN declared TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否由声明式注册管理' AFTER active;

CREATE INDEX idx_permission_app_declared
  ON yak_security_permission(app_name, declared, active);
