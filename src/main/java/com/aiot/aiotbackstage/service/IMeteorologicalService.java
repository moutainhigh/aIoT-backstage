package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysSceneEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.vo.PageResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IMeteorologicalService {
    void meteorologicalExcelInput(MultipartFile excelFile);

    void meteorologicalExcelOutput(HttpServletRequest request, HttpServletResponse response) throws IOException;

    PageResult<SysSceneEntity> meteorological(PageParam param);
}
