package com.aiot.aiotbackstage.model.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/29 16:17
 */
@Data
public class DeviceInfoOldParam extends PageParam{

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

}
