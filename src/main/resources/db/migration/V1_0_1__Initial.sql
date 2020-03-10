create database `haldus` default charset utf8mb4 collate utf8mb4_unicode_ci;
create user `haldus` identified by 'haldus';
grant all on haldus.* to haldus;

use haldus;

create table haldus.t_user (
    id             bigint                                 not null auto_increment,
    code           varchar(64)  default ''                not null comment '用户业务编码',
    name           varchar(32)  default ''                not null comment '用户显示名称',
    type           int          default '0'               not null comment '用户类型',
    username       varchar(32)  default ''                not null comment '用户名 由用户自定义的',
    password       varchar(128) default ''                not null comment '加密的密码文',
    enable         tinyint      default 1                 not null comment '是否有效/激活',
    email          varchar(64)  default ''                not null comment '邮箱账号',
    email_verified tinyint      default '0'               not null comment '邮箱是否已验证过',
    phone          varchar(32)  default ''                not null comment '联系电话/手机号',
    phone_verified tinyint      default 0                 not null comment '联系电话是否已经验证过',
    avatar         varchar(255) default ''                not null comment '用户头像',
    deleted        tinyint      default 0                 not null comment '软删除标记',
    created_at     datetime     default current_timestamp not null comment '最新建的时间',
    updated_at     datetime     default current_timestamp not null comment '最新修改的时间',
    primary key (id),
    unique key uk_ucs_user_username (username),
    unique key uk_ucs_user_code (code),
    unique key uk_ucs_user_email (email),
    unique key uk_ucs_user_phone (phone)
) Engine = INNODB, comment '用户|C';

create table pied.t_source (
    id                bigint                                not null auto_increment,
    user_code         varchar(64) default ''                not null comment '所记录的用户code',
    service           varchar(32) default ''                not null comment '来源什么服务',
    service_user_code varchar(64) default ''                not null comment '所记录的用户code',
    service_key       varchar(32) default ''                not null comment '服务的具体appid',
    service_key_code  varchar(64) default ''                not null comment '所记录的用户code',
    created_at        datetime    default current_timestamp not null comment '最新建的时间',
    primary key (id),
    key ix_ucs_source_user_code (user_code),
    unique uq_ucs_source_service_user_code (service, service_user_code),
    unique uq_ucs_source_service_key_key_code (service_key, service_key_code),
    key ix_ucs_source_service_key_code (service_key_code),
    key ix_ucs_source_service_user_code (service_user_code)
) Engine = INNODB, comment '记录用户来源';

create table pied.t_contact (
    id             bigint                                 not null auto_increment,
    code           varchar(64)  default ''                not null comment '联系方式编码 业务主键',
    name           varchar(32)  default ''                not null comment '用户为自己联系方式起的记忆名称 如“家” “公司”',
    contact        varchar(32)  default ''                not null comment '联系人名称',
    phone          varchar(32)  default ''                not null comment '联系电话/手机号',
    phone_verified tinyint      default 0                 not null comment '手机号码通过验证',
    district       varchar(32)  default ''                not null comment '地址区域,街道一级',
    address        varchar(128) default ''                not null comment '联系地址详情',
    comment        varchar(128) default ''                not null comment '地址',
    user_code      varchar(64)  default ''                not null comment '表示是哪个用户的联系方式',
    is_default     tinyint      default 0                 not null comment '表示该地址为用户默认地址',
    deleted        tinyint      default 0                 not null comment '软删除标记',
    created_at     datetime     default current_timestamp not null comment '最新建的时间',
    updated_at     datetime     default current_timestamp not null comment '最新修改的时间',
    primary key (id),
    key ix_ucs_contact_phone (phone),
    key ix_ucs_contact_user_code (user_code)
) Engine = INNODB, comment '用户的联系方式';

