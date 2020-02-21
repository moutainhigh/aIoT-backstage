package com.aiot.aiotbackstage.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysSensorRecVo2 implements Serializable {

    private Double windSpeed;

    private Double windDirection;

    private Double humidity;

    private Double temperature;

    private Double noisy;

    private Double PM10;

    private Double PM25;

    private Double atmos;

    private String date;

    private Double humidityNightly;

    private Double temperatureNightly;
}
