package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysSiteEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SysSiteMapper extends BaseMapper<SysSiteEntity> {

    List<SysSiteEntity> selectAll();
}
