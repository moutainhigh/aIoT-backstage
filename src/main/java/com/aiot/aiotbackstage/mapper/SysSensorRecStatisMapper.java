package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysSensorRecStatisEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysSensorRecStatisMapper extends BaseMapper<SysSensorRecStatisEntity> {

    int batchInsert(@Param("data") List<SysSensorRecStatisEntity> data);

    List<SysSensorRecStatisEntity> findAllBySiteId(Map<String, Object> params);
}
