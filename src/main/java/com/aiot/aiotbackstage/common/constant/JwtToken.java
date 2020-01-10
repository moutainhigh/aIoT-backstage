package com.aiot.aiotbackstage.common.constant;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Description JWT获取token信息
 * @Author xiaowenhui
 * @CreateTime 2020/1/7 13:32
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    /**
     * 获得token中的信息
     * @return token中包含的wxOpenId
     */
    public static String getWxOpenId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("wxOpenId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
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
