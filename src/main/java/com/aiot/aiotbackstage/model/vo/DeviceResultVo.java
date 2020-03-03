package com.aiot.aiotbackstage.model.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/3/3 9:49
 */
@Data
public class DeviceResultVo {

    private String siteName;
    private Integer siteId;
    private List<DeviceVo> deviceVo;

}
