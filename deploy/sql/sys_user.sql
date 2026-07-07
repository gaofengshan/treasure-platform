-- 系统用户表（3NF）
-- 兼容 Oracle 12c + 达梦 DM8
CREATE TABLE sys_user (
    id          NUMBER(20)    PRIMARY KEY,
    username    VARCHAR2(50)  NOT NULL,
    password    VARCHAR2(255) NOT NULL,
    real_name   VARCHAR2(100),
    email       VARCHAR2(100),
    phone       VARCHAR2(20),
    avatar      VARCHAR2(255),
    status      NUMBER(1)     DEFAULT 1,
    create_by   VARCHAR2(50),
    create_time DATE          DEFAULT SYSDATE,
    update_by   VARCHAR2(50),
    update_time DATE,
    del_flag    NUMBER(1)     DEFAULT 0
);

COMMENT ON TABLE sys_user IS '系统用户';
COMMENT ON COLUMN sys_user.id IS '主键';
COMMENT ON COLUMN sys_user.username IS '用户名';
COMMENT ON COLUMN sys_user.password IS '密码（BCrypt 加密）';
COMMENT ON COLUMN sys_user.real_name IS '真实姓名';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.phone IS '手机号';
COMMENT ON COLUMN sys_user.avatar IS '头像';
COMMENT ON COLUMN sys_user.status IS '状态（1正常 0停用）';
COMMENT ON COLUMN sys_user.del_flag IS '删除标记（0未删 1已删）';

CREATE SEQUENCE seq_sys_user START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
