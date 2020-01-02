package com.aiot.aiotbackstage.service;


import com.aiot.aiotbackstage.model.entity.SysUserEntity;

import java.util.List;

/**
 * @ClassName UserManageService
 * @Description 用户管理接口
 * @Author xiaowenhui
 * @Email 610729719@qq.com
 * @Date 2020/01/02  11:07
 * @Version 1.0
 **/
public interface UserManageService {

    List<SysUserEntity> userInfoList();
}
