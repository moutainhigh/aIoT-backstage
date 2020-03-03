package com.aiot.aiotbackstage.model.vo;

import com.aiot.aiotbackstage.model.entity.SysDustRecEntity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
public class SysDustRecVo extends SysDustRecEntity implements Serializable {

    private String siteName;
    public SysDustRecVo(Integer id, Integer siteId, Integer depth, Double wc, Double temperature, Double ec, Double salinity, Double tds, Double epsilon, Date time) {
        super(id, siteId, depth, wc, temperature, ec, salinity, tds, epsilon, time);
    }
}
