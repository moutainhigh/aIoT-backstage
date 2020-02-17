package com.aiot.aiotbackstage.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class SysHumidityRegionVo {

    /**
     * 区域
     */
    private String region;

    /**
     * 时间和湿度值
     */
    private List<SysHumidityTempTimeVo> humidityTempTimes;
}
