package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.mapper.*;
import com.aiot.aiotbackstage.model.entity.*;
import com.aiot.aiotbackstage.model.vo.*;
import com.aiot.aiotbackstage.service.IFourQingService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FourQingServiceImpl implements IFourQingService {

    @Autowired
    private SysSensorRecMapper sysSensorRecMapper;

    @Override
    public List<SysSensorRecVo> meteorological(Long stationId) {

        List<SysSensorRecEntity> sysSensorRecEntities = sysSensorRecMapper
                .selectList(Wrappers.<SysSensorRecEntity>lambdaQuery()
                .eq(SysSensorRecEntity::getSiteId, stationId));
        if(CollectionUtils.isEmpty(sysSensorRecEntities)){
            throw new MyException(ResultStatusCode.SYSSENSORREC_NO_EXIT);
        }
        Map<Date, List<SysSensorRecEntity>> collect = sysSensorRecEntities.stream()
                .collect(Collectors.groupingBy(SysSensorRecEntity::getTime, Collectors.toList()));
        Set<Date> dates = collect.keySet();

        List<SysSensorRecVo> sensorRecVos=new ArrayList<>();
        dates.stream().forEach(date -> {
            SysSensorRecVo sensorRecVo=new SysSensorRecVo();
            sensorRecVo.setTime(date);
            List<SysSensorRecEntity> sysSensorRecList = collect.get(date);
            sysSensorRecList.stream().forEach(sysSensorRecEntity -> {
                if(sysSensorRecEntity.getSensor().equals("humidity")){
                    sensorRecVo.setHumidity(sysSensorRecEntity.getValue()+"RH%");
                }
                if(sysSensorRecEntity.getSensor().equals("temperature")){
                    sensorRecVo.setTemperature(sysSensorRecEntity.getValue()+"℃");
                }
                if(sysSensorRecEntity.getSensor().equals("noisy")){
                    sensorRecVo.setNoisy(sysSensorRecEntity.getValue()+"dB");
                }
                if(sysSensorRecEntity.getSensor().equals("PM10")){
                    sensorRecVo.setPM10(sysSensorRecEntity.getValue()+"mg/m3");
                }
                if(sysSensorRecEntity.getSensor().equals("PM25")){
                    sensorRecVo.setPM25(sysSensorRecEntity.getValue()+"μg/m³");
                }
                if(sysSensorRecEntity.getSensor().equals("atmos")){
                    sensorRecVo.setAtmos(sysSensorRecEntity.getValue()+"Pa");
                }
            });
            sensorRecVos.add(sensorRecVo);
        });
        return sensorRecVos;
    }


}
