package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.SysSeedlingGrowthMapper;
import com.aiot.aiotbackstage.model.entity.SysSeedlingGrowthEntity;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.service.ISysSeedlingGrowthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
