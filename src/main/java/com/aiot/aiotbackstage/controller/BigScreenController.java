package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.service.IBigScreenService;
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
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/3/3 15:26
 */
@Controller
@RequestMapping("bigScreen")
@Api(tags = "大屏数据展示", description = "bigScreen Controller")
@CrossOrigin
public class BigScreenController {


    @Autowired
    private IBigScreenService bigScreenService;

    @ApiOperation(value = "害虫数量统计", notes = "害虫数量统计")
    @ResponseBody
    @GetMapping("/insectStatistics")
    public Result insectStatistics(
    ) {
        return Result.success(bigScreenService.insectStatistics());
    }

    @ApiOperation(value = "土壤数据统计", notes = "土壤数据统计")
    @ResponseBody
    @GetMapping("/dustRecStatistics")
    public Result dustRecStatistics(
    ) {
        return Result.success(bigScreenService.dustRecStatistics());
    }

    @ApiOperation(value = "气象数据统计", notes = "气象数据统计")
    @ResponseBody
    @GetMapping("/sensorRecStatistics")
    public Result sensorRecStatistics(
    ) {
        return Result.success(bigScreenService.sensorRecStatistics());
    }
}
