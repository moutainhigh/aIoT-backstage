package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.model.entity.SysPestAdoptionEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.service.IPestAdoptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/24 14:19
 */
@Controller
@RequestMapping("adoption")
@Api(tags = "有害生物采集API", description = "PestAdoption Controller")
@Slf4j
@CrossOrigin
public class PestAdoptionController {


    @Autowired
    private IPestAdoptionService pestAdoptionService;

    /**
     * 下载有害生物excel模板
     */
    @ApiOperation(value = "下载有害生物excel模板(downloadExcel)", notes = "下载excel模板(downloadExcel)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @GetMapping("/downloadExcel")
    public void downloadPermMatrix( HttpServletResponse response) {
        Workbook wb;
        try {
            ClassPathResource resource = new ClassPathResource("excel/excel_pest_adoption.xlsx");
            InputStream inputStream = resource.getInputStream();
            // 根据不同excel创建不同对象,Excel2003版本-->HSSFWorkbook,Excel2007版本-->XSSFWorkbook
            wb = WorkbookFactory.create(inputStream);
//            response.reset();
            response.setContentType("multipart/form-data");
            if (wb.getClass().getSimpleName() == "HSSFWorkbook") {
                response.setHeader("Content-Disposition",
                        "attachment; filename=" + new String("excel-pest_adoption".getBytes(), "iso8859-1") + ".xls");
            } else {
                response.setHeader("Content-Disposition",
                        "attachment; filename=" + new String("excel-pest_adoption".getBytes(), "iso8859-1") + ".xlsx");
            }
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量导入有害生物报表数据
     */
    @ApiOperation(value = "批量导入有害生物报表数据(importExcel)", notes = "批量导入有害生物报表数据(importExcel)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @PostMapping("/importExcel")
    @ResponseBody
    @RequiresPermissions("importExcel:add")
    public Result importWatchExcel(@RequestParam MultipartFile excelFile) {

        pestAdoptionService.importWatchExcel(excelFile);

        return Result.success();
    }

    /**
     * 有害生物诊断信息查询
     */
    @ApiOperation(value = "有害生物诊断信息查询", notes = "有害生物诊断信息查询")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @GetMapping("/pestAdoption")
    @ResponseBody
    public Result pestAdoption(@RequestParam Integer pageNumber,@RequestParam Integer pageSize) {

        PageParam pageParam=new PageParam();
        pageParam.setPageNumber(pageNumber);
        pageParam.setPageSize(pageSize);
        return Result.success(pestAdoptionService.pestAdoption(pageParam));
    }

    /**
     * 有害生物诊断信息编辑
     */
    @ApiOperation(value = "有害生物诊断信息编辑", notes = "有害生物诊断信息编辑")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @PutMapping("/pestAdoptionEdit")
    @ResponseBody
    public Result pestAdoptionEdit(@RequestBody SysPestAdoptionEntity param) {

        pestAdoptionService.pestAdoptionEdit(param);
        return Result.success();
    }

}
