package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.service.IFourQingService;
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
@RequestMapping("fourqing")
@Api(tags = "四情数据API", description = "FourQing Controller")
@Slf4j
@CrossOrigin
public class FourQingController {


    @Autowired
    private IFourQingService fourQingService;

    @ApiOperation(value = "实时监测数据接口(monitorInfo)", notes = "实时监测数据接口(monitorInfo)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @GetMapping("/monitorInfo")
    public Result monitorInfo(@RequestParam Long stationId
    ) {
        return Result.success(fourQingService.monitorInfo(stationId));
    }

}
