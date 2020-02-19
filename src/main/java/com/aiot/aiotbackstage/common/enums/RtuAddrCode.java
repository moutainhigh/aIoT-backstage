package com.aiot.aiotbackstage.common.enums;

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
    //土壤墒情10cm
    DUST_10CM(5, "dust_10"),
    //土壤墒情20cm
    DUST_20CM(4, "dust_20"),
    //土壤墒情40cm
    DUST_40CM(6, "dust_40"),
    //太阳能
    SOLAR(7, "solar");

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
