package com.aiot.aiotbackstage.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.aiot.aiotbackstage.model.entity.SysUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

public interface SysUserMapper extends BaseMapper<SysUserEntity> {


    List<SysUserEntity> selectAll();


}