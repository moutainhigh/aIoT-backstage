package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.*;
import com.aiot.aiotbackstage.model.entity.SysDisasterSituationEntity;
import com.aiot.aiotbackstage.model.entity.SysDustRecStatisEntity;
import com.aiot.aiotbackstage.model.entity.SysInsectRecStatisEntity;
import com.aiot.aiotbackstage.service.ISysDustRecStatisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.http.impl.cookie.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysDustRecStatisServiceImpl extends ServiceImpl<SysDustRecStatisMapper, SysDustRecStatisEntity> implements ISysDustRecStatisService {

    @Autowired
    private SysDustRecStatisMapper sysDustRecStatisMapper;

    @Autowired
    private SysInsectRecStatisMapper sysInsectRecStatisMapper;

    /**
     * 获取每天土壤信息平均值
     *
     * @param siteId    站点ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    @Override
    public List<SysDustRecStatisEntity> getPestSoilInfo(String siteId, long startTime, long endTime) {
        Date startDate = new Date(startTime);
        Date endDate = new Date(endTime);

        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startTime", DateUtils.formatDate(startDate, "yyyy-MM-dd HH:mm:ss"));
        params.put("endTime", DateUtils.formatDate(endDate, "yyyy-MM-dd HH:mm:ss"));

        // 查询当天每小时平均值
        if (DateUtils.formatDate(startDate, "yyyy-MM-dd")
                .equals(DateUtils.formatDate(startDate, "yyyy-MM-dd"))) {
            return sysDustRecStatisMapper.findAllBySiteId(params);
        }
        return sysDustRecStatisMapper.findAllDaily(params);
    }

    /**
     * 最大虫害土壤信息
     *
     * @param siteId    站点ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param isMax     1：最大，0：最小
     * @return
     */
    @Override
    public List<SysDustRecStatisEntity> getMaxOrMinPestSoilInfo(String siteId, long startTime, long endTime, int isMax) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startTime", DateUtils.formatDate(new Date(startTime), "yyyy-MM-dd"));
        params.put("endTime", DateUtils.formatDate(new Date(endTime), "yyyy-MM-dd"));
        params.put("isMax", isMax);
        // 获取虫害最大日期
        List<Map<String, Object>> pestResult = sysInsectRecStatisMapper.findMaxOrMinPestDate(params);
        if (pestResult != null && pestResult.size() == 1) {
            String pestDate = String.valueOf(pestResult.get(0).get("date"));
            params.clear();
            params.put("siteId", siteId);
            params.put("startTime", pestDate);
            params.put("endTime", pestDate);
            return sysDustRecStatisMapper.findAllDaily(params);
        }
        return null;
    }
}
