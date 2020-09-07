package com.cheng.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @author cheng
 *         2018/11/2 20:18
 */
public class IniRealmTest {

    //定义一个存储安全数据的Inirealm 安全数据通过Ini配置文件存储
    IniRealm iniRealm = new IniRealm("classpath:user.ini");

    @Test
    public void testAuthentication() {

        // 1. 构建 SecurityManager 环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);
        // 通过SecurityUtils 获取主体
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        //模拟登陆输入的用户名和密码
        UsernamePasswordToken token = new UsernamePasswordToken("cheng", "123456");
        // 2. 主体提交认证请求
        subject.login(token);
        System.out.println("isAuthenticated: " + subject.isAuthenticated());
        //3. 主体提交授权认证
        subject.checkRole("admin");
        subject.checkPermission("user:select");
        subject.checkPermission("user:update");
    }
}
