package com.aiot.aiotbackstage.server.schedule;

import com.aiot.aiotbackstage.server.TcpServer;
import io.netty.channel.Channel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Avernus
 */
@Component
public class RtuReadSchedule {

    /**
     * frame问询帧结构
     * *  * 地址码 = 1 字节
     * {@link com.aiot.aiotbackstage.common.enums.RtuAddrCode}
     * *  * 功能码 = 1 字节 问询帧固定为3
     * *  * 起始寄存器地址 = 2 字节
     * *  * 问询数据长度 = 2 字节
     * *  * 错误校验 = 2字节 （16 位 CRC 码）
     * 每5分钟调用一次
     */
    @Scheduled(cron = "* 0/5 * * * ?")
    public void read() throws InterruptedException {
        //风速，从寄存器地址0000开始查询，查询1个寄存器地址
        broadcast("010300000001840A");
        //风向，从寄存器地址0000开始查询，查询2个寄存器地址
        broadcast("020300000002C438");
        //百叶箱，从寄存器地址01F4开始查询，查询6个寄存器地址
        broadcast("030301F400068424");
        //土壤墒情-20CM，从寄存器地址0000开始查询，查询6个寄存器地址
        broadcast("040300000006C59D");
        //土壤墒情-10CM，从寄存器地址0000开始查询，查询6个寄存器地址
        broadcast("050300000006C44C");
        //土壤墒情-40CM，从寄存器地址0000开始查询，查询6个寄存器地址
        broadcast("060300000006C47F");
        //太阳能

    }

    private void broadcast(String frame) throws InterruptedException{
        for (Channel channel : TcpServer.channels.values()) {
            channel.writeAndFlush(frame);
        }
        Thread.sleep(2000);
    }

}
