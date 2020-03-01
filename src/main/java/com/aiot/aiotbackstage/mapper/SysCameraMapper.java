package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysCameraEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SysCameraMapper extends BaseMapper<SysCameraEntity> {

    List<SysCameraEntity> selectAll();
}
