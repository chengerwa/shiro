package com.cheng.session;

import com.cheng.util.JedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 继承 AbstractSessionDAO
 * 重写创建、读取、修改、删除等方法，将存储方式改为redis的方式存储
 * @author cheng
 *         2018/11/4 16:45
 */
public class RedisSessionDao extends AbstractSessionDAO {

    @Resource
    private JedisUtil jedisUtil;

    private final String SHIRO_SESSION_PREFIX = "cheng-session:";

    /*第一次会话获取会话并且存储到redis*/
    @Override
    protected Serializable doCreate(Session session) {
        System.out.println("会话管理：在redisSessionDao中新增session");
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        saveSession(session);

        return sessionId;
    }

    /*读取redis中的sessionid对应的session会话*/
    @Override
    protected Session doReadSession(Serializable sessionId) {

        System.out.println("会话管理：在redisSession方法读取session");
        System.out.println("sessionId值： "+sessionId.toString());
        if (sessionId == null) {
            return null;
        }
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtil.get(key);

        return (Session) SerializationUtils.deserialize(value);
    }

    /*修改redis中的sessionid对应的session会话*/
    @Override
    public void update(Session session) throws UnknownSessionException {
        System.out.println("会话管理：在redisSessionDao中修改session");
        if (session != null && session.getId() != null) {
            saveSession(session);
        }
    }

    /*删除redis中的sessionid对应的session会话*/
    @Override
    public void delete(Session session) {

        if (session == null || session.getId() == null) {
            return;
        }

        byte[] key = getKey(session.getId().toString());
        jedisUtil.delete(key);
    }

    /*获取redis中的前缀为shiro对应的session值*/
    @Override
    public Collection<Session> getActiveSessions() {

        Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSION_PREFIX);
        Set<Session> sessions = new HashSet<>();
        if (CollectionUtils.isEmpty(keys)) {
            return sessions;
        }

        for (byte[] key : keys) {
            Session session = (Session) SerializationUtils.deserialize(jedisUtil.get(key));
            sessions.add(session);
        }

        return sessions;
    }

    private void saveSession(Session session) {

        if (session != null && session.getId() != null) {
            byte[] key = getKey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);

            jedisUtil.set(key, value);
            jedisUtil.expire(key, 600);
        }
    }

    private byte[] getKey(String key) {
        return (SHIRO_SESSION_PREFIX + key).getBytes();
    }
}
