-- Haldus Auth 模块 DDL
-- account_auth: 用户认证主表
-- account_oauth: OAuth 第三方登录关联表

CREATE TABLE IF NOT EXISTS account_auth (
    id              BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    code            VARCHAR(64)     NOT NULL UNIQUE                COMMENT 'snowflake 生成的唯一业务码',
    name            VARCHAR(128)    DEFAULT NULL                    COMMENT '名称（AbstractUnified 基类字段）',
    type            INT             DEFAULT NULL                    COMMENT '类型（AbstractUnified 基类字段）',
    email           VARCHAR(255)    DEFAULT NULL                    COMMENT '邮箱',
    phone           VARCHAR(32)     DEFAULT NULL                    COMMENT '手机号',
    password_hash   VARCHAR(255)    DEFAULT NULL                    COMMENT '密码哈希（bcrypt）',
    display_name    VARCHAR(128)    DEFAULT NULL                    COMMENT '显示名称',
    avatar_url      VARCHAR(512)    DEFAULT NULL                    COMMENT '头像URL',
    email_verified  TINYINT         NOT NULL DEFAULT 0              COMMENT '邮箱是否已验证 0-否 1-是',
    phone_verified  TINYINT         NOT NULL DEFAULT 0              COMMENT '手机是否已验证 0-否 1-是',
    status          INT             NOT NULL DEFAULT 1              COMMENT '状态 1-正常 0-禁用',
    last_login_at   DATETIME        DEFAULT NULL                    COMMENT '最后登录时间',
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT         NOT NULL DEFAULT 0              COMMENT '逻辑删除 0-未删除 1-已删除',

    INDEX idx_email (email),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户认证主表';


CREATE TABLE IF NOT EXISTS account_oauth (
    id               BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    code             VARCHAR(64)     NOT NULL UNIQUE                COMMENT 'snowflake 生成的唯一业务码',
    name             VARCHAR(128)    DEFAULT NULL                    COMMENT '名称（AbstractUnified 基类字段）',
    type             INT             DEFAULT NULL                    COMMENT '类型（AbstractUnified 基类字段）',
    user_id          BIGINT          NOT NULL                        COMMENT '关联 account_auth.id',
    provider         VARCHAR(32)     NOT NULL                        COMMENT 'OAuth 平台标识（如 github/google/wechat）',
    provider_user_id VARCHAR(255)    NOT NULL                        COMMENT '第三方平台的用户唯一标识',
    access_token     VARCHAR(512)    DEFAULT NULL                    COMMENT '访问令牌',
    refresh_token    VARCHAR(512)    DEFAULT NULL                    COMMENT '刷新令牌',
    token_expires_at DATETIME        DEFAULT NULL                    COMMENT '令牌过期时间',
    provider_data    TEXT            DEFAULT NULL                    COMMENT '第三方平台返回的原始数据（JSON）',
    status           INT             NOT NULL DEFAULT 1              COMMENT '状态 1-正常 0-禁用',
    created_at       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted          TINYINT         NOT NULL DEFAULT 0              COMMENT '逻辑删除 0-未删除 1-已删除',

    UNIQUE INDEX uk_provider_user (provider, provider_user_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth 第三方登录关联表';
