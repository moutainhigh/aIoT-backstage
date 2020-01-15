package com.aiot.aiotbackstage.server.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * 采用 Modbus-RTU 通讯规约，格式如下：
 * 地址码 = 1 字节
 * 功能码 = 1 字节
 * 数据区长度 = 1字节
 * 数据区 = N 字节
 * 错误校验 = 16 位 CRC 码
 *
 * @author Avernus
 */
public class ModbusRtuCodec extends ByteToMessageCodec<int[]> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //问询帧
        ByteBuf req = in.readBytes(8);
        int addr = in.readByte();
        int func = in.readByte();
        int length = in.readByte();
        //数据区
        ByteBuf datum = in.readBytes(length);
        int[] data = new int[length];
        for (int i = 0; i < length; i++) {
            data[i] = datum.readByte();
        }
        //校验码
        ByteBuf CRC = in.readBytes(2);
        out.add(data);
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, int[] msg, ByteBuf out) throws Exception {
        for (int i : msg) {
            out.writeByte(i);
        }
    }

}
