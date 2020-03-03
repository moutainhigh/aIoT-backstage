package com.aiot.aiotbackstage.model.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/29 16:17
 */
@Data
public class DeviceInfoNewParam extends PageParam{

    private Integer dimension;

    private Integer siteId;

    private Integer deviceType;
}
