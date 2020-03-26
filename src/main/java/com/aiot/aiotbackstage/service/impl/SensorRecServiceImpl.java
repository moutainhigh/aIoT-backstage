package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.enums.RtuAddrCode;
import com.aiot.aiotbackstage.common.enums.SensorType;
import com.aiot.aiotbackstage.common.enums.WindDirection;
import com.aiot.aiotbackstage.common.util.RedisUtils;
import com.aiot.aiotbackstage.mapper.*;
import com.aiot.aiotbackstage.model.dto.RtuData;
import com.aiot.aiotbackstage.model.entity.*;
import com.aiot.aiotbackstage.model.param.InsectRecByDateParam;
import com.aiot.aiotbackstage.model.param.SysNewRuleParam;
import com.aiot.aiotbackstage.model.vo.SysSensorRecVo2;
import com.aiot.aiotbackstage.service.IEarlyWarningService;
import com.aiot.aiotbackstage.service.ISensorRecService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author Avernus
 */
@Service
public class SensorRecServiceImpl extends ServiceImpl<SysSensorRecMapper, SysSensorRecEntity> implements ISensorRecService {

    @Resource
    private SysSiteMapper sysSiteMapper;
    @Resource
    private SysDustRecMapper sysDustRecMapper;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private IEarlyWarningService earlyWarningService;
    @Resource
    private SysInsectDeviceMapper deviceMapper;
    @Resource
    private SysInsectRecStatisMapper insectRecMapper;


    @Override
    public void receive(RtuData rtuData) {
        SysSiteEntity site = sysSiteMapper.selectById(rtuData.getRtu());
        if (site == null) {
            return;
        }
        RtuAddrCode code = RtuAddrCode.trans(rtuData.getAddr());
        if (code == null) {
            return;
        }

        Date time = new Date(System.currentTimeMillis());
        switch (code) {
            case WIND_SPEED:
                SysSensorRecEntity ws = new SysSensorRecEntity(site.getId(), SensorType.wind_speed.name(), rtuData.getData()[0] / 10 + "", time);
                save(ws);
                break;
            case WIND_DIRECTION:
                String trans = WindDirection.trans(rtuData.getData()[0]);
                SysSensorRecEntity wd = new SysSensorRecEntity(site.getId(), SensorType.wind_direction.name(), trans, time);
                save(wd);
                break;
            case ATMOS:
                SysSensorRecEntity humidity = new SysSensorRecEntity(site.getId(), SensorType.humidity.name(),rtuData.getData()[0] / 10 + "", time);
                SysSensorRecEntity temperature = new SysSensorRecEntity(site.getId(), SensorType.temperature.name(), rtuData.getData()[1] / 10 + "", time);
                SysSensorRecEntity noisy = new SysSensorRecEntity(site.getId(), SensorType.noise.name(), rtuData.getData()[2] / 10 + "", time);
                SysSensorRecEntity PM25 = new SysSensorRecEntity(site.getId(), SensorType.PM25.name(), rtuData.getData()[3] + "", time);
                SysSensorRecEntity PM10 = new SysSensorRecEntity(site.getId(), SensorType.PM10.name(), rtuData.getData()[4] + "", time);
                SysSensorRecEntity atmos = new SysSensorRecEntity(site.getId(), SensorType.atmos.name(), rtuData.getData()[5] / 10 + "", time);
                save(humidity, temperature, noisy, PM10, PM25, atmos);
                break;
            case DUST_10CM:
                SysDustRecEntity dust10 = new SysDustRecEntity(site.getId(), 10, rtuData.getData(), time);
                save(dust10);
                break;
            case DUST_20CM:
                SysDustRecEntity dust20 = new SysDustRecEntity(site.getId(), 20, rtuData.getData(), time);
                save(dust20);
                break;
            case DUST_40CM:
                SysDustRecEntity dust40 = new SysDustRecEntity(site.getId(), 40, rtuData.getData(), time);
                save(dust40);
                break;
            default:

        }
    }

    @Override
    public Map<String, Object> current(Integer siteId) {
        Map<String, Object> result = new HashMap<>();
        for (SensorType value : SensorType.values()) {
            Object o = redisUtils.get("SENSOR-VALUE:" + siteId + ":" + value.name());
            result.put(value.name(), o == null ? "-" : o);
        }
        return result;
    }

    private void save(SysSensorRecEntity... entities) {
        List<SysSensorRecEntity> list = Arrays.asList(entities);
        for (SysSensorRecEntity entity : list) {
            redisUtils.set("SENSOR-VALUE:" + entity.getSiteId() + ":" + entity.getSensor(), entity.getValue());
            try {
                earlyWarningService.earlyWarningReport("气象", entity.getSensor(), null, entity.getValue(), entity.getSiteId());
                Date date = new Date();
                SimpleDateFormat f = new SimpleDateFormat("MM-dd");
                String time = f.format(date);
                String temp="";
                if(entity.getSensor().equals("temperature")){
                    temp=entity.getValue();
                }else{
                    temp=null;
                }
                String humidity="";
                if(entity.getSensor().equals("humidity")){
                    humidity=entity.getValue();
                }else{
                    humidity=null;
                }
                SysNewRuleParam newRuleEntity=new SysNewRuleParam();
                earlyWarningService.earlyWarningReportNew(newRuleEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        saveBatch(list);
    }

    private void save(SysDustRecEntity entity) {
        redisUtils.set("SENSOR-VALUE:" + entity.getSiteId() + ":" + entity.getDepth(), entity);
        sysDustRecMapper.insert(entity);
        try {
            Date date = new Date();
            SimpleDateFormat f = new SimpleDateFormat("MM-dd");
            String time = f.format(date);
            SysNewRuleParam newRuleEntity=new SysNewRuleParam();
            earlyWarningService.earlyWarningReportNew(newRuleEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getStatByTime(String time) {
        List<SysSensorRecVo2> result = baseMapper.findByTimeGroupBySensor(time);

        Map<String, List<SysSensorRecVo2>> collect = result.stream().collect(Collectors.groupingBy(SysSensorRecEntity::getSensor));

        Map<String, Map<String, Object>> map = new HashMap<>();
        Map<String, Object> tempMap;
        for (Map.Entry<String, List<SysSensorRecVo2>> entry : collect.entrySet()) {
            tempMap = new HashMap<>();
            map.put(entry.getKey(), tempMap);

            List<String> x = new ArrayList<>();
            List<String> y = new ArrayList<>();

            tempMap.put("x", x);
            tempMap.put("y", y);

            for (SysSensorRecVo2 item : entry.getValue()) {
                x.add(item.getSiteName());
                y.add(item.getValue());
            }
        }
        return map;
    }

    @Override
    public Map<String,Object> insectRecByDate(InsectRecByDateParam param) {
        List<SysInsectRecStatisEntity> sysInsectRecEntities = insectRecMapper
                .selectList(Wrappers.<SysInsectRecStatisEntity>lambdaQuery()
                        .between(true, SysInsectRecStatisEntity::getDate, param.getStartDate(), param.getEndDate()));
        if(CollectionUtils.isEmpty(sysInsectRecEntities)){
            return null;
        }
        //获取设备对应的站点数据
        List<String> collect2 = sysInsectRecEntities.stream().map(SysInsectRecStatisEntity::getDeviceId).collect(Collectors.toList());
        List<String> collect3 = collect2.stream().distinct().collect(Collectors.toList());
        List<SysInsectDeviceEntity> sysInsectDeviceEntities = deviceMapper.selectList(Wrappers.<SysInsectDeviceEntity>lambdaQuery().in(SysInsectDeviceEntity::getImei, collect3));
        List<Integer> collect4 = sysInsectDeviceEntities.stream().map(SysInsectDeviceEntity::getSiteId).collect(Collectors.toList());
        List<SysSiteEntity> sysSiteEntities = sysSiteMapper.selectBatchIds(collect4);

        TreeMap<String, List<SysInsectRecStatisEntity>> collect = sysInsectRecEntities.stream()
                .collect(Collectors.groupingBy(SysInsectRecStatisEntity::getDate
                        , TreeMap::new, Collectors.toList()));
        Set<String> strings = collect.keySet();
        List<Map<String,Object>> siteList=new ArrayList<>();
        List<String> list=new ArrayList<>();
        strings.stream().forEach(s -> {
            list.add(s);
            List<SysInsectRecStatisEntity> sysInsectRecStatisEntities = collect.get(s);
            Map<String, List<SysInsectRecStatisEntity>> collect1 = sysInsectRecStatisEntities.stream().collect(Collectors.groupingBy(SysInsectRecStatisEntity::getDeviceId));
            Set<String> strings1 = collect1.keySet();
            strings1.stream().forEach(s1 -> {
                List<SysInsectRecStatisEntity> sysInsectRecStatisEntities1 = collect1.get(s1);
                int sum = sysInsectRecStatisEntities1.stream().mapToInt(SysInsectRecStatisEntity::getNum).sum();
                sysInsectDeviceEntities.stream().forEach(sysInsectDeviceEntity -> {

                    if(Integer.parseInt(s1) == sysInsectDeviceEntity.getId()){
                        Map<String,Object> map=new HashMap<>();
                        sysSiteEntities.stream().forEach(sysSiteEntity -> {
                            if(sysInsectDeviceEntity.getSiteId() == sysSiteEntity.getId()){
                                map.put("siteName",sysSiteEntity.getName());
                            }
                        });
                        map.put("insectRecCount",sum);
                        map.put("date",s);
                        siteList.add(map);
                    }
                });
            });
        });
        Map<Object, List<Map<String, Object>>> date = siteList.stream().collect(Collectors.groupingBy(stringObjectMap -> stringObjectMap.get("siteName")));
        Set<Object> objects = date.keySet();
        List<Map<String,Object>> siteList1=new ArrayList<>();
        objects.forEach(o -> {
            Map<String,Object> map1=new HashMap<>();
            String sa="";
            String dateStr="";
            List<Map<String, Object>> maps = date.get(o);
            for (Map<String, Object> map : maps) {
                sa+=map.get("insectRecCount")+",";
                dateStr+=map.get("date")+",";
            }
            map1.put("insectRecCount",sa.substring(0,sa.length()-1));
            map1.put("dateStr",dateStr.substring(0,dateStr.length()-1));
            map1.put("siteName",o);
            siteList1.add(map1);
        });
        List<String> strings1 = dateList(param.getStartDate(), param.getEndDate());

        for (Map<String, Object> stringObjectMap : siteList1) {
            String[] dateStrs = stringObjectMap.get("dateStr").toString().split(",");
            List<String> strings2 = Arrays.asList(dateStrs);
            List<String> result = strings1.stream().filter(item -> !strings2.contains(item)).collect(Collectors.toList());
            if(result.size() > 0){
                result.forEach(s -> {
                    stringObjectMap.put("insectRecCount",stringObjectMap.get("insectRecCount").toString()+","+0);
                });
            }
            String[] insectRecCounts = stringObjectMap.get("insectRecCount").toString().split(",");
            String[] dateStr = stringObjectMap.get("dateStr").toString().split(",");
            stringObjectMap.put("insectRecCount",Arrays.asList(insectRecCounts));
            stringObjectMap.put("dateStr",Arrays.asList(dateStr));
        }
        String[] siteStr={"三泉村","龙泉村","灯塔村","牌坊村"};
        for (String s : siteStr) {
            Map map =new HashMap();
            map.put("siteName",s);
            map.put("dateStr",strings1);
            List list1=new ArrayList();
            for (String s1 : strings1) {
                list1.add("0");
            }
            map.put("insectRecCount",list1);
            siteList1.add(map);
        }

        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("dateList",list);
        resultMap.put("siteList",siteList1);
        return resultMap;
    }

    private  long get_Date(Calendar c) {
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
        return c.getTimeInMillis();
    }

    /**
     * 开始时间与结束时间之间的所有日期
     * @param beginDate
     * @param endDate
     * @return
     */
    private List<String> dateList(Date beginDate,Date endDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        List<String> list=new ArrayList<>();
        for (long d = cal.getTimeInMillis(); d <= endDate.getTime(); d = get_Date(cal)) {
            list.add(sdf.format(d));
        }
        return list;
    }

}
