package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.SysInsectRecStatisMapper;
import com.aiot.aiotbackstage.model.entity.SysInsectRecStatisEntity;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.service.ISysInsectRecStatisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysInsectRecStatisServiceImpl extends ServiceImpl<SysInsectRecStatisMapper, SysInsectRecStatisEntity>
        implements ISysInsectRecStatisService {

    @Autowired
    private SysInsectRecStatisMapper sysInsectRecStatisMapper;

    @Override
    public List<Map<String, Object>> getAllSitesPestNumStat(String startDate, String endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return sysInsectRecStatisMapper.findAllGroupBySiteId(params);
    }

    @Override
    public PageResult<Map<String, Object>> getSomeSitePestNumStat(String siteId, String startDate, String endDate, int pageSize, int pageIndex) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        int total = sysInsectRecStatisMapper.countAllBySiteId(params);
        params.put("pageSize", pageSize);
        params.put("pageIndex", pageIndex);
        List<Map<String, Object>> result = sysInsectRecStatisMapper.findAllBySiteId(params);
        return PageResult.<Map<String, Object>>builder()
                .pageData(result)
                .total(total)
                .pageSize(pageSize)
                .pageNumber(pageIndex)
                .build();
    }

    @Override
    public Map<String, List<String>> getPestNumStat(String startDate, String endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        List<Map<String, Object>> result = sysInsectRecStatisMapper.findAllGroupByInsectId(params);

        Map<String, List<String>> map = new HashMap<>();
        List<String> tempList;
        for (Map<String, Object> item : result) {
            if (!map.containsKey("name")) {
                tempList = new ArrayList<>();
                map.put("name", tempList);
            } else {
                tempList = map.get("name");
            }
            tempList.add(String.valueOf(item.get("name")));

            if (!map.containsKey("data")) {
                tempList = new ArrayList<>();
                map.put("data", tempList);
            } else {
                tempList = map.get("data");
            }
            tempList.add(String.valueOf(item.get("value")));
        }
        return map;
    }

    public Object getAllPestNumStat() {
        return sysInsectRecStatisMapper.findAllGroupByInsectId(null);
    }
}
