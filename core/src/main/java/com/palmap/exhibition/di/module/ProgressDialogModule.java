package com.palmap.exhibition.di.module;

import com.palmap.exhibition.di.ActivityScope;
import com.palmap.library.base.BaseActivity;
import com.palmap.library.delegate.ProgressDialogDelegate;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 王天明 on 2015/12/21 0021.
 */
@Module
public class ProgressDialogModule {
    @ActivityScope
    @Provides
    ProgressDialogDelegate providesDelegate(BaseActivity activity) {
        return new ProgressDialogDelegate(activity,"提示","加载中...");
    }
}
