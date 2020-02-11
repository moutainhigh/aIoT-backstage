package com.aiot.aiotbackstage.server.handler;

import com.aiot.aiotbackstage.model.dto.RtuData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


/**
 * @author Avernus
 */
@Slf4j
public class DtuHandler extends SimpleChannelInboundHandler<RtuData> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RtuData msg) throws Exception {
        log.info("tcp server received : {}", msg.toString());
    }

}
