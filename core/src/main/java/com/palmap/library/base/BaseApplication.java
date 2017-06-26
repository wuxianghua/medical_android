package com.palmap.library.base;

import android.app.Application;

import com.palmap.library.executor.PostExecutionThread;
import com.palmap.library.executor.ThreadExecutor;

import javax.inject.Inject;

/**
 * Created by 王天明 on 2016/5/3.
 */
//public abstract class BaseApplication extends MultiDexApplication {
public abstract class BaseApplication extends Application {

    @Inject
    protected PostExecutionThread uiExecutor;
    @Inject
    protected ThreadExecutor jobExecutor;

    @Override
    public void onCreate() {
        super.onCreate();
        initInject();
    }

    protected abstract void initInject();

    public PostExecutionThread getUiExecutor() {
        return uiExecutor;
    }

    public ThreadExecutor getJobExecutor() {
        return jobExecutor;
    }
}
