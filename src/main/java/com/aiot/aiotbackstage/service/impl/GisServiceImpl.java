package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.mapper.SysSiteMapper;
import com.aiot.aiotbackstage.model.entity.SysSiteEntity;
import com.aiot.aiotbackstage.service.IGisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class GisServiceImpl implements IGisService {

    @Autowired
    private SysSiteMapper siteMapper;


    @Override
    public List<SysSiteEntity> stationInfo() {

        List<SysSiteEntity> gisStationEntities = siteMapper.selectAll();
        if(CollectionUtils.isEmpty(gisStationEntities)){
            throw new MyException(ResultStatusCode.GIS_NO_EXIT);
        }
        return gisStationEntities;
    }
}
