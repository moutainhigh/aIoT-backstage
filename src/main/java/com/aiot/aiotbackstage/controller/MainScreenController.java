package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.service.IMainScreenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description  大屏展示数据API
 * @Author xiaowenhui
 * @CreateTime 2020/2/8 16:52
 */
@Controller
@RequestMapping("gis")
@Api(tags = "大屏展示数据API", description = "MainScreen Controller")
@Slf4j
@CrossOrigin
public class MainScreenController {


    @Autowired
    private IMainScreenService mainScreenService;

    @ApiOperation(value = "柱状图(histogram)", notes = "柱状图(histogram)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @GetMapping("/histogram")
    public Result histogram(
    ) {
        return Result.success(mainScreenService.histogram());
    }

    @ApiOperation(value = "环状图(Circular graph)", notes = "环状图(Circular graph)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @GetMapping("/circularGraph")
    public Result circularGraph(
    ) {
        return Result.success(mainScreenService.circularGraph());
    }

    @ApiOperation(value = "表格(formData)", notes = "表格(formData)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @GetMapping("/formData")
    public Result formData(
    ) {
        return Result.success(mainScreenService.formData());
    }

}
