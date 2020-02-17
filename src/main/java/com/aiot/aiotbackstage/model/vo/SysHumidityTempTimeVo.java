package com.aiot.aiotbackstage.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SysHumidityTempTimeVo {

    private String time;

    private BigDecimal var;
}
