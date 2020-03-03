package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.enums.DeviceType;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.mapper.SysDeviceErrorRecMapper;
import com.aiot.aiotbackstage.mapper.SysInsectInfoMapper;
import com.aiot.aiotbackstage.mapper.SysSiteMapper;
import com.aiot.aiotbackstage.model.entity.SysDeviceErrorRecEntity;
import com.aiot.aiotbackstage.model.entity.SysInsectInfoEntity;
import com.aiot.aiotbackstage.model.entity.SysSiteEntity;
import com.aiot.aiotbackstage.model.param.DeviceInfoNewParam;
import com.aiot.aiotbackstage.model.param.DeviceInfoOldParam;
import com.aiot.aiotbackstage.model.param.DeviceInfoParam;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.vo.DeviceVo;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.service.IDeviceService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/29 13:42
 */
@Data
@Service
public class DeviceServiceImpl implements IDeviceService {

    @Autowired
    private SysDeviceErrorRecMapper errorRecMapper;

    @Autowired
    private SysSiteMapper siteMapper;

    @Override
    public List<SysDeviceErrorRecEntity> deviceInfoNew() {

        //获取故障的设备
        List<SysDeviceErrorRecEntity> errorRecEntityList =
                errorRecMapper.selectList(Wrappers.<SysDeviceErrorRecEntity>lambdaQuery()
                        .isNull(SysDeviceErrorRecEntity::getEndTime));
        Map<Integer, List<SysDeviceErrorRecEntity>> collect = errorRecEntityList.stream().collect(Collectors.groupingBy(SysDeviceErrorRecEntity::getSiteId));
        Set<Integer> integers = collect.keySet();
        List<SysDeviceErrorRecEntity> list=new ArrayList<>();
        integers.forEach(integer -> {
            SysDeviceErrorRecEntity sysDeviceErrorRecEntity1=new SysDeviceErrorRecEntity();
            sysDeviceErrorRecEntity1.setSiteId(integer);
            List<SysDeviceErrorRecEntity> sysDeviceErrorRecEntities = collect.get(integer);
            DeviceVo deviceVo=new DeviceVo();
            sysDeviceErrorRecEntities.forEach(sysDeviceErrorRecEntity -> {
                if(sysDeviceErrorRecEntity.getDeviceType().equals("RYU")){
                    deviceVo.setRTU(sysDeviceErrorRecEntity.getDeviceType());
                }else if(sysDeviceErrorRecEntity.getDeviceType().equals("CAMERA")){
                    deviceVo.setCAMERA(sysDeviceErrorRecEntity.getDeviceType());
                }else{
                    deviceVo.setInsectDevice(sysDeviceErrorRecEntity.getDeviceType());
                }
            });
            sysDeviceErrorRecEntity1.setDeviceVo(deviceVo);
            list.add(sysDeviceErrorRecEntity1);
        });
        //组建正常的设备数据
        List<SysDeviceErrorRecEntity> entities=new ArrayList<>();
        for (int i = 0; i < 8 ; i++) {
            SysDeviceErrorRecEntity errorRecEntity=new SysDeviceErrorRecEntity();
            errorRecEntity.setSiteId(i);
            DeviceVo deviceVo=new DeviceVo();
            deviceVo.setCAMERA("CAMERA");
            deviceVo.setDeviceNameCAMERA("摄像头");
            deviceVo.setStatusCAMERA("正常");
            deviceVo.setInsectDevice("InsectDevice");
            deviceVo.setDeviceNameInsectDevice("虫情测报灯");
            deviceVo.setStatusInsectDevice("正常");
            deviceVo.setRTU("RYU");
            deviceVo.setDeviceNameRTU("传感器RTU");
            deviceVo.setStatusRTU("正常");
            errorRecEntity.setDeviceVo(deviceVo);
            entities.add(errorRecEntity);
        }
        //将故障的设备数据和组建数据合并
        Map<Integer, List<SysDeviceErrorRecEntity>> collect1 = entities.stream().collect(Collectors.groupingBy(SysDeviceErrorRecEntity::getSiteId, Collectors.toList()));
        Map<Integer, List<SysDeviceErrorRecEntity>> collect2 = list.stream().collect(Collectors.groupingBy(SysDeviceErrorRecEntity::getSiteId, Collectors.toList()));
        Set<Integer> integers1 = collect1.keySet();
        Set<Integer> integers2 = collect2.keySet();
        integers1.stream().forEach(i -> {
            integers2.stream().forEach(j -> {
                if(i == j){
                    List<SysDeviceErrorRecEntity> entities1 = collect1.get(i);
                    List<SysDeviceErrorRecEntity> entities2 = collect2.get(j);
                    entities1.stream().forEach(errorRecEntity -> {
                        entities2.stream().forEach(errorRecEntity1 -> {

                            DeviceVo deviceVos = errorRecEntity.getDeviceVo();
                            DeviceVo deviceVos1 = errorRecEntity1.getDeviceVo();
                            if(deviceVos.getCAMERA().equals(deviceVos1.getCAMERA())){
                                errorRecEntity.setStartTime(errorRecEntity1.getStartTime());
                                deviceVos.setStatusCAMERA("异常");
                            }
                            if(deviceVos.getInsectDevice().equals(deviceVos1.getInsectDevice())){
                                errorRecEntity.setStartTime(errorRecEntity1.getStartTime());
                                deviceVos.setStatusInsectDevice("异常");
                            }
                            if(deviceVos.getRTU().equals(deviceVos1.getRTU())){
                                errorRecEntity.setStartTime(errorRecEntity1.getStartTime());
                                deviceVos.setStatusRTU("异常");
                            }
                        });
                    });
                }
            });
        });
        return  entities;
        /*PageParam pageParam=new PageParam();
        pageParam.setPageNumber(param.getPageNumber());
        pageParam.setPageSize(param.getPageSize());

        List<SysDeviceErrorRecEntity> sysDeviceErrorRecEntities;
        Integer total;
        if(param.getDimension() == 1){
            sysDeviceErrorRecEntities = errorRecMapper.deviceErrorRecNewYearPage(param);
            total= errorRecMapper.countYear(param);
        }else{
            sysDeviceErrorRecEntities = errorRecMapper.deviceErrorRecNewYuePage(param);
            total= errorRecMapper.countYue(param);
        }
        if(CollectionUtils.isEmpty(sysDeviceErrorRecEntities)){
            throw new MyException(ResultStatusCode.NO_RESULT);
        }
        sysDeviceErrorRecEntities.stream().forEach(sysDeviceErrorRecEntity -> {
            long startTime = sysDeviceErrorRecEntity.getStartTime().getTime();
            if(sysDeviceErrorRecEntity.getEndTime() != null ){
                long endTime = sysDeviceErrorRecEntity.getEndTime().getTime();
                long ss=(startTime-endTime)/(1000); //共计秒数
                int MM = (int)ss/60;   //共计分钟数
                int hh=(int)ss/3600;  //共计小时数
                int dd=(int)hh/24;   //共计天数
                if(hh == 0){
                    sysDeviceErrorRecEntity.setDuration(MM+" 分钟");
                }else{
                    sysDeviceErrorRecEntity.setDuration(hh+" 小时 "+MM+" 分钟");
                }
            }
            SysSiteEntity siteEntity = siteMapper.selectById(sysDeviceErrorRecEntity.getSiteId());
            sysDeviceErrorRecEntity.setSiteName(siteEntity.getName());
            if(sysDeviceErrorRecEntity.getDeviceType().equals("RTU")){
                sysDeviceErrorRecEntity.setDeviceName("RTU");
            }else if(sysDeviceErrorRecEntity.getDeviceType().equals("CAMERA")){
                sysDeviceErrorRecEntity.setDeviceName("摄像头");
            }else{
                sysDeviceErrorRecEntity.setDeviceName("虫情测报灯");
            }
        });
        return PageResult.<SysDeviceErrorRecEntity>builder()
                .total(total)
                .pageData(sysDeviceErrorRecEntities)
                .pageNumber(pageParam.getPageNumber())
                .pageSize(pageParam.getPageSize())
                .build();*/
    }

    public long fromDateStringToLong(String inVal) { //此方法计算时间毫秒
        Date date = null; //定义时间类型       
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd hh:ss");
        try {
        date = inputFormat.parse(inVal); //将字符型转换成日期型
        } catch (Exception e) {
        e.printStackTrace();
        }
        return date.getTime(); //返回毫秒数
    }

    @Override
    public PageResult<SysDeviceErrorRecEntity> deviceInfoOld(DeviceInfoOldParam param) {
        PageParam pageParam=new PageParam();
        pageParam.setPageNumber(param.getPageNumber());
        pageParam.setPageSize(param.getPageSize());
        List<SysDeviceErrorRecEntity> sysDeviceErrorRecEntities = errorRecMapper.deviceErrorRecOldPage(param);
        Integer total = errorRecMapper.countOld(param);

        if(CollectionUtils.isEmpty(sysDeviceErrorRecEntities)){
            return  null;
        }

        sysDeviceErrorRecEntities.stream().forEach(sysDeviceErrorRecEntity -> {
            SysSiteEntity siteEntity = siteMapper.selectById(sysDeviceErrorRecEntity.getSiteId());
            sysDeviceErrorRecEntity.setSiteName(siteEntity.getName());
            if(sysDeviceErrorRecEntity.getDeviceType().equals("RTU")){
                sysDeviceErrorRecEntity.setDeviceName("RTU");
            }else if(sysDeviceErrorRecEntity.getDeviceType().equals("CAMERA")){
                sysDeviceErrorRecEntity.setDeviceName("摄像头");
            }else{
                sysDeviceErrorRecEntity.setDeviceName("虫情测报灯");
            }
        });
        return PageResult.<SysDeviceErrorRecEntity>builder()
                .total(total)
                .pageData(sysDeviceErrorRecEntities)
                .pageNumber(pageParam.getPageNumber())
                .pageSize(pageParam.getPageSize())
                .build();
    }

    @Override
    public void deviceInfoNewModify(DeviceInfoParam param) {
        SysDeviceErrorRecEntity errorRecEntity1 = errorRecMapper.selectById(param.getId());
        if(ObjectUtils.isEmpty(errorRecEntity1)){
            throw new MyException(ResultStatusCode.DATA_RESULT);
        }

        SysDeviceErrorRecEntity errorRecEntity=new SysDeviceErrorRecEntity();
        errorRecEntity.setId(param.getId());
        errorRecEntity.setSiteId(param.getSiteId());
        errorRecEntity.setDeviceType(param.getDeviceType());
        errorRecEntity.setSubType(null);
        errorRecEntity.setStartTime(param.getStartTime());
        errorRecEntity.setEndTime(param.getEndTime());
        errorRecEntity.setCreateTime(new Date());
        errorRecMapper.updateById(errorRecEntity);

    }

    @Override
    public void deviceInfoOldModify(DeviceInfoParam param) {
        deviceInfoNewModify(param);
    }

    @Override
    public void deviceInfoOldDel(Integer id) {
        SysDeviceErrorRecEntity errorRecEntity1 = errorRecMapper.selectById(id);
        if(ObjectUtils.isEmpty(errorRecEntity1)){
            throw new MyException(ResultStatusCode.DATA_RESULT);
        }
        errorRecMapper.deleteById(id);
    }
}
