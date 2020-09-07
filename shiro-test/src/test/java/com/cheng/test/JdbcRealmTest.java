package com.cheng.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @author cheng
 *         2018/11/2 20:23
 */
public class JdbcRealmTest {

    DruidDataSource dataSource = new DruidDataSource();

    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
    }

    @Test
    public void testAuthentication() {
        //定义一个存储安全数据的Inirealm 安全数据通过Ini配置文件存储
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        // 查询权限数据，默认为 false
        jdbcRealm.setPermissionsLookupEnabled(true);

        String sql = "select password from test_users where username = ?";
        jdbcRealm.setAuthenticationQuery(sql);

        String roleSql = "select role_name from test_user_roles  where username = ?";
        jdbcRealm.setUserRolesQuery(roleSql);

        String permissionSql = "select permission from test_roles_permissions  where role_name = ?";
        jdbcRealm.setPermissionsQuery(permissionSql);

        // 1. 构建 SecurityManager 环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        // 通过SecurityUtils 获取主体
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        //模拟登陆输入的用户名和密码
        UsernamePasswordToken token = new UsernamePasswordToken("cheng", "123");
        // 2. 主体提交认证请求
        subject.login(token);
        System.out.println("isAuthenticated: " + subject.isAuthenticated());
        //3. 主体提交授权请求
        subject.checkRole("admin");
        //subject.checkRoles("admin", "user");

        subject.checkPermission("user:add");
        //subject.checkPermissions("user:add","user:update");
    }
}
