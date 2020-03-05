package com.aiot.aiotbackstage.model.vo;

import com.aiot.aiotbackstage.model.entity.SysInsectRecEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/20 15:36
 */
@Data
public class SensorInfoVo {

    private List<Map<String,String>> sensorRecVos;

    private List<Map<String,Object>> dustRecVos;

    private List<SysInsectRecEntity> insectRecVos;
}
