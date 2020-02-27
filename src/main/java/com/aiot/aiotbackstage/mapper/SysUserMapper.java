package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysUserEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUserEntity> {
    List<SysUserEntity> selectAll();

    List<SysUserEntity> userPageInfo(@Param("page") PageParam page);
}