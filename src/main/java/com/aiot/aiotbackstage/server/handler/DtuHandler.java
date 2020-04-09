package com.aiot.aiotbackstage.server.handler;

import com.aiot.aiotbackstage.common.constant.Constants;
import com.aiot.aiotbackstage.common.util.HexConvert;
import com.aiot.aiotbackstage.common.util.RedisUtils;
import com.aiot.aiotbackstage.common.util.SpringContextHolder;
import com.aiot.aiotbackstage.model.dto.RtuData;
import com.aiot.aiotbackstage.server.TcpServer;
import com.aiot.aiotbackstage.service.ISensorRecService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 *  * 采用 Modbus-RTU 通讯规约，格式如下：
 *  * 地址码 = 1 字节
 *  * 功能码 = 1 字节
 *  * 数据区长度 = 1字节
 *  * 数据区 = N 字节
 *  * 错误校验 = 16 位 CRC 码
 *  *
 * @author Avernus
 */
@Slf4j
public class DtuHandler extends SimpleChannelInboundHandler<String> {

    private ISensorRecService service;

    private RedisUtils redis;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String hex) throws Exception {
        //转为ASCII
        String asc = HexConvert.convertHexToString(hex.replace(" ",""));

        if (redis == null) {
            redis = SpringContextHolder.getBean(RedisUtils.class);
        }
        if (asc.startsWith("register")) {
            //注册包
            int siteId = Integer.parseInt(asc.split("-")[1]);
            TcpServer.CHANNELS.put(siteId, ctx.channel());
            redis.hset(Constants.RTU_LAST_TIME, siteId + "-00", System.currentTimeMillis());
            log.info("from {}, received hex {}, asc {}",ctx.channel().remoteAddress(), hex, asc);
        } else if (asc.startsWith("ping")) {
            //心跳包 do nothing
            log.info("from {}, received {}", ctx.channel().remoteAddress(), asc);
        } else {
            //数据包
            Integer rtu = 0;
            for (Map.Entry<Integer, Channel> entry : TcpServer.CHANNELS.entrySet()) {
                if (entry.getValue().equals(ctx.channel())) {
                    rtu = entry.getKey();
                }
            }
            if (rtu == 0) {
                return;
            }
            String[] hexs = hex.split(" ");
            int[] datum = new int[hexs.length];
            for (int i = 0; i < hexs.length; i++) {
                datum[i] = Integer.parseInt(hexs[i], 16);
            }

            int addr = datum[0];
            int func = datum[1];
            int dataAreaLength = datum[2];
            int[] values = new int[dataAreaLength / 2];
            for (int i = 0; i < dataAreaLength; i++) {
                if (i % 2 != 0) {
                    continue;
                }
                values[i / 2] = Integer.parseInt(hexs[i + 3] + hexs[i + 4], 16);
            }

            RtuData rtuData = new RtuData(rtu, addr, func, dataAreaLength, values, datum[datum.length - 2], datum[datum.length - 1]);
            log.info("from {}--{}, received data {}", ctx.channel().remoteAddress(), rtu, rtuData);

            if (service == null) {
                service = SpringContextHolder.getBean(ISensorRecService.class);
            }
            service.receive(rtuData);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        if (event instanceof IdleStateEvent) {
            IdleStateEvent evt = (IdleStateEvent) event;
            //未收到客户端信息（包含心跳）
            if (evt.state() == IdleState.READER_IDLE) {
                for (Map.Entry<Integer, Channel> entry : TcpServer.CHANNELS.entrySet()) {
                    if (entry.getValue().equals(ctx.channel())) {
                        TcpServer.CHANNELS.remove(entry.getKey());
                        log.info("channels of rtu-{} removed -read idle", entry.getKey());
                        break;
                    }
                }
                ctx.channel().close();
            } else if (evt.state() == IdleState.WRITER_IDLE) {

            } else if (evt.state() == IdleState.ALL_IDLE) {

            }
        } else {
            super.userEventTriggered(ctx, event);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        for (Map.Entry<Integer, Channel> entry : TcpServer.CHANNELS.entrySet()) {
            if (entry.getValue().equals(ctx.channel())) {
                TcpServer.CHANNELS.remove(entry.getKey());
                log.info("channels of rtu-{} removed", entry.getKey());
                break;
            }
        }

        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        for (Map.Entry<Integer, Channel> entry : TcpServer.CHANNELS.entrySet()) {
            if (entry.getValue().equals(ctx.channel())) {
                TcpServer.CHANNELS.remove(entry.getKey());
                log.info("channels of rtu-{} removed - exception", entry.getKey());
                break;
            }
        }

        super.exceptionCaught(ctx, cause);
    }
}
