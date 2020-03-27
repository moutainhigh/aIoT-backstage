package com.aiot.aiotbackstage.server.schedule;

import com.aiot.aiotbackstage.common.enums.DeviceType;
import com.aiot.aiotbackstage.common.sdk.DahuaSDK;
import com.aiot.aiotbackstage.common.util.RedisUtils;
import com.aiot.aiotbackstage.mapper.SysCameraMapper;
import com.aiot.aiotbackstage.mapper.SysSiteMapper;
import com.aiot.aiotbackstage.model.entity.SysCameraEntity;
import com.aiot.aiotbackstage.model.entity.SysSiteEntity;
import com.aiot.aiotbackstage.server.TcpServer;
import com.aiot.aiotbackstage.service.ISysDeviceErrorRecService;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 定时确认硬件设备连接状态
 *
 * @author Avernus
 */
@Component
public class DeviceStatusReadSchedule {

    @Resource
    private SysSiteMapper sysSiteMapper;
    @Resource
    private SysCameraMapper sysCameraMapper;
    @Resource
    private ISysDeviceErrorRecService sysDeviceErrorRecService;
    @Resource
    private DahuaSDK sdk;
    @Resource
    private RedisUtils redisUtil;
    @Value("${server.tcp.enable}")
    private boolean enable;

    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void read() throws InterruptedException {
        String key = "SYNC-LOCK:DEVICE-STATUS";
        try {
            if (enable & redisUtil.get(key) == null) {
                if (redisUtil.setScheduler(key, key)) {
                    //通过rtu通道集合检测rtu连接状态
                    List<SysSiteEntity> sites = sysSiteMapper.selectAll();
                    for (SysSiteEntity site : sites) {
                        boolean online = false;
                        for (Integer integer : TcpServer.CHANNELS.keySet()) {
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

                    //检测摄像头连接状态
                    List<SysCameraEntity> cameras = sysCameraMapper.selectAll();
                    for (SysCameraEntity camera : cameras) {
                        boolean online = false;
                        for (Object name : sdk.cameraChannel().keySet()) {
                            if (camera.getName().equals(name)) {
                                online = true;
                                break;
                            }
                        }
                        if (online) {
                            sysDeviceErrorRecService.refreshRec(camera.getSiteId(), DeviceType.CAMERA.name(), camera.getType());
                        } else {
                            sysDeviceErrorRecService.newRec(camera.getSiteId(), DeviceType.CAMERA.name(), camera.getType());
                        }
                    }
                    //TODO 检测测报灯连接状态

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisUtil.del(key);
        }
    }
}
