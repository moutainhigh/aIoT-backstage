package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.mapper.SysPestAdoptionMapper;
import com.aiot.aiotbackstage.model.entity.SysInsectRecReportEntity;
import com.aiot.aiotbackstage.model.entity.SysPestAdoptionEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.service.IPestAdoptionService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/24 14:30
 */
@Service
public class PestAdoptionServiceImpl implements IPestAdoptionService {

    @Autowired
    private SysPestAdoptionMapper pestAdoptionMapper;

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
                SysPestAdoptionEntity pestAdoptionEntity=new SysPestAdoptionEntity();
                pestAdoptionEntity.setDistributionSituation(row.getCell(0).toString());
                pestAdoptionEntity.setDistributionArea(row.getCell(1).toString());
                pestAdoptionEntity.setDistributionAddress(row.getCell(2).toString());
                pestAdoptionEntity.setArea(row.getCell(3).toString());
                pestAdoptionEntity.setCount(Double.valueOf(String.valueOf(row.getCell(4))).intValue());
                pestAdoptionEntity.setPestBankName(row.getCell(5).toString());
                pestAdoptionEntity.setCauseDisease(row.getCell(6).toString());
                pestAdoptionEntity.setSource(row.getCell(7).toString());
                pestAdoptionEntity.setCreateTime(new Date());
                pestAdoptionMapper.insert(pestAdoptionEntity);
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
    public PageResult<SysPestAdoptionEntity> pestAdoption(PageParam pageParam) {

        List<SysPestAdoptionEntity> sysPestAdoptionEntities = pestAdoptionMapper.pestAdoptionPage(pageParam);
        Integer total = pestAdoptionMapper.selectCount(null);
        if(CollectionUtils.isEmpty(sysPestAdoptionEntities)){
            throw new MyException(ResultStatusCode.DB_ERR);
        }
        return PageResult.<SysPestAdoptionEntity>builder().total(total)
                .pageData(sysPestAdoptionEntities)
                .pageNumber(pageParam.getPageNumber())
                .pageSize(pageParam.getPageSize())
                .build();
    }
}
