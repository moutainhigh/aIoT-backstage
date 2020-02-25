package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 风速/风向/百叶箱传感器数值记录实体
 *
 * @author Avernus
 */
@Data
@TableName("sys_sensor_rec_statis")
@Accessors(chain = true)
public class SysSensorRecStatisEntity {

    private String id;

    private String siteId;

    private String date;

    private Integer hour;

    private Double windSpeed;

    private Double windDirection;

    private Double humidity;

    private Double temperature;

    private Double noisy;

    private Double PM10;

    private Double PM25;

    private Double atmos;

    private String updateTime;

    private String createTime;

    private Double humidityDay;

    private Double temperatureDay;

    private Double humidityNight;

    private Double temperatureNight;
}
