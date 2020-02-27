package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.model.param.DisasterSituationGisParam;
import com.aiot.aiotbackstage.model.param.SeedlingGrowthGisParam;
import com.aiot.aiotbackstage.service.IGisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description  GIS数据API
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

    @ApiOperation(value = "站点数据接口(stationInfo)", notes = "站点数据接口(stationInfo)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @GetMapping("/station")
    public Result stationInfo(
    ) {
        return Result.success(iGisService.stationInfo());
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
    public Result disasterSituation(@RequestBody DisasterSituationGisParam  param
                                    ) {
        iGisService.disasterSituation(param);
        return Result.success();
    }

    @ApiOperation(value = "传感器数据接口：该接口只用于GIS", notes = "传感器数据接口")
    @ResponseBody
    @PostMapping("/sensorInfo")
    public Result sensorInfo(@RequestParam Long  stationId
    ) {
        return Result.success(iGisService.sensorInfo(stationId));
    }
}
