package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 景区监测
 * @Author xiaowenhui
 * @CreateTime 2020/2/8 16:52
 */
@Controller
@RequestMapping("spot")
@Api(tags = "景区监测API", description = "ScenicSpot Controller")
@Slf4j
@CrossOrigin
public class ScenicSpotController {


    @ApiOperation(value = "景区监测接口(人流量)", notes = "景区监测接口(人流量)")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @GetMapping("/traffic")
    public Result traffic(
    ) {
        List<Map> list=new ArrayList();
        Map map = new HashMap();
        map.put("startTime","2019-03-01 12:33:10");
        map.put("endTime","2019-03-02 15:00:06");
        map.put("count","120");
        list.add(map);

        Map map1 = new HashMap();
        map1.put("startTime","2019-03-01 12:33:10");
        map1.put("endTime","2019-03-02 15:00:06");
        map1.put("count","120");
        list.add(map1);

        Map map2 = new HashMap();
        map2.put("startTime","2019-03-01 12:33:10");
        map2.put("endTime","2019-03-02 15:00:06");
        map2.put("count","1110");
        list.add(map2);

        Map map3 = new HashMap();
        map3.put("startTime","2019-03-01 12:33:10");
        map3.put("endTime","2019-03-02 15:00:06");
        map3.put("count","1230");
        list.add(map3);
        return Result.success(list);
    }

    @ApiOperation(value = "景区空气监测", notes = "景区空气监测")
    @ApiResponses({
            @ApiResponse(code = 200,message = "成功")
    })
    @ResponseBody
    @GetMapping("/airQuality")
    public Result airQuality(
    ) {
        List<Map> list=new ArrayList();
        Map map = new HashMap();
        map.put("so2",1);
        map.put("no2",5);
        map.put("IP",23.5);
        list.add(map);

        Map map2 = new HashMap();
        map2.put("so2",15);
        map2.put("no2",51);
        map2.put("IP",23.5);
        list.add(map2);

        Map map3 = new HashMap();
        map3.put("so2",11);
        map3.put("no2",5);
        map3.put("IP",23.5);
        list.add(map3);

        Map map4 = new HashMap();
        map4.put("so2",51);
        map4.put("no2",5);
        map4.put("IP",23.5);
        list.add(map4);
        return Result.success(list);
    }

}
