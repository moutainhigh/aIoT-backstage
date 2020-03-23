package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
@TableName(value = "sys_scene")
public class SysSceneEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 城市
     */
    @TableField(value = "city")
    private String city;

    /**
     * 时间
     */
    @TableField(value = "time")
    private String time;

    /**
     * 最低温度
     */
    @TableField(value = "tmin")
    private String tmin;

    /**
     * 最高温度
     */
    @TableField(value = "tmax")
    private String tmax;

    @TableField(value = "tavg")
    private String tavg;

    /**
     * 湿度
     */
    @TableField(value = "humidity")
    private String humidity;

    /**
     * 风速
     */
    @TableField(value = "wind_speed")
    private String windSpeed;

    /**
     * 风级
     */
    @TableField(value = "wind_scale")
    private String windScale;

    /**
     * 气压
     */
    @TableField(value = "pressure")
    private String pressure;

    /**
     * 能见度
     */
    @TableField(value = "visibility")
    private String visibility;

    /**
     * 总降水量
     */
    @TableField(value = "sum_precipitation")
    private String sumPrecipitation;

    /**
     * 平均总云量
     */
    @TableField(value = "avg_total_cloud")
    private String avgTotalCloud;

    private static final long serialVersionUID = 1L;
}