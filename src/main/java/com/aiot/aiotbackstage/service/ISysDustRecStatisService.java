package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysDustRecStatisEntity;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ISysDustRecStatisService extends IService<SysDustRecStatisEntity> {

    PageResult<SysDustRecStatisEntity> getPestSoilInfo(String siteId, String startDate, String endDate, int pageIndex, int pageSize);

    PageResult<SysDustRecStatisEntity> getMaxOrMinPestSoilInfo(String siteId, String startDate, String endDate, int isMax, int pageIndex, int pageSize);
}