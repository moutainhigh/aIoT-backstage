package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysPreventiveMeasuresEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SysPreventiveMeasuresMapper extends BaseMapper<SysPreventiveMeasuresEntity> {

    List<SysPreventiveMeasuresEntity> selectAll();
}