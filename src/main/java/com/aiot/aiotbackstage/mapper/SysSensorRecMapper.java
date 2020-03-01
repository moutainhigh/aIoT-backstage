package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysSensorRecEntity;
import com.aiot.aiotbackstage.model.vo.SysSensorRecVo2;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface SysSensorRecMapper extends BaseMapper<SysSensorRecEntity> {

    /**
     * 每天气候平均值
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> findAllAverageHourly(Map<String, Object> params);

    List<SysSensorRecVo2> findByTimeGroupBySensor(String time);
}
