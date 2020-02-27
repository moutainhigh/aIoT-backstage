package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysWarnRuleEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysWarnRuleMapper extends BaseMapper<SysWarnRuleEntity> {

    List<SysWarnRuleEntity> warnRulePage(@Param("earlyType") String earlyType, @Param("pageParam") PageParam pageParam);

}