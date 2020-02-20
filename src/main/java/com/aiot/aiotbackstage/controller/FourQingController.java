package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.service.IFourQingService;
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


}
