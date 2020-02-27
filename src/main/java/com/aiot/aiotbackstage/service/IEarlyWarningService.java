package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysWarnInfoEntity;
import com.aiot.aiotbackstage.model.entity.SysWarnRuleEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.param.WarnInfoParam;
import com.aiot.aiotbackstage.model.param.WarnRuleParam;
import com.aiot.aiotbackstage.model.vo.PageResult;

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

    void earlyInfoReport(WarnInfoParam param);

    PageResult<SysWarnInfoEntity> earlyInfoReportPage(PageParam pageParam);

    void earlyClosed(Long id);

    void earlyInfoExamine(Long id);

    String earlyContent(String earlyType, String earlyDegree, String earlyName);

    Integer earlyCount();

    /**
     * 自动预警
     */
    void earlyWarningReport(String type, String typeName, String depth, String value, Integer siteId);
}
