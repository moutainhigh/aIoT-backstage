package com.aiot.aiotbackstage.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/17 17:16
 */
@Data
public class SysSensorRecVo {

    private Date  time;

    private  String  humidity;

    private  String  temperature;

    private  String  noisy;

    private  String  PM10;

    private  String  PM25;

    private  String  atmos;
}
