package com.cheng.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author cheng
 *         2019/11/2 20:04
 */
public class AuthenticationTest {

    //定义一个存储安全数据的realm 并且放入安全数据（模拟安全数据库）
    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();
    @Before
    public void addUser() {
        simpleAccountRealm.addAccount("cheng", "123", "admin", "user");
    }

    @Test
    public void testAuthentication() {

        // 1. 构建 SecurityManager 环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        // 2. 主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        //模拟登陆的用户口令和密码
        UsernamePasswordToken token = new UsernamePasswordToken("cheng", "123");
        subject.login(token);

        System.out.println("isAuthenticated: " + subject.isAuthenticated());

        // 3. 主体提交授权请求、检验用户是否有 admin 角色
        subject.checkRole("admin");
        //subject.checkRoles(Arrays.asList("admin", "user2"));
        //subject.checkRoles("admin", "user");

        //subject.checkPermission("user:delete");
        //subject.checkPermissions("user:delete","user:add");
        // 4. 退出认证
        subject.logout();

        System.out.println("isAuthenticated: " + subject.isAuthenticated());
    }
}
