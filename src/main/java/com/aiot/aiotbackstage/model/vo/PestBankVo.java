package com.aiot.aiotbackstage.model.vo;

import com.aiot.aiotbackstage.model.entity.SysPestBankEntity;
import lombok.Data;

import java.util.List;

@Data
public class PestBankVo{

    private List<SysPestBankEntity> pestBankEntities;

    /**
     * 总条数
     */
    private Integer total;
}