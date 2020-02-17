package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.mapper.SysPreventiveMeasuresMapper;
import com.aiot.aiotbackstage.model.entity.SysPreventiveMeasuresEntity;
import com.aiot.aiotbackstage.service.IPreventiveMasuresService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class PreventiveMasuresServiceImpl implements IPreventiveMasuresService {

    @Autowired
    private SysPreventiveMeasuresMapper measuresMapper;


    @Override
    public List<SysPreventiveMeasuresEntity> getMasuresInfo() {

        List<SysPreventiveMeasuresEntity> preventiveMeasuresEntities = measuresMapper.selectAll();
        if(CollectionUtils.isEmpty(preventiveMeasuresEntities)){
            throw new MyException(ResultStatusCode.MEASURES_NO_EXIT);
        }
        return  preventiveMeasuresEntities;
    }

    @Override
    public List<SysPreventiveMeasuresEntity> getMasuresInfoByName(String name) {

        List<SysPreventiveMeasuresEntity> preventiveMeasuresEntities = measuresMapper.selectList(Wrappers.<SysPreventiveMeasuresEntity>lambdaQuery()
                .like(SysPreventiveMeasuresEntity::getName, name));
        if(CollectionUtils.isEmpty(preventiveMeasuresEntities)){
            throw new MyException(ResultStatusCode.MEASURES_NO_EXIT);
        }
        return preventiveMeasuresEntities;
    }
}
