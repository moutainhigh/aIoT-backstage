package com.aiot.aiotbackstage.server.schedule;

import com.aiot.aiotbackstage.common.util.RedisUtils;
import com.aiot.aiotbackstage.service.IEarlyWarningService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Avernus
 * 专家库预警定时任务
 */
@Component
public class WarnSchedule {

    @Resource
    private RedisUtils redisUtil;
    @Resource
    private IEarlyWarningService earlyWarningService;
    @Value("${server.tcp.enable}")
    private boolean enable;

    @Scheduled(cron = "0 0 0/12 * * ?")
    public void read() {
        String key = "SYNC-LOCK:WARN";
        try {
            if (enable & redisUtil.get(key) == null) {
                if (redisUtil.setScheduler(key, Thread.currentThread().getId())) {
                    earlyWarningService.earlyWarningReportNew();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisUtil.del(key);
        }
    }
}
