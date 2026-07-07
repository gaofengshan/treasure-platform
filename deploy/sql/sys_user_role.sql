-- 用户角色关联表（3NF 关联表）
CREATE TABLE sys_user_role (
    user_id   NUMBER(20) NOT NULL,
    role_id   NUMBER(20) NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

COMMENT ON TABLE sys_user_role IS '用户角色关联';
COMMENT ON COLUMN sys_user_role.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_role.role_id IS '角色ID';
