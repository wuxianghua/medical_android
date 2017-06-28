package com.palmap.medical;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.palmap.exhibition.AndroidApplication;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by 王天明 on 2017/6/22.
 */
public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidApplication.onCreated();
    }

    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AndroidApplication.attachContext(base);
        CrashReport.initCrashReport(this, "692c141bdb", true);
    }

}
