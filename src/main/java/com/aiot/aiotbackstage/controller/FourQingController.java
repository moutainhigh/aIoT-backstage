package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.model.param.GetInsectRecReportParam;
import com.aiot.aiotbackstage.model.param.InsectRecReportParam;
import com.aiot.aiotbackstage.service.IFourQingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public Result meteorological(@RequestParam @Validated Long stationId
    ) {
        return Result.success(fourQingService.meteorological(stationId));
    }

    /**
     * 虫情动态上报新增
     */
    @ApiOperation(value = "虫情动态上报(新增insectRecReport)", notes = "虫情动态上报(insectRecReport)")
    @ResponseBody
    @PostMapping("/insectRecReport")
    @RequiresPermissions("insectRecReport:add")
    public Result insectRecReport(@RequestBody @Validated InsectRecReportParam recReportParam
                                  ) {
        fourQingService.insectRecReport(recReportParam);
        return Result.success();
    }

    /**
     * 虫情动态上报编辑
     */
    @ApiOperation(value = "虫情动态上报(修改insectRecReport)", notes = "虫情动态上报(insectRecReport)")
    @ResponseBody
    @PutMapping("/insectRecReport")
    @RequiresPermissions("insectRecReport:update")
    public Result insectRecReportModify(@RequestBody @Validated InsectRecReportParam recReportParam
    ) {
        fourQingService.insectRecReportModify(recReportParam);
        return Result.success();
    }

    /**
     * 虫情动态上报审核
     */
    @ApiOperation(value = "虫情动态上报审核", notes = "虫情动态上报审核")
    @ResponseBody
    @PutMapping("/examine")
    @RequiresPermissions("insectRecReport:examine")
    public Result examine(@RequestParam Long id
    ) {
        fourQingService.examine(id);
        return Result.success();
    }

    /**
     * 害虫图片上传
     */
    @ApiOperation(value = "害虫图片上传(pestUpload)", notes = "害虫图片上传(pestUpload)")
    @ResponseBody
    @PostMapping("/pestUpload")
    public Result pestUpload(@RequestParam(value = "file", required = false) MultipartFile multipartFile
    ) {
        return Result.success(fourQingService.pestUpload(multipartFile));
    }

    /**
     * 虫情动态上报查询
     */
    @ApiOperation(value = "虫情动态上报查询(insectRecReport)", notes = "虫情动态上报(insectRecReport)")
    @ResponseBody
    @GetMapping("/insectRecReport")
    public Result insectRecReportGet(@RequestParam Integer whetherExamine, @RequestParam Integer pageNumber, @RequestParam Integer pageSize
                                  ) {
        GetInsectRecReportParam recReportParam=new GetInsectRecReportParam();
        recReportParam.setWhetherExamine(whetherExamine);
        recReportParam.setPageNumber(pageNumber);
        recReportParam.setPageSize(pageSize);
        return Result.success(fourQingService.insectRecReportGet(recReportParam));
    }

}
