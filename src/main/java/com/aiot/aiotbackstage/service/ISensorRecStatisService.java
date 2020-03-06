package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysSensorRecStatisEntity;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.model.vo.SysSensorRecVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ISensorRecStatisService extends IService<SysSensorRecStatisEntity> {

    Object getPestMeteInfo(String siteId, String startDate, String endDate);

    PageResult<SysSensorRecStatisEntity> getMaxOrMinPestMeteInfo(String siteId, String startDate, String endDate, int pageIndex, int pageSize);
}
