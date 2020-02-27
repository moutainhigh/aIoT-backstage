package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysWarnInfoEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysWarnInfoMapper extends BaseMapper<SysWarnInfoEntity> {

    List<SysWarnInfoEntity> warnInfoPage(@Param("pageParam") PageParam pageParam);
}