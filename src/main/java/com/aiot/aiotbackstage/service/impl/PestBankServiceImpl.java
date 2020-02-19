package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.mapper.SysPestBankMapper;
import com.aiot.aiotbackstage.model.entity.SysPestBankEntity;
import com.aiot.aiotbackstage.model.param.AddPestBankParam;
import com.aiot.aiotbackstage.model.param.ModifyPestBankParam;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.param.PestBankParam;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.service.IPestBankService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
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
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * @Description 害虫库业务
 * @Author xiaowenhui
 * @CreateTime 2020/2/18 9:48
 */
@Service
public class PestBankServiceImpl implements IPestBankService {

    @Autowired
    private SysPestBankMapper pestBankMapper;

    @Override
    public PageResult<SysPestBankEntity> pestInfo(PestBankParam param) {

        //如果没有传害虫名称则查询所有
        List<SysPestBankEntity> sysPestBankEntities;
        Integer total;
        PageParam pageQuery=new PageParam();
        pageQuery.setPageSize(param.getPageSize());
        pageQuery.setPageNumber(param.getPageNumber());
        if(param.getPestName() == null){
             sysPestBankEntities =
                    pestBankMapper.pestBankInfoByNamePage(null,pageQuery);
             total = pestBankMapper.selectCount(null);
        }else{
             sysPestBankEntities =
                    pestBankMapper.pestBankInfoByNamePage(param.getPestName(),pageQuery);
             total = pestBankMapper.selectCount(Wrappers.<SysPestBankEntity>lambdaQuery()
                    .like(SysPestBankEntity::getPestName,param.getPestName()));
        }
        return PageResult.<SysPestBankEntity>builder().total(total).pageData(sysPestBankEntities).build();
    }

    @Override
    public void addPestInfo(AddPestBankParam param) {

        SysPestBankEntity pestBankEntity=new SysPestBankEntity();
        pestBankEntity.setPestName(param.getPestName());
        pestBankEntity.setPestType(param.getPestType());
        pestBankEntity.setPestIntroduce(param.getPestIntroduce());
        pestBankEntity.setPestImg(param.getPestImg());
        pestBankEntity.setCreateTime(new Date());
        pestBankEntity.setUpdateTime(new Date());
        pestBankMapper.insert(pestBankEntity);
    }

    @Override
    public void modifyPestInfo(ModifyPestBankParam param) {

        SysPestBankEntity pestBankEntity=new SysPestBankEntity();
        pestBankEntity.setId(param.getPestBankId());
        pestBankEntity.setPestName(param.getPestName());
        pestBankEntity.setPestType(param.getPestType());
        pestBankEntity.setPestIntroduce(param.getPestIntroduce());
        pestBankEntity.setPestImg(param.getPestImg());
        pestBankMapper.updateById(pestBankEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importWatchExcel(MultipartFile xlsFile) {
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
                SysPestBankEntity pestBankEntity=new SysPestBankEntity();
                pestBankEntity.setPestName(row.getCell(0).toString());
                pestBankEntity.setPestType(row.getCell(1).toString());
                pestBankEntity.setPestIntroduce(row.getCell(2).toString());
                pestBankEntity.setPestImg(row.getCell(3).toString());
                pestBankEntity.setCreateTime(new Date());
                pestBankEntity.setUpdateTime(new Date());
                pestBankMapper.insert(pestBankEntity);
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
    public void exportExcel(String pestName, HttpServletRequest request, HttpServletResponse response) throws IOException {

        HSSFWorkbook workBook = new HSSFWorkbook();// 创建excel工作簿
        HSSFFont headFont = workBook.createFont();
        headFont.setFontName("Arial");
        headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headFont.setFontHeightInPoints((short) 10);// 字体大小
        HSSFCellStyle headStyle = workBook.createCellStyle();
        headStyle.setFont(headFont);
        headStyle.setLocked(true);
        headStyle.setWrapText(true);// 自动换行
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
        //设置上下左右四个边框宽度
        headStyle.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
        headStyle.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
        headStyle.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
        headStyle.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
        //设置上下左右四个边框颜色
        headStyle.setTopBorderColor(HSSFColor.RED.index);
        headStyle.setBottomBorderColor(HSSFColor.RED.index);
        headStyle.setLeftBorderColor(HSSFColor.RED.index);
        headStyle.setRightBorderColor(HSSFColor.RED.index);
        headStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFSheet sheet = workBook.createSheet("虫情库信息");
        //此处添加数据
        HSSFRow headerRow1 = sheet.createRow(0);
        headerRow1.setHeight((short) 600);// 设置表格行的高度
        HSSFCell cell0 = headerRow1.createCell(0);
        cell0.setCellValue(new HSSFRichTextString("害虫类型"));// 设置表名
        cell0.setCellStyle(headStyle);
        HSSFCell cell1 = headerRow1.createCell(1);
        cell1.setCellValue(new HSSFRichTextString("害虫名称"));// 设置表名
        cell1.setCellStyle(headStyle);
        HSSFCell cell2 = headerRow1.createCell(2);
        cell2.setCellValue(new HSSFRichTextString("害虫介绍"));// 设置表名
        cell2.setCellStyle(headStyle);
        HSSFCell cell3 = headerRow1.createCell(3);
        cell3.setCellValue(new HSSFRichTextString("害虫图片地址"));// 设置表名
        cell3.setCellStyle(headStyle);

        List<SysPestBankEntity> sysPestBankEntities;
        if(pestName == null){
            sysPestBankEntities = pestBankMapper.selectList(null);
        }else{
            sysPestBankEntities = pestBankMapper
                    .selectList(Wrappers.<SysPestBankEntity>lambdaQuery()
                    .eq(SysPestBankEntity::getPestName, pestName));
        }
        sysPestBankEntities.stream().forEach(sysPestBankEntity -> {
            HSSFRow headerRow2 = sheet.createRow(sysPestBankEntity.getId().intValue());
            headerRow2.createCell(0).setCellValue(sysPestBankEntity.getPestType());
            headerRow2.createCell(1).setCellValue(sysPestBankEntity.getPestName());
            headerRow2.createCell(2).setCellValue(sysPestBankEntity.getPestIntroduce());
            headerRow2.createCell(3).setCellValue(sysPestBankEntity.getPestImg());
        });
        //清空response
        response.reset();
        response.setHeader("Content-Disposition",
                "attachment; filename=" + new String("excel-model".getBytes(), "iso8859-1") + ".xls");
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        workBook.write(os);
        os.flush();
        os.close();
        workBook.close();
    }
}
