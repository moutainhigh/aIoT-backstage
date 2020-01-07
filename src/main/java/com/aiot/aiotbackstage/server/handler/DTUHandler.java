package com.aiot.aiotbackstage.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


/**
 * @author Avernus
 */
@Slf4j
public class DTUHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.error("tcp received : {}", msg);
        ctx.channel().writeAndFlush("echo : " + msg);
    }
}
