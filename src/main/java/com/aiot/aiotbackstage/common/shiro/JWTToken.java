package com.aiot.aiotbackstage.common.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * xiaowenhui
 *  token 工具类
 */
public class JWTToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;

    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
