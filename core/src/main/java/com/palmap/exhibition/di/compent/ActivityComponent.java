package com.palmap.exhibition.di.compent;

import com.palmap.exhibition.di.ActivityScope;
import com.palmap.exhibition.di.module.ActivityModule;
import com.palmap.library.base.BaseActivity;

import dagger.Component;

/**
 * Created by 王天明 on 2015/12/18 0018.
 */
@ActivityScope
@Component(dependencies = ApplicationComponent.class,modules = {ActivityModule.class})
public interface ActivityComponent {
    BaseActivity getActivity();
}