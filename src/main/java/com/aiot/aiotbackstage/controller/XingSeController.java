package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.common.fastdfs.FastDFSClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

/**
 * @Description 形色管理
 * @Author xiaowenhui
 * @CreateTime 2020/1/7 16:52
 */
@Controller
@RequestMapping("xingse")
@Api(tags = "形色管理接口", description = "User Controller")
public class XingSeController {

//    @Autowired
//    private IXingSeService iXingSeService;

    @Autowired
    private FastDFSClient fastDFSClient;

    @ApiOperation(value = "用户图片识别(形色)", notes = "用户图片识别(形色)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @PostMapping("/fileUpload")
    public Result picUpload(){
//        iXingSeService.picUpload(picUploadParam);
//        ClassPathResource classPathResource = new ClassPathResource("aiot.jpg");
        File file=new File("/usr/local/aiot.jpg");
//        try {
//            file=ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX+"static/aiot.jpg");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String str = fastDFSClient.uploadFile(file);
        String resAccessUrl = fastDFSClient.getResAccessUrl(str);
        return Result.success(resAccessUrl);
    }

}
