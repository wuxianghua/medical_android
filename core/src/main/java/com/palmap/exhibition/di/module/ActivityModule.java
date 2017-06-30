package com.palmap.exhibition.di.module;


import com.palmap.exhibition.di.ActivityScope;
import com.palmap.library.base.BaseActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;


/**
 * Created by 王天明 on 2015/12/18 0018.
 */
@Module(includes = {ToastModule.class,ProgressDialogModule.class})
public class ActivityModule {

    private final BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }
    @ActivityScope
    @Provides
    BaseActivity activity() {
        return this.activity;
    }

    @ActivityScope
    @Provides ExecutorService providesMapViewDrawExecutor(){
        return Executors.newSingleThreadExecutor();
    }

}
