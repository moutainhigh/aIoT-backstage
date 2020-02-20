package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.model.param.AddPestBankParam;
import com.aiot.aiotbackstage.model.param.ModifyPestBankParam;
import com.aiot.aiotbackstage.model.param.PestBankParam;
import com.aiot.aiotbackstage.service.IPestBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
 * @Description 害虫库API
 * @Author xiaowenhui
 * @CreateTime 2020/2/18 9:43
 */
@Controller
@RequestMapping("pestBank")
@Api(tags = "害虫库API", description = "pestBank Controller")
@CrossOrigin
public class PestBankController {


    @Autowired
    private IPestBankService pestBankService;

    @ApiOperation(value = "查询所有害虫并分页或者根据单个害虫查询(pestInfo)", notes = "查询所有害虫并分页或者根据单个害虫查询(pestInfo)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @GetMapping
    public Result pestInfo(@RequestParam(required = false) String pestName,
                           @RequestParam Integer pageNumber,
                           @RequestParam Integer pageSize
                           ) {
        PestBankParam param=new PestBankParam();
        param.setPestName(pestName);
        param.setPageNumber(pageNumber);
        param.setPageSize(pageSize);
        return Result.success(pestBankService.pestInfo(param));
    }

    @ApiOperation(value = "害虫上报(addPestInfo)", notes = "害虫上报(addPestInfo)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @PostMapping
    public Result addPestInfo(@RequestBody AddPestBankParam param
    ) {
        pestBankService.addPestInfo(param);
        return Result.success();
    }

    @ApiOperation(value = "害虫信息修改(modifyPestInfo)", notes = "害虫信息修改(modifyPestInfo)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @PutMapping
    public Result modifyPestInfo(@RequestBody ModifyPestBankParam param
    ) {
        pestBankService.modifyPestInfo(param);
        return Result.success();
    }

    @ApiOperation(value = "害虫信息删除(modifyPestInfo)", notes = "害虫信息删除(modifyPestInfo)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @DeleteMapping("/{id}")
    public Result deletePestInfo(@PathVariable Long id
    ) {
        pestBankService.deletePestInfo(id);
        return Result.success();
    }

    /**
     * 下载虫情库excel模板
     */
    @ApiOperation(value = "下载excel模板(downloadExcel)", notes = "下载excel模板(downloadExcel)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @GetMapping("/downloadExcel")
    public void downloadPermMatrix( HttpServletResponse response) {
        Workbook wb;
        try {
            ClassPathResource resource = new ClassPathResource("excel/excel.xlsx");
            InputStream inputStream = resource.getInputStream();
            // 根据不同excel创建不同对象,Excel2003版本-->HSSFWorkbook,Excel2007版本-->XSSFWorkbook
            wb = WorkbookFactory.create(inputStream);
            response.reset();
            response.setContentType("multipart/form-data");
            if (wb.getClass().getSimpleName() == "HSSFWorkbook") {
                response.setHeader("Content-Disposition",
                        "attachment; filename=" + new String("excel-model".getBytes(), "iso8859-1") + ".xls");
            } else {
                response.setHeader("Content-Disposition",
                        "attachment; filename=" + new String("excel-model".getBytes(), "iso8859-1") + ".xlsx");
            }
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出虫情库excel
     */
    @ApiOperation(value = "导出虫情库excel(exportExcel)", notes = "导出虫情库excel(exportExcel)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @GetMapping("/exportExcel")
    @ResponseBody
    public Result exportPermMatrix(@RequestParam(required = false) String pestName, HttpServletRequest request, HttpServletResponse response) throws IOException {

        pestBankService.exportExcel(pestName,request,response);
        return Result.success();
    }

    /**
     * 批量导入虫情数据
     */
    @ApiOperation(value = "批量导入虫情数据(importExcel)", notes = "批量导入虫情数据(importExcel)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @PostMapping("/importExcel")
    @ResponseBody
    public Result importWatchExcel(@RequestParam("excelFile") MultipartFile xlsFile) {

        pestBankService.importWatchExcel(xlsFile);

        return Result.success();
    }

}
