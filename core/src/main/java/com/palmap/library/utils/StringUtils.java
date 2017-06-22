package com.palmap.library.utils;

/**
 * Created by 王天明 on 2016/10/21.
 * 字符串工具类
 */
public class StringUtils {


    public static String checkNullString(String data){
        if (data == null || data.length() ==0) return "";
        return data;
    }

}
