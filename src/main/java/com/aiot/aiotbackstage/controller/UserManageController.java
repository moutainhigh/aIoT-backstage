package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.common.util.RedisUtils;
import com.aiot.aiotbackstage.service.UserManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @ClassName UserManageService
 * @Description 用户管理接口
 * @Author xiaowenhui
 * @Email 610729719@qq.com
 * @Date 2020/01/02  11:07
 * @Version 1.0
 **/
@Controller
@RequestMapping("sys/users")
@Api(tags = "用户管理接口", description = "User Controller")
public class UserManageController {

    @Resource
    private RedisUtils redisUtils;

    @Autowired
    private UserManageService userManageService;


    @ApiOperation(value = "用户列表查询", notes = "用户列表查询")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @GetMapping("/list")
    public Result userInfoList(){

        return Result.success(userManageService.userInfoList());
    }


}
