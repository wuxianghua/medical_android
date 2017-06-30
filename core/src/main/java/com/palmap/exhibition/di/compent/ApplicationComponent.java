package com.palmap.exhibition.di.compent;


import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.dao.business.ActivityInfoBusiness;
import com.palmap.exhibition.dao.business.CoordinateBusiness;
import com.palmap.exhibition.di.module.ApplicationModule;
import com.palmap.exhibition.navigator.Navigator;
import com.palmap.library.executor.PostExecutionThread;
import com.palmap.library.executor.ThreadExecutor;
import com.palmaplus.nagrand.data.DataSource;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by 王天明 on 2015/12/18 0018.
 */
@Component(modules = {ApplicationModule.class})
@Singleton
public interface ApplicationComponent {

    String IFLYTEK_KEY = "iflytek_key";

    AndroidApplication application();

    PostExecutionThread mainExecutor();

    ThreadExecutor jobExecutor();

    Navigator navigator();

    DataSource dataSource();

    ActivityInfoBusiness activityInfoBusiness();

    CoordinateBusiness coordinateBusiness();

    void inject(AndroidApplication app);

}