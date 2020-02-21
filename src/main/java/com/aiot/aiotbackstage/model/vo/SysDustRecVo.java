package com.aiot.aiotbackstage.model.vo;

import com.aiot.aiotbackstage.model.entity.SysDustRecEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysDustRecVo extends SysDustRecEntity implements Serializable {

    /**
     * 日期（不包含时间）
     */
    private String date;
}
