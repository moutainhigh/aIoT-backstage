package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.mapper.SysUserMapper;
import com.aiot.aiotbackstage.model.entity.SysUserEntity;
import com.aiot.aiotbackstage.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName UserManageServiceImpl
 * @Description 用户管理接口
 * @Author xiaowenhui
 * @Email 610729719@qq.com
 * @Date 2020/01/02  11:07
 * @Version 1.0
 **/
@Service
public class UserManageServiceImpl implements UserManageService {

    @Autowired
    private SysUserMapper userMapper;


    @Override
    public List<SysUserEntity> userInfoList() {

        List<SysUserEntity> sysUserEntities = userMapper.selectAll();
        if(CollectionUtils.isEmpty(sysUserEntities)){
            throw new MyException(ResultStatusCode.USER_LIST_NO_EXIT);
        }
        return sysUserEntities;
    }
}
