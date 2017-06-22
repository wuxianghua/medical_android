package com.palmap.exhibition.di.compent;


import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.dao.business.ActivityInfoBusiness;
import com.palmap.exhibition.dao.business.CoordinateBusiness;
import com.palmap.exhibition.di.module.ApplicationModule;
import com.palmap.exhibition.navigator.Navigator;
import com.palmap.exhibition.repo.ActivityInfoRepo;
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
    AndroidApplication application();

    PostExecutionThread mainExecutor();

    ThreadExecutor jobExecutor();

    Navigator navigator();

    ActivityInfoRepo activityInfoRepo();

    DataSource dataSource();

    ActivityInfoBusiness activityInfoBusiness();

    CoordinateBusiness coordinateBusiness();

    void inject(AndroidApplication app);

}