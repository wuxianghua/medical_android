package com.palmap.exhibition.config;

import com.palmap.exhibition.BuildConfig;

/**
 * Created by aoc on 2016/6/16.
 */
public class ServereConfig {

    public static final String HOST;
    public static final String BOOTHINFO = "boothInfo";
    public static final String ACTIVITYINFO = "activity";
    public static final String POSITIONINFO = "positionInfo";

    static{
        if (BuildConfig.DEBUG) {
            HOST = "http://expo.palmap.cn/test/dataApi/";
//            HOST = "http://10.0.11.112//dataApi/";
        }else{
            HOST = "http://expo.palmap.cn/dataApi/";
//            HOST = "http://192.168.3.180/dataApi/";
        }
    }

   // public static final String POI_LOGO = HOST + "/exhibition/file/img/logo/";
    public static final String CECHE_FILE = "/wtmCache";
    public static final long CECHE_SIZE = 30 * 1024 * 1024;

}
