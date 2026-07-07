-- 角色表（3NF）
CREATE TABLE sys_role (
    id          NUMBER(20)    PRIMARY KEY,
    role_name   VARCHAR2(100) NOT NULL,
    role_key    VARCHAR2(100) NOT NULL,
    status      NUMBER(1)     DEFAULT 1,
    create_by   VARCHAR2(50),
    create_time DATE          DEFAULT SYSDATE,
    update_by   VARCHAR2(50),
    update_time DATE,
    del_flag    NUMBER(1)     DEFAULT 0
);

COMMENT ON TABLE sys_role IS '系统角色';
COMMENT ON COLUMN sys_role.id IS '主键';
COMMENT ON COLUMN sys_role.role_name IS '角色名称';
COMMENT ON COLUMN sys_role.role_key IS '角色标识';
COMMENT ON COLUMN sys_role.status IS '状态（1正常 0停用）';

CREATE SEQUENCE seq_sys_role START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
