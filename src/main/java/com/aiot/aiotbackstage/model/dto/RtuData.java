package com.aiot.aiotbackstage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Avernus
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RtuData {

    /**
     * rtu识别码
     */
    private int rtu;
    /**
     * 地址码
     */
    private int addr;

    /**
     * 功能码
     */
    private int func;

    /**
     * 数据区长度
     */
    private int dataLength;

    /**
     * 数据区
     */
    private int[] data;

    /**
     * 校验码低位
     */
    private int crc1;

    /**
     * 校验码高位
     */
    private int crc2;

    public RtuData(int rtu, int addr, int func, int dataLength, int[] data) {
        this.rtu = rtu;
        this.addr = addr;
        this.func = func;
        this.dataLength = dataLength;
        this.data = data;
    }
}
