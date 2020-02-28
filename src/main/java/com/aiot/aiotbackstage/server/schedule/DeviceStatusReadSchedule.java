package com.aiot.aiotbackstage.server.schedule;

import com.aiot.aiotbackstage.common.enums.DeviceType;
import com.aiot.aiotbackstage.mapper.SysSiteMapper;
import com.aiot.aiotbackstage.model.entity.SysSiteEntity;
import com.aiot.aiotbackstage.server.TcpServer;
import com.aiot.aiotbackstage.service.ISysDeviceErrorRecService;
import io.netty.channel.Channel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 定时确认硬件设备连接状态
 * @author Avernus
 */
@Component
public class DeviceStatusReadSchedule {

    @Resource
    private SysSiteMapper sysSiteMapper;
    @Resource
    private ISysDeviceErrorRecService sysDeviceErrorRecService;
    /**
     *
     */
    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void read() throws InterruptedException {
        //通过rtu通道集合检测rtu连接状态
        List<SysSiteEntity> sites = sysSiteMapper.selectAll();
        Map<Integer, Channel> channels = TcpServer.channels;
        for (SysSiteEntity site : sites) {
            boolean online = false;
            for (Integer integer : channels.keySet()) {
                if (site.getId().equals(integer)) {
                    online = true;
                    break;
                }
            }
            if (online) {
                sysDeviceErrorRecService.refreshRec(site.getId(), DeviceType.RTU.name(), "zhan-wei");
            } else {
                sysDeviceErrorRecService.newRec(site.getId(), DeviceType.RTU.name(), "zhan-wei");
            }
        }

        //TODO 检测摄像头连接状态

        //TODO 检测测报灯连接状态

    }
}
