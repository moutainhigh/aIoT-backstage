package com.aiot.aiotbackstage.server.handler;

import com.aiot.aiotbackstage.common.util.HexConvert;
import com.aiot.aiotbackstage.common.util.SpringContextHolder;
import com.aiot.aiotbackstage.model.dto.RtuData;
import com.aiot.aiotbackstage.server.TcpServer;
import com.aiot.aiotbackstage.service.ISensorRecService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String hex) throws Exception {
        //转为ASCII
        String asc = HexConvert.convertHexToString(hex.replace(" ",""));

        if (asc.startsWith("register")) {
            //注册包
            TcpServer.addrs.put(ctx.channel().remoteAddress(), Integer.parseInt(asc.split("-")[1]));
            log.info("from {}, received hex {}, asc {}",ctx.channel().remoteAddress(), hex, asc);
        } else {
            //数据包
            String[] hexs = hex.split(" ");
            int[] datum = new int[hexs.length];
            for (int i = 0; i < hexs.length; i ++) {
                datum[i] = Integer.parseInt(hexs[i], 16);
            }

            int addr = datum[0];
            int func = datum[1];
            int dataAreaLength = datum[2];
            int[] values = new int[dataAreaLength / 2];
            for (int i = 0; i < dataAreaLength; i ++) {
                if (i % 2 != 0) {
                    continue;
                }
                values[i / 2] = Integer.parseInt(hexs[i + 3] + hexs[i + 4], 16);
            }

            Integer rtu = TcpServer.addrs.get(ctx.channel().remoteAddress());
            if (rtu == null) {
                return;
            }
            RtuData rtuData = new RtuData(rtu, addr, func, dataAreaLength, values, datum[datum.length -2], datum[datum.length - 1]);
            log.info("from {}, received data {}",rtu, rtuData);

            if (service == null) {
                service = SpringContextHolder.getBean(ISensorRecService.class);
            }
            service.receive(rtuData);
        }
    }

}
