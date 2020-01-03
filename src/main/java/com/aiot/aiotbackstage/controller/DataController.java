package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Avernus
 */
@Slf4j
@RestController
@RequestMapping(value = "/data")
public class DataController {

    @RequestMapping(value = "/insects", method = RequestMethod.POST)
    public Result insects(@RequestBody String data) {
        log.error("time:{}, received:{}",System.currentTimeMillis(), data);
        return Result.success();
    }
}


