package com.palmap.library.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by 王天明 on 2016/10/8.
 */
public class MetaHelper {

    public static String getMetaDataByKey(Context context, String key) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

}
