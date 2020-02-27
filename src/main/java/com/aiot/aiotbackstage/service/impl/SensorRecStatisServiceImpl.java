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
    public List<SysSensorRecStatisEntity> getPestMeteInfo(String siteId, String startDate, String endDate, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        List<SysSensorRecStatisEntity> result = sysSensorRecStatisMapper.findAllBySiteId(params);


        // 查询当天每小时平均值
        if (startDate.equals(endDate)) {
//            int total = sysSensorRecStatisMapper.countAllBySiteId(params);
//            params.put("pageIndex", pageIndex);
//            params.put("pageSize", pageSize);
//            List<SysSensorRecStatisEntity> result = sysSensorRecStatisMapper.findAllBySiteId(params);
//            return PageResult.<SysSensorRecStatisEntity>builder()
//                    .total(total)
//                    .pageNumber(pageIndex)
//                    .pageSize(pageSize)
//                    .pageData(result)
//                    .build();
            return result;
        }
//        int total = sysSensorRecStatisMapper.countAllBySiteIdDaily(params);
//        List<SysSensorRecStatisEntity> resultHourly = sysSensorRecStatisMapper.findAllBySiteId(params);
//        params.put("pageIndex", pageIndex);
//        params.put("pageSize", pageSize);
//        List<SysSensorRecStatisEntity> resultDaily = sysSensorRecStatisMapper.findAllBySiteIdDaily(params);
        return formatDayOrNight(result);
    }

    private List<SysSensorRecStatisEntity> formatDayOrNight2(List<SysSensorRecStatisEntity> resultDaily, List<SysSensorRecStatisEntity> resultHourly) {
        Map<String, List<SysSensorRecStatisEntity>> map = new HashMap<>();
        List<SysSensorRecStatisEntity> tempList;
        for (SysSensorRecStatisEntity entity : resultHourly) {
            tempList = new ArrayList<>();
            if (map.containsKey(entity.getDate())) {
                tempList = map.get(entity.getDate());
            } else {
                map.put(entity.getDate(), tempList);
            }
            tempList.add(entity);
        }

        List<SysSensorRecStatisEntity> data = new ArrayList<>();
        SysSensorRecStatisEntity entity;
        for (Map.Entry<String, List<SysSensorRecStatisEntity>> entry : map.entrySet()) {
            entity = new SysSensorRecStatisEntity();
            entity.setDate(entry.getKey());
            data.add(entity);

            for (SysSensorRecStatisEntity entityDaily : resultDaily) {
                if (entityDaily.getDate().equals(entry.getKey())) {

                }
            }

            for (SysSensorRecStatisEntity item : entry.getValue()) {
                entity.setWindSpeed(DoubleUtils.average(entity.getWindSpeed(), item.getWindSpeed()));
                entity.setWindDirection(DoubleUtils.average(entity.getWindDirection(), item.getWindDirection()));
                entity.setHumidity(DoubleUtils.average(entity.getHumidity(), item.getHumidity()));
                entity.setTemperature(DoubleUtils.average(entity.getTemperature(), item.getTemperature()));
                entity.setNoisy(DoubleUtils.average(entity.getNoisy(), item.getNoisy()));
                entity.setPM10(DoubleUtils.average(entity.getPM10(), item.getPM10()));
                entity.setPM25(DoubleUtils.average(entity.getPM25(), item.getPM25()));
                entity.setAtmos(DoubleUtils.average(entity.getAtmos(), item.getAtmos()));

                // 白天
                if (item.getHour() > 6 && item.getHour() < 19) {
                    entity.setHumidityDay(DoubleUtils.average(entity.getHumidityDay(), item.getHumidity()));
                    entity.setTemperatureDay(DoubleUtils.average(entity.getTemperatureDay(), item.getTemperature()));
                } else {
                    entity.setHumidityNight(DoubleUtils.average(entity.getHumidityNight(), item.getHumidity()));
                    entity.setTemperatureNight(DoubleUtils.average(entity.getTemperatureNight(), item.getTemperature()));
                }
            }
        }
        return data;
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
            params.clear();
            params.put("siteId", siteId);
            params.put("startDate", pestDate);
            params.put("endDate", pestDate);
            int total = sysSensorRecStatisMapper.countAllBySiteId(params);
            params.put("pageIndex", pageIndex);
            params.put("pageSize", pageSize);
            return PageResult.<SysSensorRecStatisEntity>builder()
                    .total(total)
                    .pageSize(pageSize)
                    .pageNumber(pageIndex)
                    .pageData(formatDayOrNight(sysSensorRecStatisMapper.findAllBySiteId(params)))
                    .build();
        }
        return null;
    }

    private List<SysSensorRecStatisEntity> formatDayOrNight(List<SysSensorRecStatisEntity> list) {
        Map<String, List<SysSensorRecStatisEntity>> map = new HashMap<>();
        List<SysSensorRecStatisEntity> tempList;
        for (SysSensorRecStatisEntity entity : list) {
            tempList = new ArrayList<>();
            if (map.containsKey(entity.getDate())) {
                tempList = map.get(entity.getDate());
            } else {
                map.put(entity.getDate(), tempList);
            }
            tempList.add(entity);
        }

        List<SysSensorRecStatisEntity> data = new ArrayList<>();
        SysSensorRecStatisEntity entity;
        for (Map.Entry<String, List<SysSensorRecStatisEntity>> entry : map.entrySet()) {
            entity = new SysSensorRecStatisEntity();
            entity.setDate(entry.getKey());
            data.add(entity);

            for (SysSensorRecStatisEntity item : entry.getValue()) {
                entity.setWindSpeed(DoubleUtils.average(entity.getWindSpeed(), item.getWindSpeed()));
                entity.setWindDirection(DoubleUtils.average(entity.getWindDirection(), item.getWindDirection()));
                entity.setHumidity(DoubleUtils.average(entity.getHumidity(), item.getHumidity()));
                entity.setTemperature(DoubleUtils.average(entity.getTemperature(), item.getTemperature()));
                entity.setNoisy(DoubleUtils.average(entity.getNoisy(), item.getNoisy()));
                entity.setPM10(DoubleUtils.average(entity.getPM10(), item.getPM10()));
                entity.setPM25(DoubleUtils.average(entity.getPM25(), item.getPM25()));
                entity.setAtmos(DoubleUtils.average(entity.getAtmos(), item.getAtmos()));

                // 白天
                if (item.getHour() > 6 && item.getHour() < 19) {
                    entity.setHumidityDay(DoubleUtils.average(entity.getHumidityDay(), item.getHumidity()));
                    entity.setTemperatureDay(DoubleUtils.average(entity.getTemperatureDay(), item.getTemperature()));
                } else {
                    entity.setHumidityNight(DoubleUtils.average(entity.getHumidityNight(), item.getHumidity()));
                    entity.setTemperatureNight(DoubleUtils.average(entity.getTemperatureNight(), item.getTemperature()));
                }
            }
        }

        return data;
    }
}
