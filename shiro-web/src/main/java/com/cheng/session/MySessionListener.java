package com.cheng.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 实现sessionListener
 * 重写 创建、停止、过期三个节点需要监听的内容
 *
 * @Author cz
 * @date 2019年
 */

public class MySessionListener implements SessionListener {
    private final AtomicInteger sessionCount = new AtomicInteger(0);
    //session创建时触发
    @Override
    public void onStart(Session session) {
        sessionCount.incrementAndGet();
        System.out.println("会话监听：session登陆+1== "+sessionCount.get());
    }

    /*
    * session停止时触发， subject.logout() session.stop
    *
    * */
    @Override
    public void onStop(Session session) {
        sessionCount.decrementAndGet();
        System.out.println("会话监听：session登陆-1== "+sessionCount.get());
    }

    /*
    * session过期时触发 ，静默时间查过了过期时间
    * */
    @Override
    public void onExpiration(Session session) {
        sessionCount.decrementAndGet();
        System.out.println("会话监听：session登陆过期-1== "+sessionCount.get());
    }
}
