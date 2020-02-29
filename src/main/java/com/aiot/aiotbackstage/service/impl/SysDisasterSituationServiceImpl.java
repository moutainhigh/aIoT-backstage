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
}
