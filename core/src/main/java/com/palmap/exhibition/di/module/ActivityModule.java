package com.palmap.exhibition.di.module;


import com.palmap.exhibition.di.ActivityScope;
import com.palmap.library.base.BaseActivity;

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
}
