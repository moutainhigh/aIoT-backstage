package com.aiot.aiotbackstage.common.util;

import org.apache.poi.hssf.usermodel.HSSFBorderFormatting;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/19 11:10
 */
public class ExcelUtils {

    public static Map<String,Object> setExcelStyle(){
        HSSFWorkbook workBook = new HSSFWorkbook();// 创建excel工作簿
        HSSFFont headFont = workBook.createFont();
        headFont.setFontName("微软雅黑");
        headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headFont.setFontHeightInPoints((short) 12);// 字体大小
        headFont.setColor(HSSFColor.BLACK.index);
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
        headStyle.setTopBorderColor(HSSFColor.BLACK.index);
        headStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        headStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        headStyle.setRightBorderColor(HSSFColor.BLACK.index);
        headStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        // 设置普通单元格样式
        HSSFCellStyle style = workBook.createCellStyle();
        HSSFFont font = workBook.createFont();
        font.setFontName("Arial");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        font.setFontHeightInPoints((short) 10);// 字体大小
        style.setFont(font);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
        style.setWrapText(true);
        //设置上下左右四个边框宽度
        style.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
        style.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
        style.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
        style.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
        //设置上下左右四个边框颜色
        style.setTopBorderColor(HSSFColor.BLACK.index);
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        style.setRightBorderColor(HSSFColor.BLACK.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("workBook",workBook);
        resultMap.put("headStyle",headStyle);
        resultMap.put("style",style);
        return resultMap;
    }
}
