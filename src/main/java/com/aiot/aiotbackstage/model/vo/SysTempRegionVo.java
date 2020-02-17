package com.aiot.aiotbackstage.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class SysTempRegionVo {

    /**
     * 区域
     */
    private String region;

    /**
     * 时间和温度值
     */
    private List<SysHumidityTempTimeVo> humidityTempTimes;
}
