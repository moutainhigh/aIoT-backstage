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
    public List<SysDustRecStatisEntity> getPestSoilInfo(String siteId, long startDate, long endDate) {
        Date sDate = new Date(startDate);
        Date eDate = new Date(endDate);

        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", DateUtils.formatDate(sDate, "yyyy-MM-dd"));
        params.put("endDate", DateUtils.formatDate(eDate, "yyyy-MM-dd"));

        // 查询当天每小时平均值
        if (DateUtils.formatDate(sDate, "yyyy-MM-dd")
                .equals(DateUtils.formatDate(eDate, "yyyy-MM-dd"))) {
            return sysDustRecStatisMapper.findAllBySiteId(params);
        }
        return sysDustRecStatisMapper.findAllDaily(params);
    }

    /**
     * 最*虫害土壤信息
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param isMax     1：最大，0：最小
     * @return
     */
    @Override
    public List<SysDustRecStatisEntity> getMaxOrMinPestSoilInfo(String siteId, long startDate, long endDate, int isMax) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", DateUtils.formatDate(new Date(startDate), "yyyy-MM-dd"));
        params.put("endDate", DateUtils.formatDate(new Date(endDate), "yyyy-MM-dd"));
        params.put("isMax", isMax);
        // 获取虫害最大日期
        List<Map<String, Object>> pestResult = sysInsectRecStatisMapper.findMaxOrMinPestDate(params);
        if (pestResult != null && pestResult.size() == 1) {
            String pestDate = String.valueOf(pestResult.get(0).get("date"));
            params.clear();
            params.put("siteId", siteId);
            params.put("startDate", pestDate);
            params.put("endDate", pestDate);
            return sysDustRecStatisMapper.findAllDaily(params);
        }
        return null;
    }
}
