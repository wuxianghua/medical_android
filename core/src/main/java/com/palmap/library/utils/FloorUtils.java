package com.palmap.library.utils;

/**
 * Created by 王天明 on 2016/9/12.
 */
public class FloorUtils {

    /**
     * 判断第一个楼层名是不是比第二个楼层高
     *
     * @param floorName1
     * @param floorName2
     * @return 负数: 楼1 比楼2 矮小  0相等 正数 比楼2高
     */
    public static int compare(String floorName1, String floorName2) {
        if (isEmpty(floorName1)) return -1;
        if (isEmpty(floorName2)) return 1;
        floorName1 = floorName1.trim().toUpperCase();
        floorName2 = floorName2.trim().toUpperCase();
        int floorNumber1 = Integer.parseInt(floorName1.substring(1, floorName1.length()));
        int floorNumber2 = Integer.parseInt(floorName2.substring(1, floorName2.length()));
        if (floorName1.startsWith("B")) {
            floorNumber1 = -floorNumber1;
        }
        if (floorName2.startsWith("B")) {
            floorNumber2 = -floorNumber2;
        }
        if (floorName1.startsWith("G")) {
            floorNumber1 = 0;
        }
        if (floorName2.startsWith("G")) {
            floorNumber2 = 0;
        }
        return floorNumber1 - floorNumber2;
    }

    private static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

}