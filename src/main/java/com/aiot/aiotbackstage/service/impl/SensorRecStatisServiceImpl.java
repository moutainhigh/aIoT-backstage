package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.util.DoubleUtils;
import com.aiot.aiotbackstage.mapper.SysInsectRecStatisMapper;
import com.aiot.aiotbackstage.mapper.SysSensorRecStatisMapper;
import com.aiot.aiotbackstage.model.entity.SysSensorRecStatisEntity;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.service.ISensorRecStatisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SensorRecStatisServiceImpl extends ServiceImpl<SysSensorRecStatisMapper, SysSensorRecStatisEntity>
        implements ISensorRecStatisService {

    @Autowired
    private SysInsectRecStatisMapper sysInsectRecStatisMapper;

    @Autowired
    private SysSensorRecStatisMapper sysSensorRecStatisMapper;

    /**
     * 获取每天土壤信息平均值
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public PageResult<SysSensorRecStatisEntity> getPestMeteInfo(String siteId, String startDate, String endDate, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        // 查询当天每小时平均值
        if (startDate.equals(endDate)) {
            int total = sysSensorRecStatisMapper.countAllBySiteId(params);
            params.put("pageIndex", pageIndex);
            params.put("pageSize", pageSize);
            List<SysSensorRecStatisEntity> result = sysSensorRecStatisMapper.findAllBySiteId(params);
            return PageResult.<SysSensorRecStatisEntity>builder()
                    .total(total)
                    .pageNumber(pageIndex)
                    .pageSize(pageSize)
                    .pageData(result)
                    .build();
        }
        return getDayOrNightAverage(siteId, startDate, endDate, pageIndex, pageSize);
    }


    /**
     * 最*虫害气象信息
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param isMax     1：最大，0：最小
     * @param pageIndex 页码
     * @param pageSize  分页大小
     * @return
     */
    @Override
    public PageResult<SysSensorRecStatisEntity> getMaxOrMinPestMeteInfo(String siteId, String startDate, String endDate, int isMax, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("isMax", isMax);
        List<Map<String, Object>> pestResult = sysInsectRecStatisMapper.findMaxOrMinPestDate(params);

        if (pestResult != null && pestResult.size() == 1) {
            String pestDate = String.valueOf(pestResult.get(0).get("date"));
            return getDayOrNightAverage(siteId, pestDate, pestDate, pageIndex, pageSize);
        }
        return null;
    }

    private PageResult<SysSensorRecStatisEntity> getDayOrNightAverage(String siteId, String startDate, String endDate, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        int total = sysSensorRecStatisMapper.countAllBySiteIdDaily(params);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        List<SysSensorRecStatisEntity> result = sysSensorRecStatisMapper.findAllBySiteIdDaily(params);

        List<SysSensorRecStatisEntity> tempList;
        for (SysSensorRecStatisEntity entity : result) {
            params.clear();
            params.put("siteId", siteId);
            params.put("startDate", entity.getDate());
            params.put("endDate", entity.getDate());
            tempList = sysSensorRecStatisMapper.findAllBySiteIdDailyDay(params);
            if (!tempList.isEmpty()) {
                entity.setHumidityDay(tempList.get(0).getHumidity());
                entity.setTemperatureDay(tempList.get(0).getTemperature());
            }
            tempList = sysSensorRecStatisMapper.findAllBySiteIdDailyNight(params);
            if (!tempList.isEmpty()) {
                entity.setHumidityNight(tempList.get(0).getHumidity());
                entity.setTemperatureNight(tempList.get(0).getTemperature());
            }
        }
        return PageResult.<SysSensorRecStatisEntity>builder()
                .total(total)
                .pageNumber(pageIndex)
                .pageSize(pageSize)
                .pageData(result)
                .build();
    }
}
