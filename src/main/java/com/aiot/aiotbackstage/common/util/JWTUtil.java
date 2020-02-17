package com.aiot.aiotbackstage.common.util;

import com.aiot.aiotbackstage.model.entity.SysUserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA
 *
 * @Author yuanhaoyue swithaoy@gmail.com
 * @Description JWT 工具类
 * @Date 2018-04-07
 * @Time 22:48
 */
@Component
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
     * 生成 token, 5min后过期
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
                    .withClaim("jwt-id", jwtId)
                    .withExpiresAt(new Date(System.currentTimeMillis() + expire_time*1000))
                    .sign(algorithm);
        }else{
            token= JWT.create()
                    .withClaim("userName", userEntity.getUserName())
                    .withClaim("jwt-id", jwtId)
                    .withExpiresAt(new Date(System.currentTimeMillis() + expire_time*1000))
                    .sign(algorithm);
        }
        //2 . Redis缓存JWT, 注 : 请和JWT过期时间一致
        redisTemplate.opsForValue().set("JWT-SESSION-" + jwtId, token, expire_time, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 根据Token 获取jwt-id
     */
    private String getJwtIdByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim("jwt-id").asString();
    }

    /**
     * 校验 token 是否正确
     *
     * @param token 用户名
     * @return 是否正确
     */
    public  boolean verify(String token) {
        try {

            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier=null;
            if(getUserNameByToken(token) == null ){
                verifier = JWT.require(algorithm)
                        .withClaim("wxOpenId", getWxOpenIdByToken(token))
                        .withClaim("sessionKey", getSessionKeyByToken(token))
                        .withClaim("jwt-id", getJwtIdByToken(token))
                        .acceptExpiresAt(System.currentTimeMillis() + expire_time*1000 )  //JWT 正确的配置续期姿势
                        .build();
            }else {
                verifier = JWT.require(algorithm)
                        .withClaim("userName", getUserNameByToken(token))
                        .withClaim("jwt-id", getJwtIdByToken(token))
                        .acceptExpiresAt(System.currentTimeMillis() + expire_time*1000 )  //JWT 正确的配置续期姿势
                        .build();
            }
            //3 . 验证token
            verifier.verify(token);
            return true;
        } catch (Exception e) { //捕捉到任何异常都视为校验失败
            return false;
        }
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
