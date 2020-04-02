package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

@Data
@TableName(value = "sys_new_rule")
public class SysNewRuleEntity implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    public Integer id;

    /**
     * 农作物名称
     */
    @TableField(value = "crops")
    public String crops;

    /**
     * 生长周期
     */
    @TableField(value = "growthcycle")
    public String growthcycle;

    /**
     * 风速
     */
    @TableField(value = "wind_speed")
    public String windSpeed;

    /**
     * 风向
     */
    @TableField(value = "wind_direction")
    public String windDirection;

    /**
     * 湿度
     */
    @TableField(value = "humidity")
    public String humidity;

    /**
     * 温度
     */
    @TableField(value = "temperature")
    public String temperature;

    /**
     * 噪音
     */
    @TableField(value = "noise")
    public String noise;

    /**
     * pm25
     */
    @TableField(value = "pm25")
    public String pm25;

    /**
     * pm10
     */
    @TableField(value = "pm10")
    public String pm10;

    /**
     * 大气压
     */
    @TableField(value = "atmos")
    public String atmos;

    /**
     * 含水率
     */
    @TableField(value = "wc_dust")
    public String wcDust;

    /**
     * 土壤温度
     */
    @TableField(value = "temperature_dust")
    public String temperatureDust;

    /**
     * 导电率
     */
    @TableField(value = "ec_dust")
    public String ecDust;

    /**
     * 土壤盐度
     */
    @TableField(value = "salinity_dust")
    public String salinityDust;

    /**
     * 总溶解固体
     */
    @TableField(value = "tds_dust")
    public String tdsDust;

    /**
     * 介电常数
     */
    @TableField(value = "epsilon_dust")
    public String epsilonDust;

    /**
     * 预警信息
     */
    @TableField(value = "warn")
    public String warn;

    /**
     * 防治措施
     */
    @TableField(value = "control")
    public String control;

    /**
     * 描述
     */
    @TableField(value = "remark")
    public String remark;

    @TableField(value = "growthcycle_type")
    private String growthcycleType;

    private static final long serialVersionUID = 1L;
}