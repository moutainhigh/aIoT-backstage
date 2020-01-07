package com.aiot.aiotbackstage.common.config;

import com.aiot.aiotbackstage.common.constant.JwtToken;
import com.aiot.aiotbackstage.mapper.*;
import com.aiot.aiotbackstage.model.entity.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description 权限规则类
 * @Author xiaowenhui
 * @CreateTime 2020/1/7 13:30
 */
@Component
@Slf4j
public class ShiroRealmConfig {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper roleUserMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * JWT 过期时间值 这里写死为和小程序时间一致 7200 秒，也就是两个小时
     */
    private static long expire_time = 7200;

    /**
     * 配置所有自定义的realm,方便起见,应对可能有多个realm的情况
     */
    public List<Realm> allRealm() {
        List<Realm> realmList = new LinkedList<>();
        AuthorizingRealm jwtRealm = jwtRealm();
        realmList.add(jwtRealm);
        return Collections.unmodifiableList(realmList);
    }

    /**
     * 自定义 JWT的 Realm
     * 重写 Realm 的 supports() 方法是通过 JWT 进行登录判断的关键
     */
    private AuthorizingRealm jwtRealm() {
        AuthorizingRealm jwtRealm = new AuthorizingRealm() {
            /**
             * 注意坑点 : 必须重写此方法，不然Shiro会报错
             * 因为创建了 JWTToken 用于替换Shiro原生 token,所以必须在此方法中显式的进行替换，否则在进行判断时会一直失败
             */
            @Override
            public boolean supports(AuthenticationToken token) {
                return token instanceof JwtToken;
            }

            /**
             * 授权
             * @param principals
             * @return
             */
            @Override
            protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
                log.info("权限配置-->doGetAuthorizationInfo()");
                String token =(String)principals.getPrimaryPrincipal();
                String wxOpenId = JwtToken.getWxOpenId(token);
                return setSimpleAuthorizationInfo(wxOpenId);
            }

            /**
             * 校验 验证token逻辑
             */
            @Override
            protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
                String jwtId = UUID.randomUUID().toString();
                String jwtToken = (String) token.getCredentials();
                String wxOpenId = jwtConfig.getWxOpenIdByToken(jwtToken);
                String sessionKey = jwtConfig.getSessionKeyByToken(jwtToken);
                if (wxOpenId == null || wxOpenId.equals(""))
                    throw new AuthenticationException("user account not exits , please check your token");
                if (sessionKey == null || sessionKey.equals(""))
                    throw new AuthenticationException("sessionKey is invalid , please check your token");
                if (!jwtConfig.verifyToken(jwtToken))
                    throw new AuthenticationException("token is invalid , please check your token");
                redisTemplate.opsForValue().set("JWT-SESSION-" + jwtId, jwtToken, expire_time, TimeUnit.SECONDS);
                return new SimpleAuthenticationInfo(token, token, getName());
            }
        };
        jwtRealm.setCredentialsMatcher(credentialsMatcher());
        return jwtRealm;
    }

    /**
     * 注意坑点 : 密码校验 , 这里因为是JWT形式,就无需密码校验和加密,直接让其返回为true(如果不设置的话,该值默认为false,即始终验证不通过)
     */
    private CredentialsMatcher credentialsMatcher() {
        return (token, info) -> true;
    }

    /**
     * 设置权限
     * @param wxOpenId
     */
    private SimpleAuthorizationInfo setSimpleAuthorizationInfo(String wxOpenId){

        SysUserEntity user= sysUserMapper.selectOne(Wrappers.<SysUserEntity>lambdaQuery()
                .eq(SysUserEntity::getWxOpenid,wxOpenId));
        List<String> permissions=new ArrayList<>();
        List<String> rolesName=new ArrayList<>();
        List<SysUserRoleEntity> sysUserRoleEntities = roleUserMapper
                .selectList(Wrappers.<SysUserRoleEntity>lambdaQuery()
                        .eq(SysUserRoleEntity::getUserId, user.getUserId()));
        List<Long> rootIds = sysUserRoleEntities.stream().map(SysUserRoleEntity::getRoleId).collect(Collectors.toList());
        List<SysRoleEntity> roles = roleMapper.selectBatchIds(rootIds);
        if(roles.size()>0) {
            for(SysRoleEntity role : roles) {
                rolesName.add(role.getRoleName());
                List<SysRoleMenuEntity> sysRoleMenuEntities = sysRoleMenuMapper
                        .selectList(Wrappers.<SysRoleMenuEntity>lambdaQuery()
                                .eq(SysRoleMenuEntity::getRoleId, role));
                List<Long> menuIds = sysRoleMenuEntities.stream()
                        .map(SysRoleMenuEntity::getMenuId).collect(Collectors.toList());
                List<SysMenuEntity> sysMenuEntities = sysMenuMapper.selectBatchIds(menuIds);
                if(sysMenuEntities.size()>0) {
                    for(SysMenuEntity module : sysMenuEntities) {
                        permissions.add(module.getName());
                    }
                }
            }
        }
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        //将角色放入shiro中
        info.addRoles(rolesName);
        //将权限放入shiro中
        info.addStringPermissions(permissions);
        return info;
    }
}
