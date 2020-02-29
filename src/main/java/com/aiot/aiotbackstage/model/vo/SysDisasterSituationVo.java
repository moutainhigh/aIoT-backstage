package com.aiot.aiotbackstage.model.vo;

import com.aiot.aiotbackstage.model.entity.SysDisasterSituationEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysDisasterSituationVo extends SysDisasterSituationEntity implements Serializable {

    private String level;
}
