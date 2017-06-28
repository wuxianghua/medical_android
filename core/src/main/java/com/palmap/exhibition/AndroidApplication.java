package com.palmap.exhibition;

import android.content.Context;

import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.di.compent.ApplicationComponent;
import com.palmap.exhibition.di.compent.DaggerApplicationComponent;
import com.palmap.exhibition.di.module.ApplicationModule;
import com.palmap.exhibition.iflytek.IFlytekController;
import com.palmap.exhibition.navigator.Navigator;
import com.palmap.library.base.BaseApplication;
import com.palmap.library.utils.FileUtils;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.BuildConfig;
import com.palmaplus.nagrand.core.Engine;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import static com.palmap.exhibition.di.compent.ApplicationComponent.IFLYTEK_KEY;

/**
 * Created by 王天明 on 2016/6/3.
 */
public class AndroidApplication extends BaseApplication {
    ApplicationComponent applicationComponent;

    @Inject
    Navigator navigator;
    @Named(IFLYTEK_KEY)
    @Inject
    String iflytek_key;

    private static AndroidApplication instance;

    /**
     * 定位使用的mapID
     */
    private long locationMapId = -1;

    public static void attachContext(Context base) {
        if (instance != null) {
            instance = null;
        }
        instance = new AndroidApplication();
        instance.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;
        }
        Engine.getInstance();
        IFlytekController.getInstance().initWithKey(this, iflytek_key);
        LogUtil.allowE = LogUtil.allowD = BuildConfig.DEBUG;
        try {
            FileUtils.createSDDir(Config.CACHE_FILE_PATH + File.separator);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("创建缓存文件夹失败");
        }
    }

    public static void onCreated() {
        if (instance == null) {
            throw new IllegalArgumentException("your must call AndroidApplication.attachContext(context) before !!!");
        }
        instance.onCreate();
    }

    @Override
    protected void initInject() {
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
        applicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public synchronized static AndroidApplication getInstance() {
        if (instance == null) {
            throw new IllegalArgumentException("your application must extends com.palmap.exhibition.AndroidApplication");
        }
        return instance;
    }

    public long getLocationMapId() {
        return locationMapId;
    }

    public void setLocationMapId(long locationMapId) {
        this.locationMapId = locationMapId;
    }
}