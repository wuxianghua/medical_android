package com.palmap.exhibition.di.module;

import com.palmap.exhibition.di.ActivityScope;
import com.palmap.library.base.BaseActivity;
import com.palmap.library.delegate.ToastDelegate;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 王天明 on 2015/12/18 0018.
 */
@Module
public class ToastModule {
    @ActivityScope
    @Provides
    ToastDelegate providesDelegate(BaseActivity activity) {
        return new ToastDelegate(activity);
    }
}