package com.aiot.aiotbackstage.model.vo;

import com.aiot.aiotbackstage.model.entity.SysDustRecEntity;
import com.aiot.aiotbackstage.model.entity.SysInsectRecEntity;
import com.aiot.aiotbackstage.model.entity.SysSensorRecEntity;
import lombok.Data;

import java.util.List;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/20 15:36
 */
@Data
public class SensorInfoVo {

    private List<SysSensorRecEntity> sensorRecVos;

    private List<SysDustRecEntity> dustRecVos;

    private List<SysInsectRecEntity> insectRecVos;
}
