package com.aiot.aiotbackstage.model.vo;

import lombok.Data;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/4/7 16:45
 */
@Data
public class WeatherInfoVo {
    private String date;//时间
    private String cityName;//城市名
    private String weather;//天气
    private String temperature;//气温
    private String airQuality;//pm2.5
}
