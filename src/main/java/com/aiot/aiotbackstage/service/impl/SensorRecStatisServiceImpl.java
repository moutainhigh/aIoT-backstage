package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.util.RedisUtils;
import com.aiot.aiotbackstage.mapper.SysInsectRecStatisMapper;
import com.aiot.aiotbackstage.mapper.SysSensorRecStatisMapper;
import com.aiot.aiotbackstage.model.entity.SysSensorRecStatisEntity;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.model.vo.SysSensorRecVo;
import com.aiot.aiotbackstage.service.ISensorRecStatisService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
     * @return
     */
    @Override
    public Object getPestMeteInfo(String siteId, String startDate, String endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return getDayOrNightAverage(siteId, startDate, endDate);
    }


    /**
     * 最*虫害气象信息
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageIndex 页码
     * @param pageSize  分页大小
     * @return
     */
    @Override
    public PageResult<SysSensorRecStatisEntity> getMaxOrMinPestMeteInfo(String siteId, String startDate, String endDate, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
//        List<Map<String, Object>> pestResult = sysInsectRecStatisMapper.findMaxOrMinPestDate(params);

//        if (pestResult != null && pestResult.size() == 1) {
//            String pestDate = String.valueOf(pestResult.get(0).get("date"));
            return getDayOrNightAveragePageable(siteId, startDate, endDate, pageIndex, pageSize);
//        }
//        return null;
    }

    private Map<String, Object> getDayOrNightAverage(String siteId, String startDate, String endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        Integer integer = sysSensorRecStatisMapper.selectCount(Wrappers.<SysSensorRecStatisEntity>lambdaQuery()
                .eq(SysSensorRecStatisEntity::getSiteId, siteId)
                .between(true, SysSensorRecStatisEntity::getDate, startDate, endDate));
        params.put("pageIndex", 0);
        params.put("pageSize", integer);
        List<SysSensorRecStatisEntity> result = sysSensorRecStatisMapper.findAllBySiteIdDaily(params);

        List<SysSensorRecStatisEntity> tempList;

        Map<String, Object> map = new HashMap<>();

        String[] date = new String[result.size()];
        double[] windSpeed = new double[result.size()];
        double[] windDirection = new double[result.size()];
        double[] humidityDay = new double[result.size()];
        double[] humidityNight = new double[result.size()];
        double[] temperatureDay = new double[result.size()];
        double[] temperatureNight = new double[result.size()];
        double[] noisy = new double[result.size()];
        double[] PM10 = new double[result.size()];
        double[] PM25 = new double[result.size()];
        double[] atmos = new double[result.size()];

        map.put("date", date);
        map.put("windSpeed", windSpeed);
        map.put("windDirection", windDirection);
        map.put("humidityDay", humidityDay);
        map.put("humidityNight", humidityNight);
        map.put("temperatureDay", temperatureDay);
        map.put("temperatureNight", temperatureNight);
        map.put("noisy", noisy);
        map.put("PM10", PM10);
        map.put("PM25", PM25);
        map.put("atmos", atmos);

        for (int i = 0; i < result.size(); i++) {
            SysSensorRecStatisEntity entity = result.get(i);

            params.clear();
            params.put("siteId", siteId);
            params.put("startDate", entity.getDate());
            params.put("endDate", entity.getDate());
            Integer counts = sysSensorRecStatisMapper.selectCount(Wrappers.<SysSensorRecStatisEntity>lambdaQuery()
                    .eq(SysSensorRecStatisEntity::getSiteId, siteId)
                    .between(true, SysSensorRecStatisEntity::getDate, entity.getDate(), entity.getDate()));
            params.put("pageIndex", 0);
            params.put("pageSize", counts);

            date[i] = entity.getDate();
            windSpeed[i] = entity.getWindSpeed();
            windDirection[i] = entity.getWindDirection();
            noisy[i] = entity.getHumidity();
            PM10[i] = entity.getHumidity();
            PM25[i] = entity.getHumidity();
            atmos[i] = entity.getHumidity();

            tempList = sysSensorRecStatisMapper.findAllBySiteIdDailyDay(params);
            if (!tempList.isEmpty()) {
                humidityDay[i] = tempList.get(0).getHumidity();
                temperatureDay[i] = tempList.get(0).getTemperature();
            }

            tempList = sysSensorRecStatisMapper.findAllBySiteIdDailyNight(params);
            if (!tempList.isEmpty()) {
                humidityNight[i] = tempList.get(0).getHumidity();
                temperatureNight[i] = tempList.get(0).getTemperature();
            }
        }
        return map;
    }

    private PageResult<SysSensorRecStatisEntity> getDayOrNightAveragePageable(String siteId, String startDate, String endDate, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        int total = sysSensorRecStatisMapper.countAllBySiteIdDaily(params);
        params.put("pageIndex", (pageIndex-1)*pageSize);
        params.put("pageSize", pageSize);
        List<SysSensorRecStatisEntity> result = sysSensorRecStatisMapper.findAllBySiteIdDaily(params);

        List<SysSensorRecStatisEntity> tempList;
        for (SysSensorRecStatisEntity entity : result) {
            params.clear();
            params.put("siteId", siteId);
            params.put("startDate", entity.getDate());
            params.put("endDate", entity.getDate());
            params.put("pageIndex", 0);
            params.put("pageSize", total);
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
