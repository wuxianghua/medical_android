package com.palmap.exhibition.di.module;

import android.os.Environment;

import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.dao.business.ActivityInfoBusiness;
import com.palmap.exhibition.dao.business.CoordinateBusiness;
import com.palmap.exhibition.di.compent.ApplicationComponent;
import com.palmap.library.executor.PostExecutionThread;
import com.palmap.library.executor.ThreadExecutor;
import com.palmap.library.executor.impl.JobExecutor;
import com.palmap.library.executor.impl.UIThread;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.io.CacheAsyncHttpClient;
import com.palmaplus.nagrand.io.FileCacheMethod;

import java.io.File;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 王天明 on 2015/12/18 0018.
 */
@Module(includes = RepoModule.class)
public class ApplicationModule {
    private final AndroidApplication application;

    public ApplicationModule(AndroidApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    AndroidApplication provideApplication() {
        return this.application;
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    DataSource provideDataSource() {
        FileCacheMethod cacheMethod = new FileCacheMethod(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + Config.CACHE_FILE_PATH + File.separator);
        CacheAsyncHttpClient asyncHttpClient = new CacheAsyncHttpClient(Config.MAP_SERVER_URL);
        asyncHttpClient.reset(cacheMethod);
        DataSource dataSource = new DataSource(asyncHttpClient);
        LogUtil.e("provideDataSource !!!");
        return dataSource;
    }

    @Provides
    @Singleton
    ActivityInfoBusiness provideActivityInfoBusiness() {
        return new ActivityInfoBusiness(application);
    }

    @Provides
    @Singleton
    CoordinateBusiness provideCoordinateBusiness() {
        return new CoordinateBusiness(application);
    }

    @Named(ApplicationComponent.IFLYTEK_KEY)
    @Provides
    String provideKey(){
        return "5953456b";
    }

}
