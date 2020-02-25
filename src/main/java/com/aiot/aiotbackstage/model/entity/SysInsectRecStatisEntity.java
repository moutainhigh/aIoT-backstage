package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_insect_rec_statis")
public class SysInsectRecStatisEntity {

    private Integer id;

    private String deviceId;

    private String insectId;

    private String date;

    private Integer hour;

    private Integer num;

    private String updateTime;

    private String createTime;
}
