package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.mapper.SysInsectInfoMapper;
import com.aiot.aiotbackstage.mapper.SysPreventiveMeasuresMapper;
import com.aiot.aiotbackstage.model.entity.SysInsectInfoEntity;
import com.aiot.aiotbackstage.model.entity.SysPreventiveMeasuresEntity;
import com.aiot.aiotbackstage.service.IPreventiveMasuresService;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PreventiveMasuresServiceImpl implements IPreventiveMasuresService {

    @Autowired
    private SysPreventiveMeasuresMapper measuresMapper;

    @Autowired
    private SysInsectInfoMapper insectInfoMapper;

    @Override
    public List<SysPreventiveMeasuresEntity> getMasuresInfo() {

        List<SysPreventiveMeasuresEntity> preventiveMeasuresEntities = measuresMapper.selectAll();
        if(CollectionUtils.isEmpty(preventiveMeasuresEntities)){
            return  null;
        }
        return  preventiveMeasuresEntities;
    }

    @Override
    public List<SysPreventiveMeasuresEntity> getMasuresInfoByName(String name) {

        List<SysPreventiveMeasuresEntity> preventiveMeasuresEntities = measuresMapper.selectList(Wrappers.<SysPreventiveMeasuresEntity>lambdaQuery()
                .like(SysPreventiveMeasuresEntity::getName, name));
        if(CollectionUtils.isEmpty(preventiveMeasuresEntities)){
             return  null;
        }
        return preventiveMeasuresEntities;
    }

    @Override
    public SysPreventiveMeasuresEntity preventiveDetail(String preventiveId, String insectId) {

        SysPreventiveMeasuresEntity sysPreventiveMeasuresEntity;
        if(preventiveId != null){
            sysPreventiveMeasuresEntity = measuresMapper.selectOne(Wrappers.<SysPreventiveMeasuresEntity>lambdaQuery()
                    .eq(SysPreventiveMeasuresEntity::getId, preventiveId));
        }else{
            sysPreventiveMeasuresEntity = measuresMapper.selectOne(Wrappers.<SysPreventiveMeasuresEntity>lambdaQuery()
                    .eq(SysPreventiveMeasuresEntity::getInsectInfoId, insectId));
        }
        if(ObjectUtils.isEmpty(sysPreventiveMeasuresEntity)){
            return  null;
        }
        return sysPreventiveMeasuresEntity;
    }

    @Override
    public void preventiveAdd(SysPreventiveMeasuresEntity measuresEntity) {
        measuresEntity.setCreateTime(new Date());
        measuresEntity.setUpdateTime(new Date());
        measuresMapper.insert(measuresEntity);
    }

    @Override
    public void preventiveUpdate(SysPreventiveMeasuresEntity measuresEntity) {
        SysPreventiveMeasuresEntity measuresEntity1 = measuresMapper.selectById(measuresEntity.getId());
        if(ObjectUtils.isEmpty(measuresEntity1)){
            throw new MyException(ResultStatusCode.DATA_RESULT);
        }
        measuresEntity.setUpdateTime(new Date());
        measuresMapper.updateById(measuresEntity);
    }

    @Override
    public void preventiveDelete(Long id) {
        SysPreventiveMeasuresEntity measuresEntity1 = measuresMapper.selectById(id);
        if(ObjectUtils.isEmpty(measuresEntity1)){
            throw new MyException(ResultStatusCode.DATA_RESULT);
        }
        measuresMapper.deleteById(id);
    }

    @Override
    public List<SysInsectInfoEntity> insect() {
        return insectInfoMapper.selectList(null);
    }
}
