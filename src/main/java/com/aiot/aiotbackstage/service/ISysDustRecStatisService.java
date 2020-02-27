package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysDustRecStatisEntity;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ISysDustRecStatisService extends IService<SysDustRecStatisEntity> {

    List<SysDustRecStatisEntity> getPestSoilInfo(String siteId, String startDate, String endDate);

    PageResult<SysDustRecStatisEntity> getMaxOrMinPestSoilInfo(String siteId, String startDate, String endDate, int isMax, int pageIndex, int pageSize);
}