package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.SysDustRecMapper;
import com.aiot.aiotbackstage.model.entity.SysDustRecEntity;
import com.aiot.aiotbackstage.model.vo.SysDustRecVo;
import com.aiot.aiotbackstage.service.ISysDustRecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysDustRecServiceImpl extends ServiceImpl<SysDustRecMapper, SysDustRecEntity>
        implements ISysDustRecService {

    @Override
    public Map<Integer, Map<String, Object>> getStatByTime(String time) {
        List<SysDustRecVo> result = baseMapper.findByTimeGroupByDepth(time);

        Map<Integer, List<SysDustRecVo>> tempMap = new HashMap<>();

        List<SysDustRecVo> tempList;
        for (SysDustRecVo item : result) {
            if (!tempMap.containsKey(item.getDepth())) {
                tempList = new ArrayList<>();
                tempMap.put(item.getDepth(), tempList);
            } else {
                tempList = tempMap.get(item.getDepth());
            }
            tempList.add(item);
        }

        Map<Integer, Map<String, Object>> map = new HashMap<>();

        Map<String, Object> tempMap2;
        for (Map.Entry<Integer, List<SysDustRecVo>> entry : tempMap.entrySet()) {
            tempMap2 = new HashMap<>();
            map.put(entry.getKey(), tempMap2);

            List<String> siteNameList = new ArrayList<>();
            List<Double> wcList = new ArrayList<>();
            List<Double> temperatureList = new ArrayList<>();
            List<Double> ecList = new ArrayList<>();
            List<Double> salinityList = new ArrayList<>();
            List<Double> tdsList = new ArrayList<>();
            List<Double> epsilonList = new ArrayList<>();

            tempMap2.put("siteName", siteNameList);
            tempMap2.put("wc", wcList);
            tempMap2.put("temperature", temperatureList);
            tempMap2.put("ec", ecList);
            tempMap2.put("salinity", salinityList);
            tempMap2.put("tds", tdsList);
            tempMap2.put("epsilon", epsilonList);

            for (SysDustRecVo item : entry.getValue()) {
                siteNameList.add(item.getSiteName());
                wcList.add(item.getWc());
                temperatureList.add(item.getTemperature());
                ecList.add(item.getEc());
                salinityList.add(item.getSalinity());
                tdsList.add(item.getTds());
                epsilonList.add(item.getEpsilon());
            }
        }

        return map;
    }
}
