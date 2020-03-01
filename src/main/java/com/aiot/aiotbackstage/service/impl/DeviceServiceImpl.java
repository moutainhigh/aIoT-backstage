package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
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
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.service.IDeviceService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public PageResult<SysDeviceErrorRecEntity> deviceInfoNew(DeviceInfoNewParam param) {

        PageParam pageParam=new PageParam();
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
                .build();
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
