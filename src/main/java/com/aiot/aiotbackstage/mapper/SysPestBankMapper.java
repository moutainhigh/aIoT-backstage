package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysPestBankEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysPestBankMapper extends BaseMapper<SysPestBankEntity> {

    List<SysPestBankEntity> pestBankInfoByNamePage(@Param("pestName") String pestName, @Param("page") PageParam page);
}