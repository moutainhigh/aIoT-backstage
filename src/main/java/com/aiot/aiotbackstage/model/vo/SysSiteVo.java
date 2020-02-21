package com.aiot.aiotbackstage.model.vo;

import com.aiot.aiotbackstage.model.entity.SysSiteEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 站点附加信息返回对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysSiteVo extends SysSiteEntity implements Serializable {

    /**
     * 害虫数量
     */
    private Integer insectNumber;
}
