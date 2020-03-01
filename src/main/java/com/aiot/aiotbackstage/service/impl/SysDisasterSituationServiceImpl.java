package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.SysDisasterSituationMapper;
import com.aiot.aiotbackstage.model.entity.SysDisasterSituationEntity;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.model.vo.SysDisasterSituationVo;
import com.aiot.aiotbackstage.service.ISysDisasterSituationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysDisasterSituationServiceImpl extends ServiceImpl<SysDisasterSituationMapper, SysDisasterSituationEntity>
        implements ISysDisasterSituationService {

    public PageResult<SysDisasterSituationVo> getAll(String startDate, String endDate, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        int total = baseMapper.countAll(params);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        List<SysDisasterSituationVo> all = baseMapper.findAll(params);
        return PageResult.<SysDisasterSituationVo>builder().total(total).pageNumber(pageIndex).pageSize(pageSize).pageData(all).build();
    }

    @Override
    public Map<String, Object> getStat(String startDate, String endDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        List<Map<String, Object>> result = baseMapper.sumByLevel(params);

        Object[] x = new Object[3];
        Object[] y = new Object[3];
        if (result.size() == 1) {
            x[0] = "serious";
            x[1] = "medium";
            x[2] = "normal";

            y[0] = result.get(0).get("serious");
            y[1] = result.get(0).get("medium");
            y[2] = result.get(0).get("normal");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("x", x);
        map.put("y", y);
        return map;
    }
}
