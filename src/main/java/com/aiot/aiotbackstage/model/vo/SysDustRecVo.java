package com.aiot.aiotbackstage.model.vo;

import com.aiot.aiotbackstage.model.entity.SysDustRecEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysDustRecVo extends SysDustRecEntity implements Serializable {

    private String siteName;
}
