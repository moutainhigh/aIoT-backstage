package com.aiot.aiotbackstage.common.util;

import com.aiot.aiotbackstage.model.entity.SysUserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * JWT 工具类
 */
@Component
@Slf4j
public class JWTUtil {

    /**
     * JWT 自定义密钥 我这里写死的
     */
    private static final String SECRET_KEY = "5371f568a45e5ab1f442c38e0932aef24447139b";
    /**
     * JWT 过期时间值 这里写死为和小程序时间一致 7200 秒，也就是两个小时
     */
    private static long expire_time = 7200;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 生成 token, 10min后过期
     *
     * @param userEntity 用户名
     * @return 加密的token
     */
    public  String createToken(SysUserEntity userEntity) {
        String jwtId = UUID.randomUUID().toString();                 //JWT 随机ID,做为验证的key
        //1 . 加密算法进行签名得到token
        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(SECRET_KEY);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String token="";
        //1-微信登陆，2-PC登陆
        if(userEntity.getUserName() == null){
            token= JWT.create()
                    .withClaim("wxOpenId", userEntity.getWxOpenid())
                    .withClaim("sessionKey", userEntity.getSessionKey())
                    .withClaim("userId", userEntity.getUserId())
                    .withClaim("jwt-id", jwtId)
                    .withExpiresAt(new Date(System.currentTimeMillis() + expire_time*1000))
                    .sign(algorithm);
        }else{
            token= JWT.create()
                    .withClaim("userName", userEntity.getUserName())
                    .withClaim("userId", userEntity.getUserId())
                    .withClaim("jwt-id", jwtId)
                    .withExpiresAt(new Date(System.currentTimeMillis() + expire_time*1000))
                    .sign(algorithm);
        }
        log.info("token的key:{}","JWT-SESSION-" + jwtId);
        //缓存redis
        redisTemplate.opsForValue().set("JWT-SESSION-" + jwtId,
                token,
                expire_time,
                TimeUnit.SECONDS);
        //设置超时时间
        redisTemplate.expire("JWT-SESSION-" + jwtId,
                expire_time,
                TimeUnit.SECONDS);
        return token;
    }

    /**
     * 根据Token 获取jwt-id
     */
    public String getJwtIdByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("jwt-id").asString();
    }

    /**
     * 校验 token 是否正确
     *
     * userEntity
     */
    public  boolean jwtTokenRefresh(String token, SysUserEntity userEntity) {
        String jwtIdByToken =getJwtIdByToken(token);
        String cacheToken = String.valueOf(redisTemplate.opsForValue().get("JWT-SESSION-"+jwtIdByToken));
        if(StringUtils.isNotBlank(cacheToken)&&!cacheToken.equals("null")){
            // 校验token有效性
            if (!this.verify(cacheToken, userEntity)) {
                this.createToken(userEntity);
            } else {
                redisTemplate.opsForValue().set("JWT-SESSION-" + jwtIdByToken,
                        cacheToken,
                        expire_time,
                        TimeUnit.SECONDS);
                // 设置超时时间
                redisTemplate.expire( "JWT-SESSION-"+jwtIdByToken,
                        expire_time,
                        TimeUnit.SECONDS);
            }
            return true;
        }
        return false;
    }

    /**
     * 校验token是否正确
     */
    public  boolean verify(String token, SysUserEntity userEntity) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier;
            if(getUserNameByToken(token) == null ){
                verifier = JWT.require(algorithm)
                        .withClaim("wxOpenId", userEntity.getWxOpenid())
                        .withClaim("sessionKey", userEntity.getSessionKey())
                        .withClaim("userId", userEntity.getUserId())
                        .withClaim("jwt-id", getJwtIdByToken(token))
                        .acceptExpiresAt(System.currentTimeMillis() + expire_time*1000 )
                        .build();
            }else {
                verifier = JWT.require(algorithm)
                        .withClaim("userName", userEntity.getUserName())
                        .withClaim("userId", userEntity.getUserId())
                        .withClaim("jwt-id", getJwtIdByToken(token))
                        .acceptExpiresAt(System.currentTimeMillis() + expire_time*1000 )
                        .build();
            }
            //3 . 验证token
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 根据Token获取userId
     */
    public Long getUserIdByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("userId").asLong();
    }

    /**
     * 根据Token获取userName(注意坑点 : 就算token不正确，也有可能解密出userName,同下)
     */
    public String getUserNameByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("userName").asString();
    }

    /**
     * 根据Token获取wxOpenId(注意坑点 : 就算token不正确，也有可能解密出wxOpenId,同下)
     */
    public String getWxOpenIdByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("wxOpenId").asString();
    }

    /**
     * 根据Token获取sessionKey
     */
    public String getSessionKeyByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("sessionKey").asString();
    }
}
