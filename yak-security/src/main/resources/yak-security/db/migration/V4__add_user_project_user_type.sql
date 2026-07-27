-- 区分普通项目成员（0）与项目负责人（1）。
-- 默认值 0 可安全迁移历史关系数据，并与 UserProjectServiceImpl 的普通成员类型保持一致。
ALTER TABLE yak_security_user_project
    ADD COLUMN user_type TINYINT NOT NULL DEFAULT 0 COMMENT '项目用户类型：0 普通成员，1 项目负责人'
        AFTER user_id;
