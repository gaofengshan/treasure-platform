-- 初始化数据
-- 管理员密码：admin123（BCrypt 加密）
INSERT INTO sys_user (id, username, password, real_name, status)
VALUES (seq_sys_user.NEXTVAL, 'admin', '$2a$10$ItL0kJY9ElA1IitB696w4e9tpOqZyJt9fFd4st.WY9fgUK09hCOoa',
        '系统管理员', 1);

INSERT INTO sys_role (id, role_name, role_key, status)
VALUES (seq_sys_role.NEXTVAL, '超级管理员', 'admin', 1);

INSERT INTO sys_user_role (user_id, role_id)
VALUES (1, 1);
COMMIT;
