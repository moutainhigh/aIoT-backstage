package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.mapper.SysSiteMapper;
import com.aiot.aiotbackstage.mapper.SysWarnInfoMapper;
import com.aiot.aiotbackstage.mapper.SysWarnRuleMapper;
import com.aiot.aiotbackstage.model.entity.SysSiteEntity;
import com.aiot.aiotbackstage.model.entity.SysWarnInfoEntity;
import com.aiot.aiotbackstage.model.entity.SysWarnRuleEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.param.WarnInfoParam;
import com.aiot.aiotbackstage.model.param.WarnRuleParam;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.service.IEarlyWarningService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/24 10:37
 */
@Service
public class EarlyWarningServiceImpl implements IEarlyWarningService {

    @Autowired
    private SysWarnRuleMapper warnRuleMapper;

    @Autowired
    private SysWarnInfoMapper warnInfoMapper;

    @Autowired
    private SysSiteMapper siteMapper;




    @Override
    public void earlyInfoAdd(WarnRuleParam param) {
        SysWarnRuleEntity warnRuleEntity=new SysWarnRuleEntity();
        warnRuleEntity.setEarlyType(param.getEarlyType());
        warnRuleEntity.setEarlyName(param.getEarlyName());
        warnRuleEntity.setEarlyDepth(param.getEarlyDepth());
        warnRuleEntity.setEarlyThreshold(param.getEarlyThreshold());
        warnRuleEntity.setEarlyDegree(param.getEarlyDegree());
        warnRuleEntity.setEarlyContent(param.getEarlyContent());
        warnRuleEntity.setCreateTime(new Date());
        warnRuleEntity.setUpdateTime(new Date());
        warnRuleMapper.insert(warnRuleEntity);
    }


    @Override
    public void earlyInfoModify(WarnRuleParam param) {

        SysWarnRuleEntity warnRuleEntity1 = warnRuleMapper.selectById(param.getId());
        if(ObjectUtils.isEmpty(warnRuleEntity1)){
            throw new MyException(ResultStatusCode.EARLY_WARNING_NO_EXIT);
        }
        SysWarnRuleEntity warnRuleEntity=new SysWarnRuleEntity();
        warnRuleEntity.setId(param.getId());
        warnRuleEntity.setEarlyType(param.getEarlyType());
        warnRuleEntity.setEarlyName(param.getEarlyName());
        warnRuleEntity.setEarlyDepth(param.getEarlyDepth());
        warnRuleEntity.setEarlyThreshold(param.getEarlyThreshold());
        warnRuleEntity.setEarlyDegree(param.getEarlyDegree());
        warnRuleEntity.setEarlyContent(param.getEarlyContent());
        warnRuleEntity.setCreateTime(new Date());
        warnRuleEntity.setUpdateTime(new Date());
        warnRuleMapper.updateById(warnRuleEntity);
    }

    @Override
    public void earlyInfoDelete(Long id) {
        SysWarnRuleEntity warnRuleEntity1 = warnRuleMapper.selectById(id);
        if(ObjectUtils.isEmpty(warnRuleEntity1)){
            throw new MyException(ResultStatusCode.EARLY_WARNING_NO_EXIT);
        }
        warnRuleMapper.deleteById(id);
    }

    @Override
    public PageResult<SysWarnRuleEntity> earlyInfoPage(String earlyType, PageParam pageParam) {

        Integer total;
        List<SysWarnRuleEntity> ruleEntityList;
        if(earlyType == null || "".equals(earlyType)){
            total = warnRuleMapper.selectCount(null);
            ruleEntityList= warnRuleMapper.warnRulePage(null,pageParam);
        }else{
            total = warnRuleMapper.selectCount(Wrappers.<SysWarnRuleEntity>lambdaQuery().eq(SysWarnRuleEntity::getEarlyType,earlyType));
            ruleEntityList= warnRuleMapper.warnRulePage(earlyType,pageParam);
        }
        if(CollectionUtils.isEmpty(ruleEntityList)){
            throw new MyException(ResultStatusCode.EARLY_WARNING_NO_EXIT);
        }
        return PageResult.<SysWarnRuleEntity>builder()
                .total(total)
                .pageData(ruleEntityList)
                .pageNumber(pageParam.getPageNumber())
                .pageSize(pageParam.getPageSize())
                .build();
    }

    @Override
    public void earlyInfoReport(WarnInfoParam param) {
        SysWarnInfoEntity warnInfoEntity=new SysWarnInfoEntity();
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
        warnInfoEntity.setCreateTime(new Date());
        warnInfoEntity.setUpdateTime(new Date());
        warnInfoMapper.insert(warnInfoEntity);
    }

    @Override
    public PageResult<SysWarnInfoEntity> earlyInfoReportPage(PageParam pageParam) {
        Integer total = warnInfoMapper.selectCount(null);
        List<SysWarnInfoEntity> sysWarnInfoEntities = warnInfoMapper.warnInfoPage(pageParam);
        if(CollectionUtils.isEmpty(sysWarnInfoEntities)){
            throw new MyException(ResultStatusCode.EARLY_WARNING_NO_EXIT);
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
        if(ObjectUtils.isEmpty(warnInfoEntity)){
            throw new MyException(ResultStatusCode.EARLY_WARNING_ID_IS_EXIT);
        }
        warnInfoEntity.setIsClosed(1);
        warnInfoMapper.updateById(warnInfoEntity);
    }

    @Override
    public void earlyInfoExamine(Long id) {
        SysWarnInfoEntity warnInfoEntity = warnInfoMapper.selectById(id);
        if(ObjectUtils.isEmpty(warnInfoEntity)){
            throw new MyException(ResultStatusCode.EARLY_WARNING_ID_IS_EXIT);
        }
        warnInfoEntity.setIsExamine(1);
        warnInfoMapper.updateById(warnInfoEntity);
    }

    @Override
    public String earlyContent(String earlyType, String earlyDegree,String earlyName) {
        SysWarnRuleEntity warnRuleEntity = warnRuleMapper
                .selectOne(Wrappers.<SysWarnRuleEntity>lambdaQuery()
                        .eq(SysWarnRuleEntity::getEarlyType, earlyType)
                        .eq(SysWarnRuleEntity::getEarlyName, earlyName)
                        .eq(SysWarnRuleEntity::getEarlyDegree, earlyDegree));
        if(ObjectUtils.isEmpty(warnRuleEntity)){
            throw new MyException(ResultStatusCode.EARLY_WARNING_NO_EXIT);
        }
        return warnRuleEntity.getEarlyContent();
    }

    @Override
    public Integer earlyCount() {
        return warnInfoMapper.selectCount(Wrappers.<SysWarnInfoEntity>lambdaQuery()
                .eq(SysWarnInfoEntity::getIsClosed,2)
                .eq(SysWarnInfoEntity::getIsExamine,1));
    }

    /**
     * 自动预警
     * @param type  预警类型  苗情  灾情  虫情  土壤
     * @param typeName 预警名称  苗情  灾情  虫情  土壤  对应的属性名称
     * @param depth  土壤深度10cm 20cm 40cm
     * @param value 属性对应的值
     * @param siteId 站点名称
     */
    @Override
    public void earlyWarningReport(String type,String typeName,String depth,String value,Integer siteId) {

        SysWarnRuleEntity warnRuleEntity;
            //如果土壤深度不为空则根据土壤深度查询
            if(depth != null){
                warnRuleEntity = warnRuleMapper
                        .selectOne(Wrappers.<SysWarnRuleEntity>lambdaQuery()
                                .eq(SysWarnRuleEntity::getEarlyType, type)
                                .eq(SysWarnRuleEntity::getEarlyName,typeName)
                                .eq(SysWarnRuleEntity::getEarlyDepth,depth));
            }else{
                warnRuleEntity= warnRuleMapper
                        .selectOne(Wrappers.<SysWarnRuleEntity>lambdaQuery()
                                .eq(SysWarnRuleEntity::getEarlyType, type)
                                .eq(SysWarnRuleEntity::getEarlyName,typeName));
            }
        if (ObjectUtils.isNotEmpty(warnRuleEntity)) {
            String earlyThreshold = warnRuleEntity.getEarlyThreshold();
            Double v = Double.parseDouble(value);
            Double v1 = Double.parseDouble(earlyThreshold);
            int i = v.compareTo(v1);
            if (i > 0) {
                SysWarnInfoEntity warnInfoEntity=new SysWarnInfoEntity();
                warnInfoEntity.setSiteId(siteId);
                SysSiteEntity sysSiteEntity = siteMapper.selectById(siteId);
                warnInfoEntity.setSiteName(sysSiteEntity.getName());
                warnInfoEntity.setIsClosed(2);
                warnInfoEntity.setIsExamine(1);
                warnInfoEntity.setTime(new Date());
                warnInfoEntity.setEarlyType(type);
                warnInfoEntity.setEarlyName(warnRuleEntity.getEarlyName());
                if(warnRuleEntity.getEarlyDepth() != null){
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
}
