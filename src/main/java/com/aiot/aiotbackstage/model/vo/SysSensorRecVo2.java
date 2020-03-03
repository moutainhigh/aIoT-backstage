package com.aiot.aiotbackstage.model.vo;

import com.aiot.aiotbackstage.model.entity.SysSensorRecEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysSensorRecVo2 extends SysSensorRecEntity implements Serializable {

    private String siteName;
}
