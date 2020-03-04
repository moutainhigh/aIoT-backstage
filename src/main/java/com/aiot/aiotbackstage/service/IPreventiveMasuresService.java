package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.mapper.SysInsectInfoMapper;
import com.aiot.aiotbackstage.model.entity.SysInsectInfoEntity;
import com.aiot.aiotbackstage.model.entity.SysPreventiveMeasuresEntity;

import java.util.List;

public interface IPreventiveMasuresService {

    List<SysPreventiveMeasuresEntity> getMasuresInfo();

    List<SysPreventiveMeasuresEntity> getMasuresInfoByName(String name);

    SysPreventiveMeasuresEntity preventiveDetail(String preventiveId, String insectId);

    void preventiveAdd(SysPreventiveMeasuresEntity measuresEntity);

    void preventiveUpdate(SysPreventiveMeasuresEntity measuresEntity);

    void preventiveDelete(Long id);

    List<SysInsectInfoEntity> insect();
}
