package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.util.DateUtils;
import com.aiot.aiotbackstage.model.param.DeviceInfoNewParam;
import com.aiot.aiotbackstage.model.param.DeviceInfoOldParam;
import com.aiot.aiotbackstage.model.param.DeviceInfoParam;
import com.aiot.aiotbackstage.service.IDeviceService;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description  设备管理API
 * @Author xiaowenhui
 * @CreateTime 2020/2/8 16:52
 */
@Controller
@RequestMapping("device")
@Api(tags = "设备管理API", description = "Device Controller")
@Slf4j
@CrossOrigin
public class DeviceController {

    @Autowired
    private IDeviceService deviceService;

    @ApiOperation(value = "实时设备查询", notes = "实时设备查询")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @GetMapping("/deviceInfoNew")
    public Result deviceInfoNew(
                                ) {
        return Result.success(deviceService.deviceInfoNew());
    }

    @ApiOperation(value = "实时设备编辑", notes = "实时设备编辑")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @PutMapping("/deviceInfoNew")
    @RequiresPermissions("serveAnalysis:update")
    public Result deviceInfoNewModify(@RequestBody DeviceInfoParam param
    ) {
        deviceService.deviceInfoNewModify(param);
        return Result.success();
    }

    @ApiOperation(value = "历史设备查询", notes = "历史设备查询")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @PostMapping("/deviceInfoOld")
    public Result deviceInfoOld(@RequestBody DeviceInfoOldParam param
                                ) {
        return Result.success(deviceService.deviceInfoOld(param));
    }

    @ApiOperation(value = "历史设备编辑", notes = "历史设备编辑")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @PutMapping("/deviceInfoOld")
    @RequiresPermissions("historyAnalysis:update")
    public Result deviceInfoOldModify(@RequestBody DeviceInfoParam param
    ) {
        deviceService.deviceInfoOldModify(param);
        return Result.success();
    }

    @ApiOperation(value = "历史设备删除", notes = "历史设备删除")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @DeleteMapping("/deviceInfoOld/{id}")
    @RequiresPermissions("historyAnalysis:delete")
    public Result deviceInfoOldModify(@PathVariable Integer id
    ) {
        deviceService.deviceInfoOldDel(id);
        return Result.success();
    }

    @ResponseBody
    @GetMapping("/rtuStatus")
    @ApiOperation(value = "rtu设备状态", notes = "rtu设备状态")
    public Result rtuStatus() {
        List<Map<String, Object>> result = deviceService.rtuStatus();
        return Result.success(result);
    }
}
