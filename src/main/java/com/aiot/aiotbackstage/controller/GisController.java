package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.model.param.DisasterSituationGisParam;
import com.aiot.aiotbackstage.model.param.SeedlingGrowthGisParam;
import com.aiot.aiotbackstage.service.IGisService;
import com.aiot.aiotbackstage.service.impl.SysInsectRecStatisServiceImpl;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description GIS数据API
 * @Author xiaowenhui
 * @CreateTime 2020/2/8 16:52
 */
@Controller
@RequestMapping("gis")
@Api(tags = "GIS数据API", description = "GiS Controller")
@Slf4j
@CrossOrigin
public class GisController {

    @Autowired
    private IGisService iGisService;

    @Autowired
    private SysInsectRecStatisServiceImpl sysInsectRecStatisService;

    @ApiOperation(value = "站点数据接口GIS(stationInfo)", notes = "站点数据接口(stationInfo)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @ResponseBody
    @GetMapping("/station")
    public Result stationInfo(
    ) {
        return Result.success(iGisService.stationInfo());
    }

    @ApiOperation(value = "站点数据接口(stationData)", notes = "站点数据接口(stationData)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功")
    })
    @ResponseBody
    @GetMapping("/stationData")
    public Result stationData(
    ) {
        return Result.success(iGisService.stationData());
    }

    @ApiOperation(value = "苗情(seedlingGrowth)注意：该接口只用于GIS", notes = "苗情(seedlingGrowth)")
    @ResponseBody
    @PostMapping("/seedlingGrowth")
    public Result seedlingGrowth(@RequestBody SeedlingGrowthGisParam param
    ) {
        iGisService.seedlingGrowth(param);
        return Result.success();
    }

    @ApiOperation(value = "灾情(disasterSituation)注意：该接口只用于GIS", notes = "灾情(disasterSituation)")
    @ResponseBody
    @PostMapping("/disasterSituation")
    public Result disasterSituation(@RequestBody DisasterSituationGisParam param
    ) {
        iGisService.disasterSituation(param);
        return Result.success();
    }

    @ApiOperation(value = "传感器数据接口：该接口只用于GIS", notes = "传感器数据接口")
    @ResponseBody
    @PostMapping("/sensorInfo")
    public Result sensorInfo(@RequestParam Integer stationId
    ) {
        return Result.success(iGisService.sensorInfo(stationId));
    }

    @ApiOperation("害虫数量统计")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "startDate", name = "开始时间"),
            @ApiImplicitParam(value = "endDate", name = "结束时间")
    })
    @ResponseBody
    @PostMapping("pestNumStat")
    public Result pestNumStat(@RequestBody Map<String, Object> params) {
        Object startDate = params.get("startDate");
        Object endDate = params.get("endDate");
        return Result.success(sysInsectRecStatisService.getPestNumStat(
                startDate == null ? null : String.valueOf(startDate),
                endDate == null ? null : String.valueOf(endDate)
        ));
    }

    @ApiOperation("每种害虫数量统计")
    @ResponseBody
    @PostMapping("perPestNumStat")
    public Result perPestNumStat() {
        return Result.success(sysInsectRecStatisService.getPerPestNumStat());
    }

    @ApiOperation("每天害虫数量统计")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "startDate", name = "开始时间"),
            @ApiImplicitParam(value = "endDate", name = "结束时间")
    })
    @ResponseBody
    @PostMapping("pestNumStat/daily")
    public Result allPestNumStatDaily(@RequestBody Map<String, Object> params) {
        Object startDate = params.get("startDate");
        Object endDate = params.get("endDate");
        return Result.success(sysInsectRecStatisService.getPestNumStatDaily(
                startDate == null ? null : String.valueOf(startDate),
                endDate == null ? null : String.valueOf(endDate)
        ));
    }

    @ApiOperation("每月害虫数量统计")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "year", name = "年")
    })
    @ResponseBody
    @PostMapping("pestNumStat/monthly")
    public Result allPestNumStatMonthly(@RequestBody Map<String, Object> params) {
        if (!params.containsKey("year")) {
            return Result.error(ResultStatusCode.PARAM_NOT_COMPLETE);
        }
        return Result.success(sysInsectRecStatisService.getPestNumStatMonthly(String.valueOf(params.get("year"))));
    }
}