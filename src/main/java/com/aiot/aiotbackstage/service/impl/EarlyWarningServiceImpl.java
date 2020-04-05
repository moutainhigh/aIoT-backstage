package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.config.GisConfig;
import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.enums.SensorType;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.common.util.JWTUtil;
import com.aiot.aiotbackstage.common.util.WeXinUtils;
import com.aiot.aiotbackstage.mapper.*;
import com.aiot.aiotbackstage.model.entity.*;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.param.WarnInfoParam;
import com.aiot.aiotbackstage.model.param.WarnRuleParam;
import com.aiot.aiotbackstage.model.vo.EarlyInfoVo;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.service.IEarlyWarningService;
import com.aiot.aiotbackstage.service.ISensorRecService;
import com.aiot.aiotbackstage.service.ISysDustRecService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/24 10:37
 */
@Service
@Slf4j
public class EarlyWarningServiceImpl implements IEarlyWarningService {

    @Autowired
    private SysWarnRuleMapper warnRuleMapper;

    @Autowired
    private SysWarnInfoMapper warnInfoMapper;

    @Autowired
    private SysSiteMapper siteMapper;

    @Autowired
    private SysPreventiveMeasuresMapper measuresMapper;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GisConfig gisConfig;

    @Autowired
    private SysNewRuleMapper newRuleMapper;

    @Autowired
    private ISensorRecService SensorRecService;

    @Autowired
    private ISysDustRecService dustRecService;


    @Override
    public void earlyInfoAdd(WarnRuleParam param) {
        SysWarnRuleEntity warnRuleEntity = new SysWarnRuleEntity();
        warnRuleEntity.setEarlyType(earlyType(param.getEarlyType()));
        if (param.getEarlyType().equals("虫情")) {
            SysPreventiveMeasuresEntity measuresEntity = measuresMapper.selectOne(Wrappers.<SysPreventiveMeasuresEntity>lambdaQuery()
                    .eq(SysPreventiveMeasuresEntity::getSeason, param.getEarlyName()));
            if (ObjectUtils.isNotEmpty(measuresEntity)) {
                warnRuleEntity.setMeasures(measuresEntity.getMeasuresInfo());
            }
        }
        warnRuleEntity.setEarlyName(param.getEarlyName());
        warnRuleEntity.setEarlyDepth(param.getEarlyDepth());
        warnRuleEntity.setEarlyMax(param.getEarlyMax());
        warnRuleEntity.setEarlyMax(param.getEarlyMin());
        warnRuleEntity.setEarlyDegree(param.getEarlyDegree());
        warnRuleEntity.setEarlyContent(param.getEarlyContent());
        warnRuleEntity.setCreateTime(new Date());
        warnRuleEntity.setUpdateTime(new Date());
        warnRuleMapper.insert(warnRuleEntity);
    }

    private String earlyType(String earlyType) {
        if (earlyType.equals("1")) {
            return "苗情";
        } else if (earlyType.equals("2")) {
            return "灾情";
        } else if (earlyType.equals("3")) {
            return "气象";
        } else if (earlyType.equals("4")) {
            return "土壤";
        } else {
            return "虫情";
        }
    }


    @Override
    public void earlyInfoModify(WarnRuleParam param) {

        SysWarnRuleEntity warnRuleEntity1 = warnRuleMapper.selectById(param.getId());
        if (ObjectUtils.isEmpty(warnRuleEntity1)) {
            throw new MyException(ResultStatusCode.EARLY_WARNING_NO_EXIT);
        }
        SysWarnRuleEntity warnRuleEntity = new SysWarnRuleEntity();
        warnRuleEntity.setId(param.getId());
        warnRuleEntity.setEarlyType(param.getEarlyType());
        warnRuleEntity.setEarlyName(param.getEarlyName());
        warnRuleEntity.setEarlyDepth(param.getEarlyDepth());
        warnRuleEntity.setEarlyMax(param.getEarlyMax());
        warnRuleEntity.setEarlyMin(param.getEarlyMin());
        warnRuleEntity.setEarlyDegree(param.getEarlyDegree());
        warnRuleEntity.setEarlyContent(param.getEarlyContent());
        warnRuleEntity.setCreateTime(new Date());
        warnRuleEntity.setUpdateTime(new Date());
        warnRuleMapper.updateById(warnRuleEntity);
    }

    @Override
    public void earlyInfoDelete(Long id) {
        SysWarnRuleEntity warnRuleEntity1 = warnRuleMapper.selectById(id);
        if (ObjectUtils.isEmpty(warnRuleEntity1)) {
            throw new MyException(ResultStatusCode.EARLY_WARNING_NO_EXIT);
        }
        warnRuleMapper.deleteById(id);
    }

    @Override
    public PageResult<SysWarnRuleEntity> earlyInfoPage(String earlyType, PageParam pageParam) {

        Integer total;
        List<SysWarnRuleEntity> ruleEntityList;
        if (earlyType == null || "".equals(earlyType)) {
            total = warnRuleMapper.selectCount(null);
            ruleEntityList = warnRuleMapper.warnRulePage(null, pageParam);
        } else {
            total = warnRuleMapper.selectCount(Wrappers.<SysWarnRuleEntity>lambdaQuery().eq(SysWarnRuleEntity::getEarlyType, earlyType));
            ruleEntityList = warnRuleMapper.warnRulePage(earlyType, pageParam);
        }
        if (CollectionUtils.isEmpty(ruleEntityList)) {
            return null;
        }
        return PageResult.<SysWarnRuleEntity>builder()
                .total(total)
                .pageData(ruleEntityList)
                .pageNumber(pageParam.getPageNumber())
                .pageSize(pageParam.getPageSize())
                .build();
    }

    @Override
    public void earlyInfoReport(WarnInfoParam param, String token) {

        String userName = jwtUtil.getUserNameByToken(token);

        SysWarnInfoEntity warnInfoEntity = new SysWarnInfoEntity();
        warnInfoEntity.setSiteId(param.getSiteId());
        SysSiteEntity sysSiteEntity = siteMapper.selectById(param.getSiteId());
        warnInfoEntity.setSiteName(sysSiteEntity.getName());
        warnInfoEntity.setIsClosed(2);
        warnInfoEntity.setIsExamine(0);
        warnInfoEntity.setEarlyName(param.getEarlyName());
        warnInfoEntity.setEarlyDepth(param.getEarlyDepth());
        warnInfoEntity.setTime(param.getTime());
        warnInfoEntity.setEarlyType(param.getEarlyType());
        warnInfoEntity.setEarlyDegree(param.getEarlyDegree());
        warnInfoEntity.setEarlyContent(param.getEarlyContent());
        if (userName != null) {
            warnInfoEntity.setReportUser(userName);
        }
        if (param.getCoordinate() == null) {
            //查询站点对应的坐标
            warnInfoEntity.setCoordinate(sysSiteEntity.getCoordinate());
        } else {
            warnInfoEntity.setCoordinate(param.getCoordinate());
        }
        warnInfoEntity.setCreateTime(new Date());
        warnInfoEntity.setUpdateTime(new Date());
        warnInfoMapper.insert(warnInfoEntity);
    }

    @Override
    public PageResult<SysWarnInfoEntity> earlyInfoReportPage(PageParam pageParam) {
        Integer total = warnInfoMapper.selectCount(Wrappers.<SysWarnInfoEntity>lambdaQuery()
                .eq(SysWarnInfoEntity::getIsClosed, 2));
        List<SysWarnInfoEntity> sysWarnInfoEntities = warnInfoMapper.warnInfoPage(pageParam);
        if (CollectionUtils.isEmpty(sysWarnInfoEntities)) {
            return null;
        }
        return PageResult.<SysWarnInfoEntity>builder()
                .total(total)
                .pageData(sysWarnInfoEntities)
                .pageNumber(pageParam.getPageNumber())
                .pageSize(pageParam.getPageSize())
                .build();
    }

    @Override
    public void earlyClosed(Long id) {
        SysWarnInfoEntity warnInfoEntity = warnInfoMapper.selectById(id);
        if (ObjectUtils.isEmpty(warnInfoEntity)) {
            throw new MyException(ResultStatusCode.EARLY_WARNING_ID_IS_EXIT);
        }
        warnInfoEntity.setIsClosed(1);
        warnInfoMapper.updateById(warnInfoEntity);
    }

    @Autowired
    private WeXinUtils weXinUtils;

    @Override
    public void earlyInfoExamine(Long id) {
        SysWarnInfoEntity warnInfoEntity = warnInfoMapper.selectById(id);
        if (ObjectUtils.isEmpty(warnInfoEntity)) {
            throw new MyException(ResultStatusCode.EARLY_WARNING_ID_IS_EXIT);
        }
        warnInfoEntity.setIsExamine(1);
        warnInfoMapper.updateById(warnInfoEntity);

        try {
            if (warnInfoEntity.getEarlyType().equals("虫情")) {
                earlyInfoGis();
            }
        } catch (Exception e) {
            log.info("GIS调用失败[{}]", e.getMessage());
        }
        //预警信息微信公众号推送
//        weXinUtils.push();
    }

    @Override
    public Map<String, Object> earlyContent(String earlyType, String earlyDegree) {

//        List<Map<String,Object>> mapList=new ArrayList<>();
        List<SysWarnRuleEntity> warnRuleEntity = warnRuleMapper
                .selectList(Wrappers.<SysWarnRuleEntity>lambdaQuery()
                        .eq(SysWarnRuleEntity::getEarlyType, earlyType)
                        .eq(SysWarnRuleEntity::getEarlyDegree, earlyDegree));
        if (ObjectUtils.isEmpty(warnRuleEntity)) {
            return null;
        }
//        Map<String, List<SysWarnRuleEntity>> collect = warnRuleEntity.stream()
//                .collect(Collectors.groupingBy(SysWarnRuleEntity::getEarlyName, Collectors.toList()));
//        Set<String> strings = collect.keySet();
//        strings.forEach(s -> {
//            Map<String,Object> map=new HashMap<>();
//            map.put("earlyName",s);
//            List<String> list=new ArrayList<>();
//            List<SysWarnRuleEntity> ruleEntityList = collect.get(s);
//            ruleEntityList.forEach(sysWarnRuleEntity -> {
//                list.add(sysWarnRuleEntity.getEarlyContent());
//            });
//            map.put("earlyContent",list);
//            mapList.add(map);
//        });
        Map<String, Object> map = new HashMap<>();
        map.put("earlyName", warnRuleEntity.get(0).getEarlyName());
        map.put("earlyContent", warnRuleEntity.get(0).getEarlyContent());
        return map;
    }

    @Override
    public Integer earlyCount() {
        return warnInfoMapper.selectCount(Wrappers.<SysWarnInfoEntity>lambdaQuery()
                .eq(SysWarnInfoEntity::getIsClosed, 2)
                .eq(SysWarnInfoEntity::getIsExamine, 1));
    }

    /**
     * 自动预警
     *
     * @param type     预警类型  苗情  灾情  虫情  土壤
     * @param typeName 预警名称  苗情  灾情  虫情  土壤  对应的属性名称
     * @param depth    土壤深度10cm 20cm 40cm
     * @param value    属性对应的值
     * @param siteId   站点名称
     */
    @Override
    public void earlyWarningReport(String type, String typeName, String depth, String value, Integer siteId) {

        SysWarnRuleEntity warnRuleEntity;
        //如果土壤深度不为空则根据土壤深度查询
        if (depth != null) {
            warnRuleEntity = warnRuleMapper
                    .selectOne(Wrappers.<SysWarnRuleEntity>lambdaQuery()
                            .eq(SysWarnRuleEntity::getEarlyType, type)
                            .eq(SysWarnRuleEntity::getEarlyName, typeName)
                            .eq(SysWarnRuleEntity::getEarlyDepth, depth));
        } else {
            warnRuleEntity = warnRuleMapper
                    .selectOne(Wrappers.<SysWarnRuleEntity>lambdaQuery()
                            .eq(SysWarnRuleEntity::getEarlyType, type)
                            .eq(SysWarnRuleEntity::getEarlyName, typeName));
        }
        if (ObjectUtils.isNotEmpty(warnRuleEntity)) {
            String earlyMax = warnRuleEntity.getEarlyMax();
            String earlyMin = warnRuleEntity.getEarlyMin();
            Double v = Double.parseDouble(value);
            Double v1 = Double.parseDouble(earlyMax);
            Double v2 = Double.parseDouble(earlyMin);
            int i = v.compareTo(v1);
            int j = v.compareTo(v2);
            if (i > 0 && j < 0) {

                SysWarnInfoEntity warnInfoEntity = new SysWarnInfoEntity();
                warnInfoEntity.setSiteId(siteId);
                SysSiteEntity sysSiteEntity = siteMapper.selectById(siteId);
                warnInfoEntity.setSiteName(sysSiteEntity.getName());
                warnInfoEntity.setIsClosed(2);
                warnInfoEntity.setIsExamine(1);
                warnInfoEntity.setTime(new Date());
                warnInfoEntity.setEarlyType(type);
                warnInfoEntity.setEarlyName(warnRuleEntity.getEarlyName());
                if (warnRuleEntity.getEarlyDepth() != null) {
                    warnInfoEntity.setEarlyDepth(warnRuleEntity.getEarlyDepth());
                }
                warnInfoEntity.setEarlyDegree(warnRuleEntity.getEarlyDegree());
                warnInfoEntity.setEarlyContent(warnRuleEntity.getEarlyContent());
                warnInfoEntity.setCreateTime(new Date());
                warnInfoEntity.setUpdateTime(new Date());
                warnInfoMapper.insert(warnInfoEntity);
            }
        }
    }


    /**
     * 自动预警  NEW
     */
    @Override
    public void earlyWarningReportNew() {

        //获取规则库数据
        List<SysNewRuleEntity> sysNewRuleEntities = newRuleMapper.selectList(null);
        //根据农作物名称进行分组
        Map<String, List<SysNewRuleEntity>> collect = sysNewRuleEntities.stream().collect(Collectors.groupingBy(SysNewRuleEntity::getCrops));

        collect.keySet().forEach(crops -> {

            List<SysNewRuleEntity> sysNewRuleEntities1 = collect.get(crops);
            sysNewRuleEntities1.forEach(newRuleEntity1 -> {
                String growthcycle = newRuleEntity1.getGrowthcycle();//获取生长周期就是日期范围 （比如：2020-01-03，2020-01-04）
                String[] split = growthcycle.split(",");
                try {
                    boolean flag = hourMinuteBetween(split[0], split[1]);//判断当前传感器的时间是否在这段时间内也就是说是否在当前生长周期内
                    if (flag) {
                        warn(newRuleEntity1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    /**
     * @param rule 规则库的数据
     * @throws IllegalAccessException
     */
    public void warn(SysNewRuleEntity rule) throws IllegalAccessException {

        StringBuilder sb = new StringBuilder();
        List<SysSiteEntity> sites = siteMapper.selectAll();
        for (SysSiteEntity site : sites) {

            Map<String, Object> current = SensorRecService.current(site.getId());
            //fixme 土壤用20cm深度的数据
            SysDustRecEntity dust;
            Object o = dustRecService.current(site.getId()).get("20cm");
            if ("-".equals(o)) {
                continue;
            }
            dust = (SysDustRecEntity) o;

            //预处理
            current.put("windSpeed", current.get(SensorType.wind_speed.name()));
            current.put("windDirection", current.get(SensorType.wind_direction.name()));
            current.put("wcDust", dust.getWc());
            current.put("temperatureDust", dust.getTemperature());
            current.put("ecDust", dust.getEc());
            current.put("salinityDust", dust.getSalinity());
            current.put("tdsDust", dust.getTds());
            current.put("epsilonDust", dust.getEpsilon());

            Field fields[] = rule.getClass().getDeclaredFields();
            boolean shouldWarn = false;
            for (Field field : fields) {
                String fieldName = field.getName();
                //排除掉不需要比对的属性
                if ("crops".equals(fieldName) ||
                        "growthcycleType".equals(fieldName) ||
                        "warn".equals(fieldName) ||
                        "control".equals(fieldName) ||
                        "id".equals(fieldName) ||
                        "serialVersionUID".equals(fieldName)) {
                    continue;
                }

                Object ruleStr = field.get(rule);
                if (ruleStr == null || !ruleStr.toString().contains(",")) {
                    continue;
                }
                String[] split = ruleStr.toString().split(",");
                String value = current.get(fieldName).toString();
                if ("-".equals(value) || "".equals(value)) {
                    break;
                }
                if (Double.parseDouble(value) > Double.parseDouble(split[0])
                        && Double.parseDouble(value) < Double.parseDouble(split[1])) {
                    break;
                }
                shouldWarn = true;
            }
            if (shouldWarn) {
                sb.append(site.getName())
                        .append(",");
            }
        }

        if (sb.length() != 0) {
            sb.insert(0, rule.getWarn())
                    .insert(0, "(")
                    .replace(sb.length() - 1, sb.length() - 1, ")");
            weXinUtils.warnPush(rule.getCrops(), sb.toString(), rule.getGrowthcycle(), rule.getControl());
        }
    }

    /**
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return true在时间段内，false不在时间段内
     * @throws Exception
     */

    public static boolean hourMinuteBetween(String startDate, String endDate) throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start = format.parse(startDate);
        Date end = format.parse(endDate);

        long nowTime = System.currentTimeMillis();
        long startTime = start.getTime();
        long endTime = end.getTime();
        return nowTime >= startTime && nowTime <= endTime;

    }


    @Override
    public List<Map<String, Object>> earlyData(Integer type) {

        List<Map<String, Object>> list = new ArrayList();
        if (type == 3) {  //3-气象
            Map resultMap = new HashMap();
            resultMap.put("wind_speed", "风速");
            resultMap.put("wind_direction", "风向");
            resultMap.put("humidity", "湿度");
            resultMap.put("temperature", "温度");
            resultMap.put("noise", "噪音");
            resultMap.put("PM25", "PM25");
            resultMap.put("PM10", "PM10");
            resultMap.put("atmos", "大气压");
            list.add(resultMap);
        } else if (type == 4) {
            Map resultMap = new HashMap();
            resultMap.put("deep1", "10cm");
            resultMap.put("deep2", "20cm");
            resultMap.put("deep3", "40cm");
            list.add(resultMap);
            Map resultMap1 = new HashMap();
            resultMap1.put("wc", "含水率");
            resultMap1.put("temperature", "温度");
            resultMap1.put("ec", "导电率");
            resultMap1.put("salinity", "盐度");
            resultMap1.put("tds", "总溶解固体");
            resultMap1.put("epsilon", "介电常数");
            list.add(resultMap1);
        } else {
            return null;
        }
        return list;
    }

    @Override
    public void earlyInfoUpdate(WarnInfoParam param) {

        SysWarnInfoEntity sysWarnInfoEntity = warnInfoMapper.selectById(param.getId());
        if (ObjectUtils.isEmpty(sysWarnInfoEntity)) {
            throw new MyException(ResultStatusCode.DB_ERR);
        }
        SysWarnInfoEntity warnInfoEntity = new SysWarnInfoEntity();
        warnInfoEntity.setId(param.getId());
        warnInfoEntity.setSiteId(param.getSiteId());
        warnInfoEntity.setTime(param.getTime());
        warnInfoEntity.setEarlyType(param.getEarlyType());
        warnInfoEntity.setEarlyName(param.getEarlyName());
        if (param.getEarlyDepth() != null) {
            warnInfoEntity.setEarlyDepth(param.getEarlyDepth());
        }
        warnInfoEntity.setEarlyDegree(param.getEarlyDegree());
        warnInfoEntity.setEarlyContent(param.getEarlyContent());
        warnInfoEntity.setUpdateTime(new Date());
        warnInfoMapper.updateById(warnInfoEntity);
    }

    @Autowired
    private SysWarnDictionariesMapper dictionariesMapper;

    @Override
    public Map<String, List<SysWarnDictionariesEntity>> warnDictionaries() {
        List<SysWarnDictionariesEntity> list = dictionariesMapper.selectList(null);
        Map<String, List<SysWarnDictionariesEntity>> collect = list.stream().collect(Collectors.groupingBy(SysWarnDictionariesEntity::getType));
        return collect;
    }

    @Override
    public void newRule(SysNewRuleEntity param) {
        newRuleMapper.insert(param);
    }

    @Override
    public void alertRule(SysNewRuleEntity param) {
        SysNewRuleEntity sysNewRuleEntity = newRuleMapper.selectById(param.getId());
        if (ObjectUtils.isEmpty(sysNewRuleEntity)) {
            throw new MyException(ResultStatusCode.BAD_REQUEST);
        }
        newRuleMapper.updateById(param);
    }

    @Override
    public PageResult<SysNewRuleEntity> ruleInfo(PageParam param) {
        IPage<SysNewRuleEntity> sysNewRuleEntityIPage = newRuleMapper.selectPage(new Page<>(param.getPageNumber(), param.getPageSize()), null);
        Long total = sysNewRuleEntityIPage.getTotal();
        return PageResult.<SysNewRuleEntity>builder()
                .total(total.intValue())
                .pageData(sysNewRuleEntityIPage.getRecords())
                .pageNumber(param.getPageNumber())
                .pageSize(param.getPageSize())
                .build();
    }

    /**
     * 此接口用于GIS预警
     *
     * @return
     */
    public void earlyInfoGis() {

        List<SysWarnInfoEntity> warnInfoEntities = warnInfoMapper
                .selectList(Wrappers.<SysWarnInfoEntity>lambdaQuery()
                        .eq(SysWarnInfoEntity::getEarlyType, "虫情")
                        .eq(SysWarnInfoEntity::getIsClosed, 2)
                        .eq(SysWarnInfoEntity::getIsExamine, 1));
        if (CollectionUtils.isNotEmpty(warnInfoEntities)) {
            List<EarlyInfoVo> earlyInfoVos = new ArrayList<>();
            warnInfoEntities.stream().forEach(sysWarnInfoEntity -> {
                EarlyInfoVo earlyInfoVo = new EarlyInfoVo();
                earlyInfoVo.setTime(sysWarnInfoEntity.getTime());
                earlyInfoVo.setEarlyName(sysWarnInfoEntity.getEarlyName());
                earlyInfoVo.setEarlyContent(sysWarnInfoEntity.getEarlyContent());
                SysPreventiveMeasuresEntity measuresEntity = measuresMapper.selectOne(Wrappers.<SysPreventiveMeasuresEntity>lambdaQuery()
                        .eq(SysPreventiveMeasuresEntity::getSeason, sysWarnInfoEntity.getEarlyName()));
                if (ObjectUtils.isNotEmpty(measuresEntity)) {
                    earlyInfoVo.setMasures(measuresEntity.getMeasuresInfo());
                }
                earlyInfoVo.setReportUser(sysWarnInfoEntity.getReportUser());
                earlyInfoVo.setCoordinate(sysWarnInfoEntity.getCoordinate());
                earlyInfoVos.add(earlyInfoVo);
            });
            String url = gisConfig.getUrl();
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
            paramMap.add("data", earlyInfoVos);
            restTemplate.postForObject(url, paramMap, String.class);
        }
    }
}
