package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.common.util.ExcelUtils;
import com.aiot.aiotbackstage.mapper.SysSceneMapper;
import com.aiot.aiotbackstage.model.entity.SysDustRecStatisEntity;
import com.aiot.aiotbackstage.model.entity.SysPestBankEntity;
import com.aiot.aiotbackstage.model.entity.SysSceneEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.service.IMeteorologicalService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @Description 气象数据处理
 * @Author xiaowenhui
 * @CreateTime 2020/3/23 14:14
 */
@Service
@Slf4j
public class MeteorologicalServiceImpl implements IMeteorologicalService {


    @Autowired
    private SysSceneMapper sceneMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void meteorologicalExcelInput(MultipartFile xlsFile) {
        if (xlsFile.isEmpty()) {
            throw new MyException(ResultStatusCode.IMPORT_IS_NULL);
        }
        // 根据不同excel创建不同对象,Excel2003版本-->HSSFWorkbook,Excel2007版本-->XSSFWorkbook
        Workbook wb = null;
        InputStream im = null;
        try {
            im = xlsFile.getInputStream();
            wb = WorkbookFactory.create(im);
            // 根据页面index 获取sheet页
            Sheet sheet = wb.getSheetAt(0);
            Row row = null;
            // 循环sheet页中数据从第x行开始,例:第2行开始为导入数据
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                // 获取每一行数据
                row = sheet.getRow(i);
                // 输出表格内容,此处可替换为数据插入操作
                SysSceneEntity sceneEntity=new SysSceneEntity();
                sceneEntity.setCity(row.getCell(0).toString());
                sceneEntity.setTime(row.getCell(1).toString());
                sceneEntity.setTmin(row.getCell(2).toString());
                sceneEntity.setTmax(row.getCell(3).toString());
                sceneEntity.setTavg(row.getCell(4).toString());
                sceneEntity.setHumidity(row.getCell(5).toString());
                sceneEntity.setWindSpeed(row.getCell(6).toString());
                sceneEntity.setWindScale(row.getCell(7).toString());
                sceneEntity.setPressure(row.getCell(8).toString());
                sceneEntity.setVisibility(row.getCell(9).toString());
                sceneEntity.setSumPrecipitation(row.getCell(10).toString());
                sceneEntity.setAvgTotalCloud(row.getCell(11).toString());
                sceneMapper.insert(sceneEntity);
            }
        } catch (Exception e1) {
            // 回滚数据
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e1.printStackTrace();
        } finally {
            try {
                im.close();
                wb.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override
    public void meteorologicalExcelOutput(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> stringObjectMap = ExcelUtils.setExcelStyle();
        HSSFWorkbook workBook = (HSSFWorkbook)stringObjectMap.get("workBook");
        HSSFCellStyle headStyle = (HSSFCellStyle)stringObjectMap.get("headStyle");
        HSSFCellStyle style = (HSSFCellStyle)stringObjectMap.get("style");

        HSSFSheet sheet = workBook.createSheet("气象信息");
        sheet.setColumnWidth(0,30 * 256);
        sheet.setColumnWidth(1,20 * 256);
        sheet.setColumnWidth(2,20 * 256);
        sheet.setColumnWidth(3,20 * 256);
        sheet.setColumnWidth(4,20 * 256);
        sheet.setColumnWidth(5,20 * 256);
        sheet.setColumnWidth(6,20 * 256);
        sheet.setColumnWidth(7,20 * 256);
        sheet.setColumnWidth(8,20 * 256);
        sheet.setColumnWidth(9,20 * 256);
        sheet.setColumnWidth(10,20 * 256);
        sheet.setColumnWidth(11,20 * 256);
        //此处添加数据
        HSSFRow headerRow1 = sheet.createRow(0);
        headerRow1.setHeight((short) 600);// 设置表格行的高度
        HSSFCell cell0 = headerRow1.createCell(0);
        cell0.setCellValue(new HSSFRichTextString("城市"));// 设置表名
        cell0.setCellStyle(headStyle);
        HSSFCell cell1 = headerRow1.createCell(1);
        cell1.setCellValue(new HSSFRichTextString("日期"));// 设置表名
        cell1.setCellStyle(headStyle);
        HSSFCell cell2 = headerRow1.createCell(2);
        cell2.setCellValue(new HSSFRichTextString("最低温度(℃)"));// 设置表名
        cell2.setCellStyle(headStyle);
        HSSFCell cell3 = headerRow1.createCell(3);
        cell3.setCellValue(new HSSFRichTextString("最高温度(℃)"));// 设置表名
        cell3.setCellStyle(headStyle);
        HSSFCell cell4 = headerRow1.createCell(4);
        cell4.setCellValue(new HSSFRichTextString("平均温度(℃)"));// 设置表名
        cell4.setCellStyle(headStyle);
        HSSFCell cell5 = headerRow1.createCell(5);
        cell5.setCellValue(new HSSFRichTextString("湿度(%)"));// 设置表名
        cell5.setCellStyle(headStyle);
        HSSFCell cell6 = headerRow1.createCell(6);
        cell6.setCellValue(new HSSFRichTextString("风速(m/s)"));// 设置表名
        cell6.setCellStyle(headStyle);
        HSSFCell cell7 = headerRow1.createCell(7);
        cell7.setCellValue(new HSSFRichTextString("风级(级)"));// 设置表名
        cell7.setCellStyle(headStyle);
        HSSFCell cell8 = headerRow1.createCell(8);
        cell8.setCellValue(new HSSFRichTextString("气压(hpa)"));// 设置表名
        cell8.setCellStyle(headStyle);
        HSSFCell cell9 = headerRow1.createCell(9);
        cell9.setCellValue(new HSSFRichTextString("能见度(km)"));// 设置表名
        cell9.setCellStyle(headStyle);
        HSSFCell cell10 = headerRow1.createCell(10);
        cell10.setCellValue(new HSSFRichTextString("总降水量(mm)"));// 设置表名
        cell10.setCellStyle(headStyle);
        HSSFCell cell11 = headerRow1.createCell(11);
        cell11.setCellValue(new HSSFRichTextString("平均总云量(%)"));// 设置表名
        cell11.setCellStyle(headStyle);

        List<SysSceneEntity> sceneEntities = sceneMapper.selectList(null);
        sceneEntities.stream().forEach(sceneEntity -> {
            HSSFRow headerRow2 = sheet.createRow(sceneEntity.getId().intValue());
            HSSFCell cell12 = headerRow2.createCell(0);
            cell12.setCellValue(sceneEntity.getCity());
            cell12.setCellStyle(style);
            HSSFCell cell13 = headerRow2.createCell(1);
            cell13.setCellValue(sceneEntity.getTime());
            cell13.setCellStyle(style);
            HSSFCell cell14 = headerRow2.createCell(2);
            cell14.setCellValue(sceneEntity.getTmin());
            cell14.setCellStyle(style);
            HSSFCell cell15 = headerRow2.createCell(3);
            cell15.setCellValue(sceneEntity.getTmax());
            cell15.setCellStyle(style);
            HSSFCell cell16 = headerRow2.createCell(4);
            cell16.setCellValue(sceneEntity.getTavg());
            cell16.setCellStyle(style);
            HSSFCell cell17 = headerRow2.createCell(5);
            cell17.setCellValue(sceneEntity.getHumidity());
            cell17.setCellStyle(style);
            HSSFCell cell18 = headerRow2.createCell(6);
            cell18.setCellValue(sceneEntity.getWindSpeed());
            cell18.setCellStyle(style);
            HSSFCell cell19 = headerRow2.createCell(7);
            cell19.setCellValue(sceneEntity.getWindScale());
            cell19.setCellStyle(style);
            HSSFCell cell20 = headerRow2.createCell(8);
            cell20.setCellValue(sceneEntity.getPressure());
            cell20.setCellStyle(style);
            HSSFCell cell21 = headerRow2.createCell(9);
            cell21.setCellValue(sceneEntity.getVisibility());
            cell21.setCellStyle(style);
            HSSFCell cell22 = headerRow2.createCell(10);
            cell22.setCellValue(sceneEntity.getSumPrecipitation());
            cell22.setCellStyle(style);
            HSSFCell cell23 = headerRow2.createCell(11);
            cell23.setCellValue(sceneEntity.getAvgTotalCloud());
            cell23.setCellStyle(style);

        });
        //清空response
        response.setHeader("Content-Disposition",
                "attachment; filename=" + new String("excel-model".getBytes(), "iso8859-1") + ".xls");
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        workBook.write(os);
        os.flush();
        os.close();
        workBook.close();
    }

    @Override
    public PageResult<SysSceneEntity> meteorological(PageParam param) {
        IPage<SysSceneEntity> sysSceneEntityIPage = sceneMapper.selectPage(new Page<>(param.getPageNumber(), param.getPageSize()), null);
        if(sysSceneEntityIPage == null){
            return null;
        }
        List<SysSceneEntity> records = sysSceneEntityIPage.getRecords();
        Long total = sysSceneEntityIPage.getTotal();
        return PageResult.<SysSceneEntity>builder()
                .total(total.intValue())
                .pageData(records)
                .pageSize(param.getPageSize())
                .pageNumber(param.getPageNumber())
                .build();
    }
}
