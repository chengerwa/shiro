package com.cheng.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 自定义 Realm
 *
 * @author cheng
 *         2019/11/2 20:49
 */
public class CustomRealm extends AuthorizingRealm {

   //模拟数据库存储数据
    Map<String, String> userMap = new HashMap<>(16);

    {
        //userMap.put("cheng", "123");
        userMap.put("cheng", "66f469382db2328c876b700deb336220");

        super.setName("customRealm");
    }

    /**
    * 授权方法
    * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

         /* 1、从 principals获取主身份信息*/
        //将getPrimaryPrincipal方法返回值转为真实身份类型（在上边的doGetAuthenticationInfo认证通过填充到SimpleAuthenticationInfo中身份类型）

        String username = (String) principals.getPrimaryPrincipal();

        /*2、根据身份信息获取权限信息*/
        //连接数据库...
        //角色信息
        Set<String> roles = getRolesByUserName(username);
        //权限信息
        Set<String> permissions = getPermissionsByUsername(username);

        /*3、查到权限数据，返回授权信息(要包括 上边的permissions)*/
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }

    /**
     *认证方法
     * 我们在测试代码中，定义的UserNamePasswordToken对象
     * 有我们保存的需要验证的账号密码信息
     *
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        /* 1. 从主体传过来的认证信息中，获得用户名、获取账号信息 */
        String username = (String) authenticationToken.getPrincipal();

        /* 2. 通过用户名到数据库中获取凭证 */
        String password = getPasswordByUsername(username);
        if (password == null) {
            return null;
        }
        /* 3. 将用户名放入认证容器中 */
        SimpleAuthenticationInfo authenticationInfo =
                new SimpleAuthenticationInfo(username, password, "customRealm");

       /* // 加密步骤中设置加密的 盐
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("cheng"));*/
        /* 4. 如果查询到返回认证信息AuthenticationInfo */
        return authenticationInfo;
    }


    /**
     * 模拟数据库查询角色信息
     *
     * @param username
     * @return
     */
    private Set<String> getRolesByUserName(String username) {

        Set<String> set = new HashSet<>();

        // 从数据库或者缓存中获取角色数据
        set.add("admin");
        set.add("user");
        return set;
    }

    /**
     * 模拟数据库查询权限信息
     *
     * @param username
     * @return
     */
    private Set<String> getPermissionsByUsername(String username) {

        Set<String> set = new HashSet<>();

        set.add("user:delete");
        set.add("user:update");
        return set;
    }

    /**
     * 模拟数据库查询凭证信息
     *
     * @param username
     * @return
     */
    private String getPasswordByUsername(String username) {
        return userMap.get(username);
    }

    public static void main(String[] args) {

        // 密码 + 盐 加密后的结果
        Md5Hash md5Hash = new Md5Hash("123", "cheng");
        System.out.println(md5Hash.toString());
    }
}
