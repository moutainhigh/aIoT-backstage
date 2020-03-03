package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysSeedlingGrowthEntity;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface ISysSeedlingGrowthService extends IService<SysSeedlingGrowthEntity> {

    PageResult<SysSeedlingGrowthEntity> getAll(String startDate, String endDate, int pageIndex, int pageSize);

    Map<String, Object> getStat(String startDate, String endDate);
}
