package com.aiot.aiotbackstage.server.codec;

import com.aiot.aiotbackstage.model.dto.RtuData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
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
@Slf4j
public class ModbusRtuCodec extends ByteToMessageCodec<int[]> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        log.info("from {}, received {}",ctx.channel().localAddress(), Arrays.toString(bytes));
        int addr = bytes[0];
        int func = bytes[1];
        int length = bytes[2];
        int[] data = new int[length];
        for (int i = 0; i < data.length; i++) {
            data[i] = bytes[3 + i];
        }
        RtuData rtuData = new RtuData(addr, func, length, data);
        out.add(rtuData);
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, int[] msg, ByteBuf out) throws Exception {
        for (int i : msg) {
            out.writeByte(i);
        }
    }

}
