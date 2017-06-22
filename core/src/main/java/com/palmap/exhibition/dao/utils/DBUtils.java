package com.palmap.exhibition.dao.utils;

import android.content.Context;

import com.palmap.exhibition.dao.DaoMaster;
import com.palmap.exhibition.dao.DaoSession;

/**
 * Created by 王天明 on 2016/5/9.
 */
public class DBUtils {

    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private static String DB_NAME = "palmap.db";

    /**
     * 获取DaoMaster
     *
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME,
                    null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 获取DaoSession
     *
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
