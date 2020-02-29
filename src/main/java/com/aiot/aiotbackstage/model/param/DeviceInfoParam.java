package com.aiot.aiotbackstage.model.param;

import lombok.Data;

import java.util.Date;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/29 14:31
 */
@Data
public class DeviceInfoParam {

    private Integer id;

    private Integer siteId;

    /**
     * {@link com.aiot.aiotbackstage.common.enums.DeviceType}
     */
    private String deviceType;

//    private String subType;

    private Date startTime;

    private Date endTime;

}
