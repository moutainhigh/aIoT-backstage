package com.aiot.aiotbackstage.common.shiro;

import com.aiot.aiotbackstage.common.util.JWTUtil;
import com.aiot.aiotbackstage.mapper.*;
import com.aiot.aiotbackstage.model.entity.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA
 *
 * @Author yuanhaoyue swithaoy@gmail.com
 * @Description 自定义 Realm
 * @Date 2018-04-09
 * @Time 16:58
 */
@Slf4j
@Component
public class CustomRealm extends AuthorizingRealm {


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
    private JWTUtil jwtUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * JWT 过期时间值 这里写死为和小程序时间一致 7200 秒，也就是两个小时
     */
    private static long expire_time = 7200;

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("————身份认证方法————");
        String jwtToken = (String) authenticationToken.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String jwtId = UUID.randomUUID().toString();
        String wxOpenId = jwtUtil.getWxOpenIdByToken(jwtToken);
        String sessionKey = jwtUtil.getSessionKeyByToken(jwtToken);
        String userName = jwtUtil.getUserNameByToken(jwtToken);
        if(userName == null){
            if (wxOpenId == null || wxOpenId.equals(""))
                throw new AuthenticationException("user account not exits , please check your token");
            if (sessionKey == null || sessionKey.equals(""))
                throw new AuthenticationException("sessionKey is invalid , please check your token");
        }else{
            if (userName == null || userName.equals(""))
                throw new AuthenticationException("user account not exits , please check your token");
        }
        if (!jwtUtil.verify(jwtToken))
            throw new AuthenticationException("token is invalid , please check your token");
        redisTemplate.opsForValue().set("JWT-SESSION-" + jwtId, jwtToken, expire_time, TimeUnit.SECONDS);
        return new SimpleAuthenticationInfo(jwtToken, jwtToken, "CustomRealm");
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("————权限认证————");
        String token =(String)principals.getPrimaryPrincipal();

        return setSimpleAuthorizationInfo(token);
    }

    /**
     * 设置权限
     * @param token
     */
    private SimpleAuthorizationInfo setSimpleAuthorizationInfo(String token){
        SysUserEntity user=null;
        String wxOpenId = jwtUtil.getWxOpenIdByToken(token);
        String userName = jwtUtil.getUserNameByToken(token);
        if(userName == null){
            user= sysUserMapper.selectOne(Wrappers.<SysUserEntity>lambdaQuery()
                    .eq(SysUserEntity::getWxOpenid,wxOpenId));
        }else{
            user= sysUserMapper.selectOne(Wrappers.<SysUserEntity>lambdaQuery()
                    .eq(SysUserEntity::getUserName,userName));
        }
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
                                .eq(SysRoleMenuEntity::getRoleId, role.getRoleId()));
                List<Long> menuIds = sysRoleMenuEntities.stream()
                        .map(SysRoleMenuEntity::getMenuId).collect(Collectors.toList());
                List<SysMenuEntity> sysMenuEntities = sysMenuMapper.selectBatchIds(menuIds);
                if(sysMenuEntities.size()>0) {
                    for(SysMenuEntity module : sysMenuEntities) {
                        permissions.add(module.getPerms());
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
