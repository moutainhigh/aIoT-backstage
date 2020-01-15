package com.aiot.aiotbackstage.common.util;

import com.aiot.aiotbackstage.common.config.AliyunConfig;
import com.aiot.aiotbackstage.common.config.ObsConfig;
import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.alibaba.fastjson.JSONObject;
import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 文件上传到OBS和调用阿里云形色接口
 * @Author xiaowenhui
 * @CreateTime 2020/1/15 13:57
 */
@Slf4j
@Component
public class FileUploadUtils {

    @Autowired
    private ObsConfig obsConfig;

    @Autowired
    private AliyunConfig aliyunConfig;

    /**
     * 上传文件到OBS
     * @param multipartFile  待上传的文件
     * @return  返回文件名称和对象
     */
    public JSONObject obsFileUpload(MultipartFile multipartFile){
        try {
            // 创建ObsClient实例
            ObsClient obsClient = new ObsClient(obsConfig.getAk(), obsConfig.getSk(), obsConfig.getEndPoint());
            // 上传文件，注意：上传内容大小不能超过5GB
            String objectKey = multipartFile.getOriginalFilename();
            InputStream inputStream = multipartFile.getInputStream();
            PutObjectResult putObjectResult = obsClient.putObject(obsConfig.getBucketName(), objectKey, inputStream);
            String url = "https://" + putObjectResult.getObjectUrl();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", objectKey);
            jsonObject.put("url", url);
            inputStream.close();
            obsClient.close();
            return jsonObject;
        } catch (Exception e) {
            log.error("{}文件上传失败！", multipartFile.getOriginalFilename());
            return null;
        }
    }

    /**
     * 查询相似的图片
     * @param url 需要寻找相似的原图片
     * @return 返回相似的图片
     */
    public List<Map<String,Object>> imageRecognition(String url) {
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + aliyunConfig.getAppCode());
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", aliyunConfig.getContentType());
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
//        bodys.put("image_data", "image_data");
        bodys.put("image_url", url);
        try {
            HttpResponse response = HttpUtils.doPost(
                    aliyunConfig.getHost(),
                    aliyunConfig.getPath(),
                    aliyunConfig.getMethod(), headers, querys, bodys);
            //解析数据s
            JSONObject imageJson = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            Map responseMap = (Map<String,Object>)imageJson.get("response");
            return (List<Map<String,Object>>) responseMap.get("identify_results");
        } catch (Exception e) {
            log.error("{}调用形色失败！", e.getMessage());
            throw new MyException(ResultStatusCode.XINGSE_POST_ERR);
        }
    }

}
