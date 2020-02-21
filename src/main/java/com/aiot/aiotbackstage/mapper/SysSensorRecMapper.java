package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysSensorRecEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface SysSensorRecMapper extends BaseMapper<SysSensorRecEntity> {

    List<Map<String, Object>> findAllAverageByDay(Map<String, Object> params);
}
