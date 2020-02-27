package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysSensorRecStatisEntity;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ISensorRecStatisService extends IService<SysSensorRecStatisEntity> {

    List<SysSensorRecStatisEntity> getPestMeteInfo(String siteId, String startDate, String endDate);

    PageResult<SysSensorRecStatisEntity> getMaxOrMinPestMeteInfo(String siteId, String startDate, String endDate, int isMax, int pageIndex, int pageSize);
}
