package com.aiot.aiotbackstage.server;

import com.aiot.aiotbackstage.common.util.RedisUtils;
import com.aiot.aiotbackstage.server.codec.ModbusRtuCodec;
import com.aiot.aiotbackstage.server.handler.DtuHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Avernus
 */
@Data
@Slf4j
@Component
public class TcpServer {

    @Value("${server.tcp.port}")
    private int port;
    @Resource
    private RedisUtils redisUtil;

    public static String channelsKey = "RTU-CHANNEL";

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            ChannelFuture f = bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new ModbusRtuCodec())
                                    .addLast(new DtuHandler());
                        }
                    })
                    .bind(new InetSocketAddress(port))
                    .sync();
            log.info("tcp server started at {}", port);
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public Map<Object, Object> rtuChannels() {
        return redisUtil.hmget(channelsKey);
    }

    public void putChannel(int item, Channel channel) {
        redisUtil.hset(channelsKey, item + "", channel);
    }
}
