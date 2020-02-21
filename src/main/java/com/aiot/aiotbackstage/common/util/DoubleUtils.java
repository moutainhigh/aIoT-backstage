package com.aiot.aiotbackstage.common.util;

public class DoubleUtils {

    public static Double average(Double... numbers) {
        int size = numbers.length;
        Double sum = 0D;
        for (Double number : numbers) {
            if (number != null) {
                sum += number;
            } else {
                size--;
            }
        }
        return sum / size;
    }
}
