package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.SysSeedlingGrowthMapper;
import com.aiot.aiotbackstage.model.entity.SysSeedlingGrowthEntity;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.service.ISysSeedlingGrowthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysSeedlingGrowthServiceImpl extends ServiceImpl<SysSeedlingGrowthMapper, SysSeedlingGrowthEntity> implements ISysSeedlingGrowthService {

    @Override
    public PageResult<SysSeedlingGrowthEntity> getAll(String startDate, String endDate, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        int total = baseMapper.countAll(params);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        List<SysSeedlingGrowthEntity> all = baseMapper.findAll(params);
        return PageResult.<SysSeedlingGrowthEntity>builder().total(total).pageSize(pageSize).pageNumber(pageIndex).pageData(all).build();
    }

    @Override
    public Map<String, Object> getStat(String startDate, String endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        List<Map<String, Object>> result = baseMapper.sumByLevel(params);
        if(CollectionUtils.isEmpty(result)){
            return null;
        }
        Object[] x = new Object[4];
        Object[] y = new Object[4];
        if (result.size() == 1) {
            x[0] = "优质";
            x[1] = "正常";
            x[2] = "亚健康";
            x[3] = "不健康";
            if(result.get(0) == null){
                return null;
            }
            y[0] = result.get(0).get("good") == null ? 0 : result.get(0).get("good");
            y[1] = result.get(0).get("normal") == null ? 0 : result.get(0).get("normal");
            y[2] = result.get(0).get("sub_health") == null ? 0 : result.get(0).get("sub_health");
            y[3] = result.get(0).get("unhealthy") == null ? 0 : result.get(0).get("unhealthy");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("x", x);
        map.put("y", y);
        return map;
    }
}
