package com.aiot.aiotbackstage.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/3/3 15:00
 */
@Data
public class EarlyInfoVo {

    private String coordinate;

    private Date time;

    private String earlyName;

    private String earlyContent;

    private String masures;

    private String reportUser;
}
