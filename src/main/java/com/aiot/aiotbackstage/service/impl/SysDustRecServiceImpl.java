package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.SysDustRecMapper;
import com.aiot.aiotbackstage.model.entity.SysDustRecEntity;
import com.aiot.aiotbackstage.service.ISysDustRecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysDustRecServiceImpl extends ServiceImpl<SysDustRecMapper, SysDustRecEntity>
        implements ISysDustRecService {

    @Override
    public Map<Integer, Object> getStatByTime(String siteId, String time) {
        List<SysDustRecEntity> result = baseMapper.findByTimeGroupByDepth(siteId, time);

        Map<Integer, Object> map = new HashMap<>();

        Map<String, Object> tempMap;
        for (int i = 0; i < result.size(); i++) {
            SysDustRecEntity item = result.get(i);

            String[] x = new String[6];
            Double[] y = new Double[6];

            x[0] = "含水率";
            x[1] = "温度";
            x[2] = "导电率";
            x[3] = "盐度";
            x[4] = "总溶解固体";
            x[5] = "介电常数";

            y[0] = item.getWc();
            y[1] = item.getTemperature();
            y[2] = item.getEc();
            y[3] = item.getSalinity();
            y[4] = item.getTds();
            y[5] = item.getEpsilon();

            tempMap = new HashMap<>();
            tempMap.put("x", x);
            tempMap.put("y", y);

            map.put(item.getDepth(), tempMap);
        }
        return map;
    }
}
