package com.aiot.aiotbackstage.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/3/1 21:57
 */
@Data
public class DeviceVo {


//    private String RTU;
//    private String CAMERA;
    private Integer id;
    private String deviceType;
    private String deviceName;
    private Date startTime;
    private String status;
    private Integer isUpdate;
//    private String statusCAMERA;
//    private String statusInsectDevice;
//
//    private String deviceNameRTU;
//    private String deviceNameCAMERA;
//    private String deviceNameInsectDevice;
}
