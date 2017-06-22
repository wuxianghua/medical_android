package com.palmap.exhibition;

import android.text.TextUtils;

import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.di.compent.ApplicationComponent;
import com.palmap.exhibition.di.compent.DaggerApplicationComponent;
import com.palmap.exhibition.di.module.ApplicationModule;
import com.palmap.exhibition.navigator.Navigator;
import com.palmap.library.base.BaseApplication;
import com.palmap.library.utils.FileUtils;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.BuildConfig;
import com.palmaplus.nagrand.core.Engine;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by 王天明 on 2016/6/3.
 */
public class AndroidApplication extends BaseApplication{
    ApplicationComponent applicationComponent;

    @Inject
    Navigator navigator;

    String regionJsonStr = null;

    private static AndroidApplication instance;

    /**
     * 定位使用的mapID
     */
    private long locationMapId = -1;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Engine.getInstance();
        LogUtil.allowE = LogUtil.allowD = BuildConfig.DEBUG;
        try {
            FileUtils.createSDDir(Config.CACHE_FILE_PATH + File.separator);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("创建缓存文件夹失败");
        }
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

    public String getRegionJsonStr() {
        return regionJsonStr;
    }

    public void setRegionJsonStr(String regionJsonStr) {
        if (TextUtils.isEmpty(this.regionJsonStr)) {
            this.regionJsonStr = regionJsonStr;
        }
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