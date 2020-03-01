package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.*;
import com.aiot.aiotbackstage.model.entity.SysDustRecStatisEntity;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.service.ISysDustRecStatisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysDustRecStatisServiceImpl extends ServiceImpl<SysDustRecStatisMapper, SysDustRecStatisEntity>
        implements ISysDustRecStatisService {

    @Autowired
    private SysDustRecStatisMapper sysDustRecStatisMapper;

    @Autowired
    private SysInsectRecStatisMapper sysInsectRecStatisMapper;

    /**
     * 获取每天土壤信息平均值
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    @Override
    public Object getPestSoilInfo(String siteId, String startDate, String endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        List<SysDustRecStatisEntity> result = sysDustRecStatisMapper.findAllDaily(params);
        Map<Integer, Map<String, List<Object>>> map = new HashMap<>();

        Map<String, List<Object>> tempMap;
        for (int i = 0; i < result.size(); i++) {
            SysDustRecStatisEntity entity = result.get(i);

            if (!map.containsKey(entity.getDepth())) {
                tempMap = new HashMap<>();
                map.put(entity.getDepth(), tempMap);
            } else {
                tempMap = map.get(entity.getDepth());
            }

            List<Object> date;
            List<Object> wc;
            List<Object> temperature;
            List<Object> ec;
            List<Object> salinity;
            List<Object> tds;
            List<Object> epsilon;

            if (!tempMap.containsKey("date")) {
                date = new ArrayList<>();
                tempMap.put("date", date);
            } else {
                date = tempMap.get("date");
            }
            date.add(entity.getDate());

            if (!tempMap.containsKey("wc")) {
                wc = new ArrayList<>();
                tempMap.put("wc", wc);
            } else {
                wc = tempMap.get("wc");
            }
            wc.add(entity.getWc());

            if (!tempMap.containsKey("temperature")) {
                temperature = new ArrayList<>();
                tempMap.put("temperature", temperature);
            } else {
                temperature = tempMap.get("temperature");
            }
            temperature.add(entity.getTemperature());

            if (!tempMap.containsKey("ec")) {
                ec = new ArrayList<>();
                tempMap.put("ec", ec);
            } else {
                ec = tempMap.get("ec");
            }
            ec.add(entity.getEc());

            if (!tempMap.containsKey("salinity")) {
                salinity = new ArrayList<>();
                tempMap.put("salinity", salinity);
            } else {
                salinity = tempMap.get("salinity");
            }
            salinity.add(entity.getSalinity());

            if (!tempMap.containsKey("tds")) {
                tds = new ArrayList<>();
                tempMap.put("tds", tds);
            } else {
                tds = tempMap.get("tds");
            }
            tds.add(entity.getTds());

            if (!tempMap.containsKey("epsilon")) {
                epsilon = new ArrayList<>();
                tempMap.put("epsilon", epsilon);
            } else {
                epsilon = tempMap.get("epsilon");
            }
            epsilon.add(entity.getEpsilon());
        }
        return map;
    }

    /**
     * 最*虫害土壤信息
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
    public PageResult<SysDustRecStatisEntity> getMaxOrMinPestSoilInfo(String siteId, String startDate, String endDate, int isMax, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("isMax", isMax);
        // 获取虫害最大日期
        List<Map<String, Object>> pestResult = sysInsectRecStatisMapper.findMaxOrMinPestDate(params);
        if (pestResult != null && pestResult.size() == 1) {
            String pestDate = String.valueOf(pestResult.get(0).get("date"));
            params.clear();
            params.put("siteId", siteId);
            params.put("startDate", pestDate);
            params.put("endDate", pestDate);
            int total = sysDustRecStatisMapper.countAllDaily(params);
            params.put("pageIndex", pageIndex);
            params.put("pageSize", pageSize);
            List<SysDustRecStatisEntity> result = sysDustRecStatisMapper.findAllDaily(params);
            return PageResult.<SysDustRecStatisEntity>builder()
                    .total(total)
                    .pageData(result)
                    .pageSize(pageSize)
                    .pageNumber(pageIndex)
                    .build();
        }
        return null;
    }

    @Override
    public List<SysDustRecStatisEntity> getStatByTime(String siteId, String time) {
        List<SysDustRecStatisEntity> result = baseMapper.findByTimeGroupByDepth(siteId, time);
//
//        Map<String, Object> map = new HashMap<>();
//        Map<String, Object> tempMap;
//        for (SysDustRecStatisEntity item : result) {
//            tempMap = new HashMap<>();
//            tempMap.put("")
//            map.put(String.valueOf(item.getDepth()), tempMap);
//        }
    }
}
