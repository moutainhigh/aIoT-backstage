package com.aiot.aiotbackstage.service;


import com.aiot.aiotbackstage.model.entity.SysUserEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.param.UserLoginParam;
import com.aiot.aiotbackstage.model.param.UserParam;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.model.vo.TokenVo;

import java.util.List;
import java.util.Map;

/**
 * @ClassName UserManageService
 * @Description 用户管理接口
 * @Author xiaowenhui
 * @Email 610729719@qq.com
 * @Date 2020/01/02  11:07
 * @Version 1.0
 **/
public interface IUserService {

    /**
     * 1 . 我们的微信小程序端传入code。
     * 2 . 调用微信code2session接口获取openid和session_key
     * 3 . 根据openid和session_key自定义登陆态(Token)
     * 4 . 返回自定义登陆态(Token)给小程序端。
     * 5 . 我们的小程序端调用其他需要认证的api，请在header的Authorization里面携带 token信息
     * @return Token 返回后端 自定义登陆态 token  基于JWT实现
     */
    TokenVo userLogin(UserLoginParam userLoginParam);

    void loginOut(String token);

    void test();

    void saveUser(UserParam userParam);

    void updateUser(UserParam userParam);

    PageResult<SysUserEntity> userPage(PageParam param);

    void delUser(UserParam userParam);

    void isToken(String token);

    List<Map<String,Object>> permissionInfo();
}
