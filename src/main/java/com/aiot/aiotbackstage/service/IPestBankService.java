package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysPestBankEntity;
import com.aiot.aiotbackstage.model.param.AddPestBankParam;
import com.aiot.aiotbackstage.model.param.ModifyPestBankParam;
import com.aiot.aiotbackstage.model.param.PestBankParam;
import com.aiot.aiotbackstage.model.vo.PageResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description 害虫库
 * @Author xiaowenhui
 * @CreateTime 2020/2/18 9:47
 */
public interface IPestBankService {
    PageResult<SysPestBankEntity> pestInfo(PestBankParam param);

    void addPestInfo(AddPestBankParam param);

    void modifyPestInfo(ModifyPestBankParam param);

    void importWatchExcel(MultipartFile xlsFile);

    void exportExcel(String pestName, HttpServletRequest request, HttpServletResponse response) throws IOException;

    void deletePestInfo(Long id);
}
