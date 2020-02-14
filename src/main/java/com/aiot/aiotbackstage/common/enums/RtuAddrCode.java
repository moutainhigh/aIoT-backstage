package com.aiot.aiotbackstage.common.enums;

import lombok.Data;

/**
 * rtu地址码与传感器类型的mapping
 * @author Avernus
 */
public enum RtuAddrCode {

    //风速
    WIND_SPEED(1,"wind_speed"),
    //风向
    WIND_DIRECTION(2, "wind_direction"),
    //百叶箱
    ATMOS(3, "atmos"),
    //土壤墒情
    DUST(4, "dust");

    public int addr;
    public String type;

    RtuAddrCode(int addr, String type) {
        this.addr = addr;
        this.type = type;
    }

    public static String getTypeByAddr(int addr) {
        for (RtuAddrCode value : RtuAddrCode.values()) {
            if (value.addr == addr) {
                return value.type;
            }
        }
        return null;
    }

    public static RtuAddrCode trans(int addr) {
        for (RtuAddrCode value : RtuAddrCode.values()) {
            if (value.addr == addr) {
                return value;
            }
        }
        return null;
    }
}
