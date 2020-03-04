package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysWarnInfoEntity;
import com.aiot.aiotbackstage.model.entity.SysWarnRuleEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.param.WarnInfoParam;
import com.aiot.aiotbackstage.model.param.WarnRuleParam;
import com.aiot.aiotbackstage.model.vo.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/24 10:37
 */
public interface IEarlyWarningService {

    void earlyInfoAdd(WarnRuleParam param);

    void earlyInfoModify(WarnRuleParam param);

    void earlyInfoDelete(Long id);

    PageResult<SysWarnRuleEntity> earlyInfoPage(String earlyType, PageParam pageParam);

    void earlyInfoReport(WarnInfoParam param, String token);

    PageResult<SysWarnInfoEntity> earlyInfoReportPage(PageParam pageParam);

    void earlyClosed(Long id);

    void earlyInfoExamine(Long id);

    Map<String,Object> earlyContent(String earlyType,String earlyDegree);

    Integer earlyCount();

    /**
     * 自动预警
     */
    void earlyWarningReport(String type, String typeName, String depth, String value, Integer siteId);

    List<Map<String,Object>> earlyData(Integer type);

    void earlyInfoUpdate(WarnInfoParam param);
}
