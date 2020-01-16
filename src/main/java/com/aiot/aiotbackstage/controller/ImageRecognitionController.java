package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.service.IImageRecognitionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description 形色管理
 * @Author xiaowenhui
 * @CreateTime 2020/1/7 16:52
 */
@Controller
@RequestMapping("xingse")
@Api(tags = "形色管理接口", description = "User Controller")
@Slf4j
public class ImageRecognitionController {

    @Autowired
    private IImageRecognitionService iImageRecognitionService;


    @ApiOperation(value = "用户图片识别(形色)", notes = "用户图片识别(形色)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @PostMapping("/fileUpload")
    public Result fileUpload(@RequestParam(value = "file", required = false) MultipartFile multipartFile,
    @RequestHeader String token
    ) {
        Long userId=1L;
        return Result.success(iImageRecognitionService.fileUpload(multipartFile,userId));
    }

}
