package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.SysInsectRecStatisMapper;
import com.aiot.aiotbackstage.mapper.SysPestBankMapper;
import com.aiot.aiotbackstage.mapper.SysPreventiveMeasuresMapper;
import com.aiot.aiotbackstage.mapper.SysWarnInfoMapper;
import com.aiot.aiotbackstage.model.entity.SysInsectRecStatisEntity;
import com.aiot.aiotbackstage.model.entity.SysPestBankEntity;
import com.aiot.aiotbackstage.model.entity.SysPreventiveMeasuresEntity;
import com.aiot.aiotbackstage.model.entity.SysWarnInfoEntity;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.service.ISysInsectRecStatisService;
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

    @Override
    public PageResult<Map<String, Object>> getSomeSitePestNumStat(String siteId, String startDate, String endDate, int pageSize, int pageIndex,int i) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        int total = sysInsectRecStatisMapper.countAllBySiteId(params);
        params.put("pageSize", pageSize);
        params.put("pageIndex", pageIndex);
        List<Map<String, Object>> result = sysInsectRecStatisMapper.findAllBySiteId(params);
        if(result == null){
            return null;
        }
        if(i == 1){
            List<Long> insectId = (List<Long>)(List)result.stream().map(stringObjectMap -> stringObjectMap.get("insectId")).collect(Collectors.toList());
            List<Long> insectName = (List<Long>)(List)result.stream().map(stringObjectMap -> stringObjectMap.get("insectName")).collect(Collectors.toList());
            List<SysPestBankEntity> sysPestBankEntities = pestBankMapper.selectBatchIds(insectId);
            List<SysPreventiveMeasuresEntity> sysPreventiveMeasuresEntities =
                    preventiveMeasuresMapper.selectList(Wrappers.<SysPreventiveMeasuresEntity>lambdaQuery()
                            .in(SysPreventiveMeasuresEntity::getInsectInfoId, insectId));
            List<SysWarnInfoEntity> warnInfoEntities = warnInfoMapper.selectList(Wrappers.<SysWarnInfoEntity>lambdaQuery()
                    .in(SysWarnInfoEntity::getEarlyName, insectName).eq(SysWarnInfoEntity::getTime,startDate));
            result.stream().forEach(stringObjectMap -> {
                sysPestBankEntities.forEach(sysPestBankEntity -> {
                    if(Long.valueOf(stringObjectMap.get("insectId")+"").equals(sysPestBankEntity.getId())){
                        stringObjectMap.put("pestIntroduce",sysPestBankEntity.getPestIntroduce());
                    }
                });
                sysPreventiveMeasuresEntities.forEach(measuresEntity -> {
                    if(Long.valueOf(stringObjectMap.get("insectId")+"").equals(measuresEntity.getInsectInfoId())){
                        stringObjectMap.put("picture",measuresEntity.getPicture());
//                        stringObjectMap.put("measuresInfo",measuresEntity.getMeasuresInfo());
                    }
                });
                if(warnInfoEntities.size()>0){
                    stringObjectMap.put("reportUser",warnInfoEntities.get(0).getReportUser());
                }else{
                    stringObjectMap.put("reportUser","user");
                }

            });
        }

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
