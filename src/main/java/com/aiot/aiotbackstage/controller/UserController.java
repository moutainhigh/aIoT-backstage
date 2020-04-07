package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.param.UserLoginParam;
import com.aiot.aiotbackstage.model.param.UserParam;
import com.aiot.aiotbackstage.service.IUserService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName UserManageService
 * @Description 用户管理接口
 * @Author xiaowenhui
 * @Email 610729719@qq.com
 * @Date 2020/01/02  11:07
 * @Version 1.0
 **/
@Controller
@CrossOrigin
@Api(tags = "用户管理API", description = "User Controller")
public class UserController {


    @Autowired
    private IUserService iUserService;


    @ApiOperation(value = "用户登录", notes = "用户微信授权登录")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @PostMapping("/login")
    public Result userInfoList(@RequestBody @Validated UserLoginParam userLoginParam){

        return Result.success(iUserService.userLogin(userLoginParam));
    }

    @ApiOperation(value = "用户登出", notes = "用户登出(loginOut)")
    @ResponseBody
    @PostMapping("/loginOut")
    public Result loginOut(@RequestHeader String token){

        iUserService.loginOut(token);
        return Result.success();
    }

    @ResponseBody
    @GetMapping("/unauthorized")
    public Result unauthorized401(){
        return Result.error(ResultStatusCode.TOKEN_NO_EXIT);
    }

    @ResponseBody
    @GetMapping("/unauthorized403")
    public Result unauthorized403(){
        return Result.error(ResultStatusCode.TOKEN_ERR);
    }

    @ResponseBody
    @PostMapping("/test")
    public Result test(){

        iUserService.test();
        return Result.success();
    }

    @ApiOperation(value = "新增用户", notes = "新增用户(saveUser)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功"),
            @ApiResponse(code=210,message = "新增用户失败"),
    })
    @PostMapping("/saveUser")
    @ResponseBody
    @RequiresPermissions("saveUser:add")
    public Result saveUser(@RequestBody @ApiParam(name="新增用户数据",value="传入json格式",required=true)
                           @Validated UserParam userParam){

        iUserService.saveUser(userParam);
        return Result.success();
    }

    @ApiOperation(value = "修改用户", notes = "修改用户(updateUser)")
    @ApiResponses({
            @ApiResponse(code = 211,message = "成功"),
            @ApiResponse(code=2,message = "修改用户失败"),
    })
    @PutMapping("/updateUser")
    @ResponseBody
    @RequiresPermissions("saveUser:update")
    public Result updateUser(@RequestBody @ApiParam(name="修改用户数据",value="传入json格式",required=true)
                             @Validated        UserParam userParam){

        iUserService.updateUser(userParam);
        return Result.success();
    }

    @ApiOperation(value = "查询用户", notes = "查询用户(userPageBy)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @GetMapping("/userPageBy")
    @ResponseBody
    public Result userPageBy(@RequestParam  Integer pageNumber,@RequestParam Integer pageSize){

        PageParam pageParam=new PageParam();
        pageParam.setPageNumber(pageNumber);
        pageParam.setPageSize(pageSize);
        return Result.success(iUserService.userPage(pageParam));
    }

    @ApiOperation(value = "删除用户", notes = "删除用户(delUser)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @DeleteMapping("/delUser/{id}")
    @ResponseBody
    @RequiresPermissions("saveUser:delete")
    public Result delUser(@PathVariable Long id){

        iUserService.delUser(id);
        return Result.success();
    }

    @ApiOperation(value = "判断token是否过期", notes = "判断token是否过期")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @GetMapping("/isToken")
    public Result isToken(@RequestHeader String token){
        iUserService.isToken(token);
        return Result.success();
    }

    @ApiOperation(value = "查询当前用户拥有的权限", notes = "查询当前用户拥有的权限")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @GetMapping("/permissionInfo")
    public Result permissionInfo(@RequestHeader String token){
        return Result.success(iUserService.permissionInfo(token));
    }

    @ApiOperation(value = "天气", notes = "天气")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @GetMapping("/weather")
    public Result weather(@RequestParam String cityName){
        return Result.success(iUserService.weather(cityName));
    }

}
