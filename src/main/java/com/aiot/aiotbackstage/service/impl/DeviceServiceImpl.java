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
import com.aiot.aiotbackstage.model.vo.DeviceResultVo;
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
    public List<DeviceResultVo> deviceInfoNew() {

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
            List<DeviceVo> deviceVos=new ArrayList<>();
            sysDeviceErrorRecEntities.forEach(sysDeviceErrorRecEntity -> {
                DeviceVo deviceVo=new DeviceVo();
                if(sysDeviceErrorRecEntity.getDeviceType().equals("RYU")){
                    deviceVo.setDeviceType(sysDeviceErrorRecEntity.getDeviceType());
                    deviceVo.setStartTime(sysDeviceErrorRecEntity.getStartTime());
                    deviceVo.setId(sysDeviceErrorRecEntity.getId());
                    deviceVo.setIsUpdate(sysDeviceErrorRecEntity.getIsUpdate());
                }else if(sysDeviceErrorRecEntity.getDeviceType().equals("CAMERA")){
                    deviceVo.setDeviceType(sysDeviceErrorRecEntity.getDeviceType());
                    deviceVo.setStartTime(sysDeviceErrorRecEntity.getStartTime());
                    deviceVo.setId(sysDeviceErrorRecEntity.getId());
                    deviceVo.setIsUpdate(sysDeviceErrorRecEntity.getIsUpdate());
                }else{
                    deviceVo.setDeviceType(sysDeviceErrorRecEntity.getDeviceType());
                    deviceVo.setStartTime(sysDeviceErrorRecEntity.getStartTime());
                    deviceVo.setId(sysDeviceErrorRecEntity.getId());
                    deviceVo.setIsUpdate(sysDeviceErrorRecEntity.getIsUpdate());
                }
                deviceVos.add(deviceVo);
            });
            sysDeviceErrorRecEntity1.setDeviceVo(deviceVos);
            list.add(sysDeviceErrorRecEntity1);
        });
        //组建正常的设备数据
        List<DeviceResultVo> entities=new ArrayList<>();
        //查询出所有的站点
        List<SysSiteEntity> sysSiteEntities = siteMapper.selectAll();
        sysSiteEntities.forEach(sysSiteEntity -> {
            DeviceResultVo deviceResultVo=new DeviceResultVo();
            deviceResultVo.setSiteId(sysSiteEntity.getId());
            deviceResultVo.setSiteName(sysSiteEntity.getName());
            List<DeviceVo> deviceVos=new ArrayList<>();
            DeviceVo deviceVo=new DeviceVo();
            deviceVo.setDeviceType("CAMERA");
            deviceVo.setDeviceName("摄像头");
            deviceVo.setStatus("正常");
            deviceVo.setStartTime(null);
            deviceVos.add(deviceVo);
            DeviceVo deviceVo1=new DeviceVo();
            deviceVo1.setDeviceType("RYU");
            deviceVo1.setDeviceName("传感器RTU");
            deviceVo1.setStatus("正常");
            deviceVo1.setStartTime(null);
            deviceVos.add(deviceVo1);
            DeviceVo deviceVo2=new DeviceVo();
            deviceVo2.setDeviceType("InsectDevice");
            deviceVo2.setDeviceName("虫情测报灯");
            deviceVo2.setStatus("正常");
            deviceVo2.setStartTime(null);
            deviceVos.add(deviceVo2);
            deviceResultVo.setDeviceVo(deviceVos);
            entities.add(deviceResultVo);
        });


        //将故障的设备数据和组建数据合并
        Map<Integer, List<DeviceResultVo>> collect1 = entities.stream().collect(Collectors.groupingBy(DeviceResultVo::getSiteId, Collectors.toList()));
        Map<Integer, List<SysDeviceErrorRecEntity>> collect2 = list.stream().collect(Collectors.groupingBy(SysDeviceErrorRecEntity::getSiteId, Collectors.toList()));
        Set<Integer> integers1 = collect1.keySet();
        Set<Integer> integers2 = collect2.keySet();
        integers1.stream().forEach(i -> {
            integers2.stream().forEach(j -> {
                if(i == j){
                    List<DeviceResultVo> entities1 = collect1.get(i);
                    List<SysDeviceErrorRecEntity> entities2 = collect2.get(j);
                    entities1.stream().forEach(errorRecEntity -> {
                        entities2.stream().forEach(errorRecEntity1 -> {
                            List<DeviceVo> deviceVo1 = errorRecEntity1.getDeviceVo();
                            List<DeviceVo> deviceVo = errorRecEntity.getDeviceVo();
                            if(!CollectionUtils.isEmpty(deviceVo1)){
                                deviceVo.forEach(deviceVo2 -> {
                                    deviceVo1.forEach(deviceVo3 -> {
                                        if(deviceVo2.getDeviceType().equals(deviceVo3.getDeviceType())&&deviceVo3.getIsUpdate() == 1){
                                            deviceVo2.setStatus("异常");
                                            deviceVo2.setStartTime(deviceVo3.getStartTime());
                                            deviceVo2.setId(deviceVo3.getId());
                                            deviceVo2.setIsUpdate(deviceVo3.getIsUpdate());
                                        }
                                    });
                                });
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
            long startTime = sysDeviceErrorRecEntity.getStartTime().getTime();
            long endTime = sysDeviceErrorRecEntity.getEndTime().getTime();
            long ss=(startTime-endTime)/(1000); //共计秒数
            int MM = (int)ss/60;   //共计分钟数
            int hh=(int)ss/3600;  //共计小时数
            int dd=(int)hh/24;   //共计天数
            sysDeviceErrorRecEntity.setDuration(Math.abs(MM)+" 分钟");
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
        errorRecEntity.setIsUpdate(2);
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