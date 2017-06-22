package com.palmap.library.base;

import android.support.multidex.MultiDexApplication;

import com.palmap.library.executor.PostExecutionThread;
import com.palmap.library.executor.ThreadExecutor;

import javax.inject.Inject;

/**
 * Created by 王天明 on 2016/5/3.
 */
public abstract class BaseApplication extends MultiDexApplication {

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
