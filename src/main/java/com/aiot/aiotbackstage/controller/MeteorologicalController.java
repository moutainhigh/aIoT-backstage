package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.service.IMeteorologicalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description 气象数据统计
 * @Author xiaowenhui
 * @CreateTime 2020/3/23 13:55
 */
@Controller
@RequestMapping("meteorological")
@Api(tags = "气象数据统计API", description = "Meteorological Controller")
public class MeteorologicalController {


    @Autowired
    private IMeteorologicalService meteorologicalService;

    /**
     * 获取气象信息
     * @return
     */
    @ApiOperation(value = "获取气象信息", notes = "获取气象信息")
    @PostMapping
    @ResponseBody
    public Result meteorological(@RequestBody PageParam param){

        return Result.success(meteorologicalService.meteorological(param));

    }

    /**
     * 导入气象信息
     */
    @ApiOperation(value = "导入气象信息", notes = "导入气象信息")
    @PostMapping("/sensorExcelIn")
    @ResponseBody
//    @RequiresPermissions("sensorExcelIn:add")
    public Result meteorologicalExcelInput(@RequestParam MultipartFile excelFile) {

        meteorologicalService.meteorologicalExcelInput(excelFile);

        return Result.success();
    }


    /**
     * 导出气象信息
     * @return
     */
    @ApiOperation(value = "导出气象信息", notes = "导出气象信息")
    @PostMapping("/sensorExcelOut")
//    @RequiresPermissions("sensorExcelOut:get")
    public Result meteorologicalExcelOutput(HttpServletRequest request, HttpServletResponse response) throws IOException {

        meteorologicalService.meteorologicalExcelOutput(request,response);

        return Result.success();
    }


    /**
     * 下载有害生物excel模板
     */
    @ApiOperation(value = "下载气象信息数据excel模板(downloadExcel)", notes = "下载气象信息数据excel模板(downloadExcel)")
    @GetMapping("/downloadExcel")
    public void downloadPermMatrix( HttpServletResponse response) {
        Workbook wb;
        try {
            ClassPathResource resource = new ClassPathResource("excel/excel_sensor.xlsx");
            InputStream inputStream = resource.getInputStream();
            wb = WorkbookFactory.create(inputStream);
            response.setContentType("multipart/form-data");
            if (wb.getClass().getSimpleName() == "HSSFWorkbook") {
                response.setHeader("Content-Disposition",
                        "attachment; filename=" + new String("excel-meteorological".getBytes(), "iso8859-1") + ".xls");
            } else {
                response.setHeader("Content-Disposition",
                        "attachment; filename=" + new String("excel-meteorological".getBytes(), "iso8859-1") + ".xlsx");
            }
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
