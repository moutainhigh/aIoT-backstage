package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.model.entity.SysNewRuleEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.param.SysNewRuleParam;
import com.aiot.aiotbackstage.model.param.WarnInfoParam;
import com.aiot.aiotbackstage.model.param.WarnRuleParam;
import com.aiot.aiotbackstage.service.IEarlyWarningService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/24 10:33
 */
@CrossOrigin
@Controller
@RequestMapping("warn")
@Api(tags = "预警API", description = "EarlyWarning Controller")
public class EarlyWarningController {


    @Autowired
    private IEarlyWarningService iEarlyWarningService;

    @ApiOperation(value = "查询预警库数据(earlyInfoPage)", notes = "查询预警库信息(earlyInfo)")
    @ResponseBody
    @GetMapping("/warnRule")
    public Result earlyInfoPage(@RequestParam(required = false) String earlyType, @RequestParam Integer pageNumber,
                                @RequestParam Integer pageSize
    ) {
        PageParam pageParam=new PageParam();
        pageParam.setPageNumber(pageNumber);
        pageParam.setPageSize(pageSize);
        return Result.success(iEarlyWarningService.earlyInfoPage(earlyType,pageParam));
    }

    @ApiOperation(value = "新增预警库数据(warnRuleAdd)", notes = "新增预警库信息(warnRuleAdd)")
    @ResponseBody
    @PostMapping("/warnRule")
//    @RequiresPermissions("warnRule:add")
    public Result earlyInfoAdd(@RequestBody @Validated WarnRuleParam param
    ) {
        iEarlyWarningService.earlyInfoAdd(param);
        return Result.success();
    }

    @ApiOperation(value = "专家新增预警库数据(newRule)", notes = "新增预警库信息(newRule)")
    @ResponseBody
    @PostMapping("/newRule")
//    @RequiresPermissions("newRule:add")
    public Result newRule(@RequestBody @Validated SysNewRuleEntity param
    ) {
        iEarlyWarningService.newRule(param);
        return Result.success();
    }

    @ApiOperation(value = "专家测试预警", notes = "专家测试预警")
    @ResponseBody
    @PostMapping("/test")
    public Result test() throws Exception {
        iEarlyWarningService.earlyWarningReportNew();
        return Result.success();
    }

    @ApiOperation(value = "专家编辑预警库数据(alertRule)", notes = "专家编辑预警库数据(alertRule)")
    @ResponseBody
    @PutMapping("/alertRule")
//    @RequiresPermissions("alertRule:add")
    public Result alertRule(@RequestBody @Validated SysNewRuleEntity param
    ) {
        iEarlyWarningService.alertRule(param);
        return Result.success();
    }

    @ApiOperation(value = "专家查询预警库数据(ruleInfo)", notes = "专家查询预警库数据(ruleInfo)")
    @ResponseBody
    @PostMapping("/ruleInfo")
//    @RequiresPermissions("alertRule:add")
    public Result ruleInfo(@RequestBody @Validated PageParam param
    ) {
        return Result.success(iEarlyWarningService.ruleInfo(param));
    }


    @ApiOperation(value = "修改预警库数据(warnRuleUpdate)", notes = "新增预警库信息(warnRuleUpdate)")
    @ResponseBody
    @PutMapping("/warnRule")
    @RequiresPermissions("warnRule:update")
    public Result earlyInfoModify(@RequestBody @Validated WarnRuleParam param
    ) {
        iEarlyWarningService.earlyInfoModify(param);
        return Result.success();
    }

    @ApiOperation(value = "修改预警信息数据(warnInfoModify)", notes = "修改预警信息数据(warnInfoModify)")
    @ResponseBody
    @PutMapping("/warnInfo/update")
    @RequiresPermissions("warnInfo:update")
    public Result earlyInfoModify(@RequestBody @Validated WarnInfoParam param
    ) {
        iEarlyWarningService.earlyInfoUpdate(param);
        return Result.success();
    }

    @ApiOperation(value = "删除预警库数据(warnRuleAdd)", notes = "新增预警库信息(warnRuleAdd)")
    @ResponseBody
    @DeleteMapping("/warnRule/{id}")
    @RequiresPermissions("warnRule:delete")
    public Result earlyInfoDelete(@PathVariable  Long  id
    ) {
        iEarlyWarningService.earlyInfoDelete(id);
        return Result.success();
    }


    @ApiOperation(value = "预警信息上报(earlyInfoReport)", notes = "预警信息上报(earlyInfoReport)")
    @ResponseBody
    @PostMapping("/earlyInfoReport")
    @RequiresPermissions("earlyInfoReport")
    public Result earlyInfoReport(@RequestBody @Validated WarnInfoParam param,@RequestHeader String token
    ) {
        iEarlyWarningService.earlyInfoReport(param,token);
        return Result.success();
    }

    @ApiOperation(value = "预警上报信息查询(earlyInfoReportPage)", notes = "预警信息查询(earlyInfoReport)")
    @ResponseBody
    @GetMapping("/earlyInfoReport")
    public Result earlyInfoReportPage(@RequestParam Integer pageNumber,
                                      @RequestParam Integer pageSize
    ) {
        PageParam pageParam=new PageParam();
        pageParam.setPageNumber(pageNumber);
        pageParam.setPageSize(pageSize);
        return Result.success(iEarlyWarningService.earlyInfoReportPage(pageParam));
    }

    @ApiOperation(value = "关闭预警信息(earlyClosed)", notes = "关闭预警信息(earlyClosed)")
    @ResponseBody
    @PutMapping("/earlyInfoClosed")
    @RequiresPermissions("earlyInfoClosed")
    public Result earlyClosed(@RequestParam Long id
    ) {
        iEarlyWarningService.earlyClosed(id);
        return Result.success();
    }

    @ApiOperation(value = "预警上报信息审核(earlyInfoExamine)", notes = "预警上报信息审核(earlyInfoExamine)")
    @ResponseBody
    @PutMapping("/earlyInfoExamine")
    @RequiresPermissions("earlyInfoExamine")
    public Result earlyInfoExamine(@RequestParam Long id
    ) {
        iEarlyWarningService.earlyInfoExamine(id);
        return Result.success();
    }

    @ApiOperation(value = "预警规则内容获取(earlyContent)", notes = "预警内容获取(earlyContent)")
    @ResponseBody
    @GetMapping("/earlyContent")
    public Result earlyContent(@RequestParam String earlyType,@RequestParam String earlyDegree
    ) {
        return Result.success(iEarlyWarningService.earlyContent(earlyType,earlyDegree));
    }

    @ApiOperation(value = "预警数量提示(earlyCount)", notes = "预警数量提示(earlyCount)")
    @ResponseBody
    @GetMapping("/earlyCount")
    public Result earlyCount(
    ) {
        return Result.success(iEarlyWarningService.earlyCount());
    }

    @ApiOperation(value = "预警规则下拉框数据(earlyData)", notes = "预警规则下拉框数据(earlyData)")
    @ResponseBody
    @GetMapping("/earlyData")
    public Result earlyData(@RequestParam Integer type
    ) {
        return Result.success(iEarlyWarningService.earlyData(type));
    }


    @ApiOperation(value = "规则字典属性获取(warnDictionaries)", notes = "规则字典属性获取(warnDictionaries)")
    @ResponseBody
    @GetMapping("/warnDictionaries")
    public Result warnDictionaries(
    ) {
        return Result.success(iEarlyWarningService.warnDictionaries());
    }
}
