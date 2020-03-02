package com.aiot.aiotbackstage.model.vo;

import lombok.Data;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/3/1 21:57
 */
@Data
public class DeviceVo {
    private String RTU;
    private String CAMERA;
    private String InsectDevice;

    private String statusRTU;
    private String statusCAMERA;
    private String statusInsectDevice;

    private String deviceNameRTU;
    private String deviceNameCAMERA;
    private String deviceNameInsectDevice;
}
