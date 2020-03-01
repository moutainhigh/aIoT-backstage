package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysSensorRecEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface SysSensorRecMapper extends BaseMapper<SysSensorRecEntity> {

    /**
     * 每天气候平均值
     *
     * @return
     * @param params
     */
    List<Map<String, Object>> findAllAverageHourly(Map<String, Object> params);

    List<SysSensorRecEntity> findByTimeGroupBySensor(String siteId, String time);
}
