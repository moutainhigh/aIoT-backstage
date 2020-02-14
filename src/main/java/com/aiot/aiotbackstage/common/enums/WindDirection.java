package com.aiot.aiotbackstage.common.enums;

/**
 * 风向对照表
 */
public enum WindDirection {

    north(0),
    northeast(1),
    east(2),
    southeast(3),
    south(4),
    southwest(5),
    west(6),
    northwest(7);

    private Integer value;

    WindDirection(Integer value) {
        this.value = value;
    }

    public static WindDirection trans(int value) {
        for (WindDirection enums : WindDirection.values()) {
            if (enums.value == value) {
                return enums;
            }
        }
        return null;
    }
}
