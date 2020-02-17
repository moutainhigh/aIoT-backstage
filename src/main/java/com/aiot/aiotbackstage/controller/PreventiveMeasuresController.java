package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.service.IPreventiveMasuresService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description  防治措施
 * @Author xiaowenhui
 * @CreateTime 2020/2/8 16:52
 */
@Controller
@RequestMapping("measures")
@Api(tags = "虫情诊断", description = "PreventiveMeasures Controller")
@Slf4j
@CrossOrigin
public class PreventiveMeasuresController {


    @Autowired
    private IPreventiveMasuresService masuresService;

    @ApiOperation(value = "防治措施(preventiveMasures)", notes = "防治措施(preventiveMasures)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @GetMapping("/preventiveMasures")
    public Result preventiveMasures(@RequestParam(required = false) String name
    ) {
        if(name == null ){
            return Result.success(masuresService.getMasuresInfo());
        }
        return Result.success(masuresService.getMasuresInfoByName(name));
    }



}
