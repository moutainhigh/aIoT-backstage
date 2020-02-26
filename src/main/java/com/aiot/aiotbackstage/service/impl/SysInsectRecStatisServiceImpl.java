package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.SysInsectRecStatisMapper;
import com.aiot.aiotbackstage.model.entity.SysInsectRecStatisEntity;
import com.aiot.aiotbackstage.service.ISysInsectRecStatisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.http.impl.cookie.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysInsectRecStatisServiceImpl extends ServiceImpl<SysInsectRecStatisMapper, SysInsectRecStatisEntity> implements ISysInsectRecStatisService {

    @Autowired
    private SysInsectRecStatisMapper sysInsectRecStatisMapper;

    @Override
    public List<Map<String, Object>> getAllSitesPestNumStat(long startDate, long endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", DateUtils.formatDate(new Date(startDate), "yyyy-MM-dd"));
        params.put("endDate", DateUtils.formatDate(new Date(endDate), "yyyy-MM-dd"));
        return sysInsectRecStatisMapper.findAllGroupBySiteId(params);
    }

    @Override
    public List<Map<String, Object>> getSomeSitePestNumStat(String siteId, long startDate, long endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", DateUtils.formatDate(new Date(startDate), "yyyy-MM-dd"));
        params.put("endDate", DateUtils.formatDate(new Date(endDate), "yyyy-MM-dd"));
        return sysInsectRecStatisMapper.findAllBySiteId(params);
    }
}
