package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.config.WeChatConfig;
import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.common.util.JWTUtil;
import com.aiot.aiotbackstage.common.util.JsonUtils;
import com.aiot.aiotbackstage.mapper.SysUserMapper;
import com.aiot.aiotbackstage.model.entity.SysUserEntity;
import com.aiot.aiotbackstage.model.param.UserLoginParam;
import com.aiot.aiotbackstage.model.vo.Code2SessionVo;
import com.aiot.aiotbackstage.model.vo.TokenVo;
import com.aiot.aiotbackstage.service.IUserService;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * @ClassName UserManageServiceImpl
 * @Description 用户管理接口
 * @Author xiaowenhui
 * @Email 610729719@qq.com
 * @Date 2020/01/02  11:07
 * @Version 1.0
 **/
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private SysUserMapper userMapper;


    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 微信的 code2session 接口 获取微信用户信息
     */
    private String code2Session(String jsCode) {
        log.info("-------jsCode--------",jsCode);
        String urlString = "?appid={app_id}&secret={app_secret}&js_code={jsCode}&grant_type={grant_type}";
        return  restTemplate.getForObject(
                weChatConfig.getSession_host() + urlString, String.class,
                weChatConfig.getApp_id(),
                weChatConfig.getApp_secret(),
                jsCode,
                weChatConfig.getGrant_type());
    }

    /**
     * 1 . 我们的微信小程序端传入code。
     * 2 . 调用微信code2session接口获取openid和session_key
     * 3 . 根据openid和session_key自定义登陆态(Token)
     * 4 . 返回自定义登陆态(Token)给小程序端。
     * 5 . 我们的小程序端调用其他需要认证的api，请在header的Authorization里面携带 token信息
     * @return Token 返回后端 自定义登陆态  token  基于JWT实现
     */
    @Override
    public TokenVo userLogin(UserLoginParam userLoginParam) {

        //如果code为空则PC端登陆
        if(userLoginParam.getLoginType() == 2){
            SysUserEntity sysUserEntity = userMapper.selectOne(Wrappers.<SysUserEntity>lambdaQuery()
                    .eq(SysUserEntity::getUserName, userLoginParam.getUserName()));
            if(ObjectUtils.isEmpty(sysUserEntity)){
                throw new MyException(ResultStatusCode.USER_NAME_NO_EXIT);
            }
            if(!sysUserEntity.getPassword().equals(userLoginParam.getPassword())){
                throw new MyException(ResultStatusCode.USER_PASSWORD_NO_EXIT);
            }
            //5 . JWT 返回自定义登陆态 Token
            String token = jwtUtil.createToken(sysUserEntity);
            return new TokenVo(token);
        }else{
            //1 . code2session返回JSON数据
            String resultJson = code2Session(userLoginParam.getCode());
            //2 . 解析数据
            Code2SessionVo response = JsonUtils.jsonToPojo(resultJson, Code2SessionVo.class);
            if (!response.getErrcode().equals("0")) {
                log.info("code2session失败:", response.getErrmsg());
                throw new AuthenticationException("code2session失败 : " + response.getErrmsg());
            }else {
                //3 . 先从本地数据库中查找用户是否存在
                SysUserEntity sysUserEntity = userMapper.selectOne(Wrappers.<SysUserEntity>lambdaQuery()
                        .eq(SysUserEntity::getWxOpenid, response.getOpenid()));
                if (ObjectUtils.isEmpty(sysUserEntity)) {
                    sysUserEntity.setWxOpenid(response.getOpenid());  //不存在就新建用户
                    sysUserEntity.setSessionKey(response.getSession_key());
                    sysUserEntity.setLoginType(1);
                    sysUserEntity.setCreateTime(new Date());
                    sysUserEntity.setUpdateTime(new Date());
                    userMapper.insert(sysUserEntity);
                }
                //4 . 更新sessionKey和登陆时间
                sysUserEntity.setSessionKey(response.getSession_key());
                sysUserEntity.setUpdateTime(new Date());
                userMapper.updateById(sysUserEntity);
                //5 . JWT 返回自定义登陆态 Token
                String token = jwtUtil.createToken(sysUserEntity);
                return new TokenVo(token);
            }
        }

    }
}
