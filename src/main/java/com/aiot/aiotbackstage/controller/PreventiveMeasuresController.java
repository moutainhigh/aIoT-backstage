package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.model.entity.SysPreventiveMeasuresEntity;
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
@Api(tags = "虫情诊断API", description = "PreventiveMeasures Controller")
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

    @ApiOperation(value = "防治措施明细", notes = "防治措施明细")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @GetMapping("/preventiveDetail")
    public Result preventiveDetail(@RequestParam(required = false) String preventiveId,@RequestParam(required = false) String insectId
    ) {
        return Result.success(masuresService.preventiveDetail(preventiveId,insectId));
    }

    @ApiOperation(value = "防治措施新增", notes = "防治措施新增")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @PostMapping("/preventiveAdd")
    public Result preventiveAdd(@RequestBody SysPreventiveMeasuresEntity measuresEntity
                                ) {
        masuresService.preventiveAdd(measuresEntity);
        return Result.success();
    }


    @ApiOperation(value = "防治措施编辑", notes = "防治措施编辑")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @PutMapping("/preventiveUpdate")
    public Result preventiveUpdate(@RequestBody SysPreventiveMeasuresEntity measuresEntity
    ) {
        masuresService.preventiveUpdate(measuresEntity);
        return Result.success();
    }

    @ApiOperation(value = "防治措施删除", notes = "防治措施删除")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @DeleteMapping("/preventiveDelete")
    public Result preventiveDelete(@RequestParam Long id
    ) {
        masuresService.preventiveDelete(id);
        return Result.success();

    }



}
