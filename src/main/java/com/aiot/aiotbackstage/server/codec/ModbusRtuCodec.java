package com.aiot.aiotbackstage.server.codec;

import com.aiot.aiotbackstage.common.util.HexConvert;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Avernus
 */
@Slf4j
public class ModbusRtuCodec extends ByteToMessageCodec<String> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        //将字节数组转换为16进制字符串
        String hex = HexConvert.BinaryToHexString(bytes);
        out.add(hex);
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        byte[] bytes = HexConvert.hexStringToBytes(msg);
        out.writeBytes(bytes);
    }

}
