package com.cheng.controller;

import com.cheng.service.SessionService;
import com.cheng.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author cheng
 *         2019/11/2 21:31
 */
@Controller
public class UserController {

    @Autowired
    public SessionService sessionService;

    @RequestMapping(value = "/subLogin", method = RequestMethod.POST,
            produces = "application/json;charset=utf-8")
    @ResponseBody
    public String subLogin(User user) {
        //1、获取主体，通过主体获取用户名密码
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());

        try {
            //2、设置 shiro 记住我功能
            token.setRememberMe(user.getRememberMe());
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        if (subject.hasRole("admin")) {
            return "有 admin 权限";
        }
        return "无 admin 权限";
    }

    /*测试单个角色*/
    @RequiresRoles("admin")
    @RequestMapping(value = "/testRole", method = RequestMethod.GET)
    @ResponseBody
    public String testRole() {
        return "testRole success";
    }

    /*测试多个角色信息*/
    @RequiresRoles({"admin1","user"})
    @RequestMapping(value = "/testRole1", method = RequestMethod.GET)
    @ResponseBody
    public String testRole1() {
        return "testRole1 success";
    }

    /*测试注解权限*/
    @RequiresPermissions({"user:delete","user:update"})
    @RequestMapping(value = "/testPerms", method = RequestMethod.GET)
    @ResponseBody
    public String testPerms() {
        return "testPerms success";
    }

    /*测试会话缓存*/
    @RequestMapping(value = "/testSession", method = RequestMethod.GET)
    @ResponseBody
    public String testSession(HttpSession session){
        session.setAttribute("key","cz");
        sessionService.testSession();
        return "success";
    }
}
