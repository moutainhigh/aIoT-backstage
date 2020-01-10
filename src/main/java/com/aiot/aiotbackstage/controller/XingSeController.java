package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.config.ObsConfig;
import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.alibaba.fastjson.JSONObject;
import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @Description 形色管理
 * @Author xiaowenhui
 * @CreateTime 2020/1/7 16:52
 */
@Controller
@RequestMapping("xingse")
@Api(tags = "形色管理接口", description = "User Controller")
@Slf4j
public class XingSeController {

//    @Autowired
//    private IXingSeService iXingSeService;

    @Autowired
    private ObsConfig obsConfig;

    @ApiOperation(value = "用户图片识别(形色)", notes = "用户图片识别(形色)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @PostMapping("/fileUpload")
    public Result fileUpload(@RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        try {
            // 创建ObsClient实例
            ObsClient obsClient = new ObsClient(obsConfig.getAk(), obsConfig.getSk(), obsConfig.getEndPoint());
            // 上传文件，注意：上传内容大小不能超过5GB
            String objectKey = multipartFile.getOriginalFilename();
            InputStream inputStream = multipartFile.getInputStream();
            PutObjectResult putObjectResult = obsClient.putObject(obsConfig.getBucketName(), objectKey, inputStream);
            String url = "https://" + putObjectResult.getObjectUrl();
            //TODO  DB处理
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", objectKey);
            jsonObject.put("url", url);
            //TODO 调用形色
            inputStream.close();
            obsClient.close();
            return Result.success(jsonObject);
        } catch (Exception e) {
            log.error("{}文件上传失败！", multipartFile.getOriginalFilename());
            return Result.error(ResultStatusCode.FILE_ERR);
        }
    }

}
