package com.aiot.aiotbackstage.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysInsectRecStatisVo implements Serializable {

    private String siteId;

    private String siteName;

    private int num;
}
