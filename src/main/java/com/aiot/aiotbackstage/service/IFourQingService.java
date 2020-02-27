package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysInsectRecReportEntity;
import com.aiot.aiotbackstage.model.param.GetInsectRecReportParam;
import com.aiot.aiotbackstage.model.param.InsectRecReportParam;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.model.vo.SysSensorRecVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFourQingService {

    List<SysSensorRecVo> meteorological(Long stationId);

    void insectRecReport(InsectRecReportParam recReportParam);

    void insectRecReportModify(InsectRecReportParam recReportParam);

    PageResult<SysInsectRecReportEntity> insectRecReportGet(GetInsectRecReportParam recReportParam);

    String pestUpload(MultipartFile multipartFile);

    void examine(Long id);
}
