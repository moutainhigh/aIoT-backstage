package com.aiot.aiotbackstage.common.util;

import com.aiot.aiotbackstage.common.config.WxMpConfig;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/3/16 15:21
 */
@Slf4j
@Component
public class WeXinUtils {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WxMpConfig wxMpConfig;

    /**
     * 获取access_token接口
     */
    private String accessToken() {
        String urlString = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={appId}&secret={secret}";
        String strResp = restTemplate.getForObject(
                urlString, String.class,
                wxMpConfig.getAppId(),
                wxMpConfig.getSecret());
        JSONObject jsonObject = JSONObject.parseObject(strResp);

        return  (String) jsonObject.get("access_token");
    }

    /**
     * 获取关注者列表接口
     */
    private List<String> userList() {
        String urlString = "https://api.weixin.qq.com/cgi-bin/user/get?access_token={access_token}";
        String strResp = restTemplate.getForObject(
                urlString, String.class,
                accessToken());
        JSONObject jsonObject = JSONObject.parseObject(strResp);
        Map tMap = (Map) jsonObject.get("data");
        List<String> result=(List<String>) tMap.get("openid");
        return result;
    }

    public void push() {
        //1，配置
        WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
        wxStorage.setAppId(wxMpConfig.getAppId());
        wxStorage.setSecret(wxMpConfig.getSecret());
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);
//		String alarmPath = "http://192.168.10.17:9091/"+alarm.getFilePath()+ File.separator+alarm.getPdfName();
        //2,推送消息
        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        templateMessage.setTemplateId(wxMpConfig.getTemplate());
//				.url(alarmPath)//点击模版消息要访问的网址
        List<WxMpTemplateData> wxMpTemplateData = new ArrayList<>();
        wxMpTemplateData.add(new WxMpTemplateData("first", "虫子预警了", "#FF0000"));
        templateMessage.setData(wxMpTemplateData);
        List<String> openList = new ArrayList<>();
        try {
            openList = userList();
            for(String id :openList){
                templateMessage.setToUser(id);//要推送的用户openid
                wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            }
        } catch (Exception e) {
            log.info("推送失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
