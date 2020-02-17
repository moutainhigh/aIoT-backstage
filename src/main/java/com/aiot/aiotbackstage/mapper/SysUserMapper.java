package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUserEntity> {
    List<SysUserEntity> selectAll();
}