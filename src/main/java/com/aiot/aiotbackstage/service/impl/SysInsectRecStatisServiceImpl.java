package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.config.ObsConfig;
import com.aiot.aiotbackstage.mapper.*;
import com.aiot.aiotbackstage.model.entity.*;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.service.ISysInsectRecStatisService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private SysPestBankMapper pestBankMapper;

    @Autowired
    private SysPreventiveMeasuresMapper preventiveMeasuresMapper;

    @Autowired
    private SysWarnInfoMapper warnInfoMapper;

    @Autowired
    private ObsConfig obsConfig;

    @Autowired
    private SysSiteMapper siteMapper;

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
    public PageResult<Map<String, Object>> getSomeSitePestNumDatail(String siteId, String startDate, String endDate, int pageSize, int pageIndex) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        params.put("pageSize", pageSize);
        params.put("pageIndex", pageIndex);
        List<SysSiteEntity> sysSiteEntities = siteMapper.selectAll();
        List<Map<String, Object>> results=new ArrayList<>();
        int total =0;
        for (SysSiteEntity sysSiteEntity : sysSiteEntities) {
            params.put("siteId", sysSiteEntity.getId());
            List<Map<String, Object>> allBySiteId = sysInsectRecStatisMapper.findAllBySiteId(params);
            if(!CollectionUtils.isEmpty(allBySiteId)){
                Map<String, Object> map = allBySiteId.get(0);
                map.put("coordinate",sysSiteEntity.getCoordinate());
                results.add(map);
            }
            total+=sysInsectRecStatisMapper.countAllBySiteId(params);
        }
        for (Map<String, Object> mapList : results) {
            SysPestBankEntity sysPestBankEntities = pestBankMapper.selectById((int)mapList.get("insectId"));
            List<SysPreventiveMeasuresEntity> sysPreventiveMeasuresEntities =
                    preventiveMeasuresMapper.selectList(Wrappers.<SysPreventiveMeasuresEntity>lambdaQuery()
                            .eq(SysPreventiveMeasuresEntity::getInsectInfoId, mapList.get("insectId")));
            List<SysWarnInfoEntity> warnInfoEntities = warnInfoMapper.selectList(Wrappers.<SysWarnInfoEntity>lambdaQuery()
                    .eq(SysWarnInfoEntity::getEarlyName, mapList.get("insectName")).eq(SysWarnInfoEntity::getTime,startDate));
            if(Long.valueOf(mapList.get("insectId")+"").equals(sysPestBankEntities.getId())) {
                mapList.put("pestIntroduce", sysPestBankEntities.getPestIntroduce());
            }
            if(Long.valueOf(mapList.get("insectId")+"").equals(sysPreventiveMeasuresEntities.get(0).getInsectInfoId())){
                mapList.put("picture",obsConfig.getUrl()+sysPreventiveMeasuresEntities.get(0).getPicture());
            }
            if(warnInfoEntities.size()>0){
                mapList.put("reportUser",warnInfoEntities.get(0).getReportUser());
            }else{
                mapList.put("reportUser","user");
            }
        }
        return PageResult.<Map<String, Object>>builder()
                .pageData(results)
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

    @Override
    public Map<String, List<String>> getPerPestNumStat() {
        List<Map<String, Object>> result = sysInsectRecStatisMapper.findAllGroupByInsectId(null);

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

    @Override
    public Map<String, List<String>> getPestNumStatDaily(String startDate, String endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        List<Map<String, Object>> result = sysInsectRecStatisMapper.findAllGroupByDate(params);

        Map<String, List<String>> map = new HashMap<>();
        List<String> tempList;
        for (Map<String, Object> item : result) {
            if (!map.containsKey("name")) {
                tempList = new ArrayList<>();
                map.put("name", tempList);
            } else {
                tempList = map.get("name");
            }
            tempList.add(String.valueOf(item.get("date")));

            if (!map.containsKey("data")) {
                tempList = new ArrayList<>();
                map.put("data", tempList);
            } else {
                tempList = map.get("data");
            }
            tempList.add(String.valueOf(item.get("num")));
        }
        return map;
    }

    public Object getPestNumStatMonthly(String year) {
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        List<Map<String, Object>> result = sysInsectRecStatisMapper.findAllGroupByDateMonth(params);

        Map<String, List<String>> map = new HashMap<>();
        List<String> tempList;
        for (Map<String, Object> item : result) {
            if (!map.containsKey("name")) {
                tempList = new ArrayList<>();
                map.put("name", tempList);
            } else {
                tempList = map.get("name");
            }
            tempList.add(String.valueOf(item.get("date")));

            if (!map.containsKey("data")) {
                tempList = new ArrayList<>();
                map.put("data", tempList);
            } else {
                tempList = map.get("data");
            }
            tempList.add(String.valueOf(item.get("num")));
        }
        return map;
    }
}
