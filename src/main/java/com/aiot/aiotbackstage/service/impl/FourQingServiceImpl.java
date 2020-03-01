package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.common.util.FileUploadUtils;
import com.aiot.aiotbackstage.mapper.SysInsectRecReportMapper;
import com.aiot.aiotbackstage.mapper.SysSensorRecMapper;
import com.aiot.aiotbackstage.model.entity.SysInsectRecReportEntity;
import com.aiot.aiotbackstage.model.entity.SysSensorRecEntity;
import com.aiot.aiotbackstage.model.param.GetInsectRecReportParam;
import com.aiot.aiotbackstage.model.param.InsectRecReportParam;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.model.vo.SysSensorRecVo;
import com.aiot.aiotbackstage.service.IFourQingService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FourQingServiceImpl implements IFourQingService {

    @Autowired
    private SysSensorRecMapper sysSensorRecMapper;

    @Autowired
    private SysInsectRecReportMapper insectRecReportMapper;

    @Autowired
    private FileUploadUtils fileUploadUtils;
    @Override
    public List<SysSensorRecVo> meteorological(Long stationId) {

        List<SysSensorRecEntity> sysSensorRecEntities = sysSensorRecMapper
                .selectList(Wrappers.<SysSensorRecEntity>lambdaQuery()
                .eq(SysSensorRecEntity::getSiteId, stationId));
        if(CollectionUtils.isEmpty(sysSensorRecEntities)){
            return  null;
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

    @Override
    public void insectRecReport(InsectRecReportParam recReportParam) {
        SysInsectRecReportEntity insectRecReportEntity=new SysInsectRecReportEntity();
        insectRecReportEntity.setCategory(recReportParam.getCategory());
        insectRecReportEntity.setForecastObject(recReportParam.getForecastObject());
        insectRecReportEntity.setSurveyTime(recReportParam.getSurveyTime());
        insectRecReportEntity.setInfoLevel(recReportParam.getInfoLevel());
        insectRecReportEntity.setNewOrNot(recReportParam.getNewOrNot());
        insectRecReportEntity.setReportName(recReportParam.getReportName());
        insectRecReportEntity.setInfoTitle(recReportParam.getInfoTitle());
        insectRecReportEntity.setRemarks(recReportParam.getRemarks());
        insectRecReportEntity.setWhetherExamine(0);
        insectRecReportEntity.setCreateTime(new Date());
        insectRecReportEntity.setUpdateTime(new Date());
        insectRecReportEntity.setPictureUrl(recReportParam.getPictureUrl());
        insectRecReportMapper.insert(insectRecReportEntity);
    }

    @Override
    public void insectRecReportModify(InsectRecReportParam recReportParam) {

        SysInsectRecReportEntity insectRecReportEntity1 = insectRecReportMapper.selectById(recReportParam.getId());
        if(ObjectUtils.isEmpty(insectRecReportEntity1)){
            throw new MyException(ResultStatusCode.UPDATE_NO_EXIT);
        }
        SysInsectRecReportEntity insectRecReportEntity=new SysInsectRecReportEntity();
        insectRecReportEntity.setId(recReportParam.getId());
        insectRecReportEntity.setCategory(recReportParam.getCategory());
        insectRecReportEntity.setForecastObject(recReportParam.getForecastObject());
        insectRecReportEntity.setSurveyTime(recReportParam.getSurveyTime());
        insectRecReportEntity.setInfoLevel(recReportParam.getInfoLevel());
        insectRecReportEntity.setNewOrNot(recReportParam.getNewOrNot());
        insectRecReportEntity.setReportName(recReportParam.getReportName());
        insectRecReportEntity.setInfoTitle(recReportParam.getInfoTitle());
        insectRecReportEntity.setRemarks(recReportParam.getRemarks());
        insectRecReportEntity.setPictureUrl(recReportParam.getPictureUrl());
        insectRecReportEntity.setCreateTime(new Date());
        insectRecReportEntity.setUpdateTime(new Date());
        insectRecReportMapper.updateById(insectRecReportEntity);
    }

    @Override
    public PageResult<SysInsectRecReportEntity> insectRecReportGet(GetInsectRecReportParam recReportParam) {
        PageParam pageQuery=new PageParam();
        pageQuery.setPageSize(recReportParam.getPageSize());
        pageQuery.setPageNumber(recReportParam.getPageNumber());
        List<SysInsectRecReportEntity> sysInsectRecReportEntities =
                insectRecReportMapper.insectRecReportInfo(recReportParam.getWhetherExamine(), pageQuery);
        Integer total = insectRecReportMapper.insectRecReportCount(recReportParam.getWhetherExamine());
        if(CollectionUtils.isEmpty(sysInsectRecReportEntities)){
             return  null;
        }
        return PageResult.<SysInsectRecReportEntity>builder().total(total)
                .pageData(sysInsectRecReportEntities)
                .pageNumber(recReportParam.getPageNumber())
                .pageSize(recReportParam.getPageSize())
                .build();
    }

    @Override
    public String pestUpload(MultipartFile multipartFile) {
        JSONObject jsonObject = fileUploadUtils.obsFileUpload(multipartFile);
        String name = (String)jsonObject.get("name");
        log.info("修改虫子图片name=[{}]",name);
        return name;
    }

    @Override
    public void examine(Long id) {
        SysInsectRecReportEntity insectRecReportEntity = insectRecReportMapper.selectById(id);
        if(ObjectUtils.isEmpty(insectRecReportEntity)){
            throw new MyException(ResultStatusCode.PEST_BANK_NO_EXIT);
        }
        SysInsectRecReportEntity recReportEntity=new SysInsectRecReportEntity();
        recReportEntity.setId(id);
        recReportEntity.setWhetherExamine(1); //审核通过
        recReportEntity.setUpdateTime(new Date());
        insectRecReportMapper.updateById(recReportEntity);
    }

}
