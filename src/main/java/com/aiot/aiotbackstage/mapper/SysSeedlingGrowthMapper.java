package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysSeedlingGrowthEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysSeedlingGrowthMapper extends BaseMapper<SysSeedlingGrowthEntity> {

    List<SysSeedlingGrowthEntity> seedingGrowthPage(@Param("page") PageParam page);

    int countAll(Map<String, Object> params);

    List<SysSeedlingGrowthEntity> findAll(Map<String, Object> params);
}