package com.cheng.service.impl;

import com.cheng.service.SessionService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;


/**
 * @ClassName:test
 * @Description:测试注释
 * @Author cz
 * @date 2019年
 */
@Service
public class SessionServiceImpl implements SessionService {

    @Override
    public void testSession() {
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String str = (String)session.getAttribute("key");
        System.out.println("测试"+str);
        //session.stop();
    }
}
