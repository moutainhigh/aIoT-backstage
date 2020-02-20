package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.service.IFourQingService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("fourqing")
@Api(tags = "四情数据API", description = "FourQing Controller")
@Slf4j
@CrossOrigin
public class FourQingController {


    @Autowired
    private IFourQingService fourQingService;

    @ApiOperation(value = "气象信息(meteorological)", notes = "气象信息(meteorological)")
    @ResponseBody
    @GetMapping("/meteorological")
    public Result meteorological(@RequestParam Long stationId
    ) {
        return Result.success(fourQingService.meteorological(stationId));
    }

    @ApiOperation(value = "苗情(seedlingGrowth)注意：该接口只用于GIS", notes = "苗情(seedlingGrowth)")
    @ResponseBody
    @PostMapping("/seedlingGrowth")
    public Result seedlingGrowth(@RequestBody JSONObject jsonParam
    ) {
        log.info("GIS传过来的苗情数据:【{}】",jsonParam.toJSONString());
        fourQingService.seedlingGrowth(jsonParam);
        return Result.success();
    }

    @ApiOperation(value = "灾情(disasterSituation)注意：该接口只用于GIS", notes = "灾情(disasterSituation)")
    @ResponseBody
    @PostMapping("/disasterSituation")
    public Result disasterSituation(@RequestBody JSONObject jsonParam
    ) {
        log.info("GIS传过来的灾情数据:【{}】",jsonParam.toJSONString());
        fourQingService.disasterSituation(jsonParam);
        return Result.success();
    }


}
