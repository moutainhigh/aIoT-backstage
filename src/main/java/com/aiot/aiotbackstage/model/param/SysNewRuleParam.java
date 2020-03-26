package com.aiot.aiotbackstage.model.param;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysNewRuleParam implements Serializable {
    public Integer id;

    /**
     * 农作物名称
     */
    public String crops;

    /**
     * 生长周期
     */
    public String growthcycle;

    /**
     * 风速
     */
    public String windSpeed;

    /**
     * 风向
     */
    public String windDirection;

    /**
     * 湿度
     */
    public String humidity;

    /**
     * 温度
     */
    public String temperature;

    /**
     * 噪音
     */
    public String noise;

    /**
     * pm25
     */
    public String pm25;

    /**
     * pm10
     */
    public String pm10;

    /**
     * 大气压
     */
    public String atmos;

    /**
     * 含水率
     */
    public String wcDust;

    /**
     * 土壤温度
     */
    public String temperatureDust;

    /**
     * 导电率
     */
    public String ecDust;

    /**
     * 土壤盐度
     */
    public String salinityDust;

    /**
     * 总溶解固体
     */
    public String tdsDust;

    /**
     * 介电常数
     */
    public String epsilonDust;

    /**
     * 预警信息
     */
    public String warn;

    /**
     * 防治措施
     */
    public String control;

    public static final long serialVersionUID = 1L;
}