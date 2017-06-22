package com.palmap.exhibition.config;

import android.content.Context;
import android.preference.PreferenceManager;

import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.R;

import java.io.File;
import java.util.Locale;

/**
 * Created by 王天明 on 2016/6/3.
 */
public final class Config {

    static {
//        APP_KEY = MetaHelper.getMetaDataByKey(AndroidApplication.getInstance(),"NGR_APPKEY");
        APP_KEY = "6617d492627540a293237ccb70b85af1";
    }

    public enum Language {
        SIMPLIFIED_CHINESE,
        ENGLISH,
    }

    public enum PositioningManagerType {
        BLE,
        WIFI,
    }

    public static boolean isTestLocation = false;

    public static PositioningManagerType getPositioningManagerType(Context context) {
        if ("1".equals(PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.ngr_locationTypeKey), "1"))) {
            return PositioningManagerType.BLE;
        }
        return PositioningManagerType.WIFI;
    }

    public static Language getLanguage() {
        Context context = AndroidApplication.getInstance();
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.ngr_languageKey), "1").equals("1") ? Config.Language.SIMPLIFIED_CHINESE : Config.Language.ENGLISH;
    }

    public static void setLanguage(Language language) {
        String val = "1";
        if (language.equals(Language.ENGLISH)) {
            val = "2";
        }
        Context context = AndroidApplication.getInstance();
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(context.getString(R.string.ngr_languageKey), val)
                .commit();
    }

    /**
     * 获取地图配色方案
     *
     * @return
     */
    public static String getMapStyle() {
        Context context = AndroidApplication.getInstance();
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.ngr_mapStyleKey), "999");
    }


    //public static Language language = Language.SIMPLIFIED_CHINESE;
    public static Locale oldLanguage;

    /**
     * 图聚mapSdk key
     */
    public static final String APP_KEY;

    public static String getLanguagePara() {
        if (Language.SIMPLIFIED_CHINESE.equals(getLanguage())) {
            return "zh_cn";
        }
        return "en";
    }

    /**
     * 默认服务器地址
     */
    public static final String DEFAULT_MAP_SERVER_URL = "http://api.ipalmap.com/";
//    public static final String DEFAULT_MAP_SERVER_URL = "http://123.59.132.203/";

    /**
     * 路网吸附距离
     * 起始点最短距离(起始点)
     */
    public static final int MIN_DISTANCE = 5;
    public static final int END_NAVI_DISTANCE = 3;
    public static final int PUSH_MAX_COUNT = 2;

    /**
     * 地图服务器地址
     */
    public static String MAP_SERVER_URL = DEFAULT_MAP_SERVER_URL;

    /**
     * wifi定位服务器地址
     */
    public static final String WIFI_LICATION_URL = MAP_SERVER_URL;
    /**
     * 路线规划最短距离
     */
    public static final double PATHPLANNING_MIN_DISTANCE = 5d;

    public static final String LUR_NAME = "Nagrand" + File.separator + "lua"; // lua地址
    public static final String CACHE_FILE_PATH = "Nagrand" + File.separator + "cache";

    /**
     * 地图旋转角度
     */
    public static float MAP_ANGLE;

    /**
     * 推送活动的ID
     */
    public static final int ACTIVITY_ID_PUSH = -1;
    /**
     * 会议活动类型
     */
    public static final int ACTIVITY_TYPE_MEETING = 0;
    /**
     * 普通活动类型
     */
    public static final int ACTIVITY_TYPE_ATY = 1;
    /**
     * 推送活动类型
     */
    public static final int ACTIVITY_TYPE_LIGHTEVENT = 2;

    //烟台楼层ID 写死
    public static final int ID_FLOOR_F1 = 1864263;
    public static final int ID_FLOOR_F2 = 1864381;
    public static final int ID_FLOOR_B1 = 1903458;


    //默认的退送距离
    public static final int pushDiameter = 10;
    public static final int lightDiameter = 10;

}