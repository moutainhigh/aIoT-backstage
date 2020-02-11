package com.aiot.aiotbackstage.common.enums;

/**
 * rtu地址码与传感器类型的mapping
 * @author Avernus
 */
public enum RtuAddrCode {

    //风速
    WIND_SPEED(0x01,"wind_speed"),
    //风向
    WIND_DIRECTION(0x02, "wind_direction"),
    //百叶箱
    ATMOS(0x03, "atmos"),
    //土壤墒情
    DUST(0x04, "dust");

    private int addr;
    private String type;

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
}
