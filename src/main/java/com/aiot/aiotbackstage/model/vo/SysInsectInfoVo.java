package com.aiot.aiotbackstage.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysInsectInfoVo implements Serializable {

    private String insectId;

    private String insectName;

    private Integer insectNumber;

    private String cropName;

    private String measuresInfo;

}
