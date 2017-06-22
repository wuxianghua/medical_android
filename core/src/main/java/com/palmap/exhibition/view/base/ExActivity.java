package com.palmap.exhibition.view.base;

import android.os.Bundle;

import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.di.compent.ApplicationComponent;
import com.palmap.exhibition.di.compent.DaggerPresenterComponent;
import com.palmap.exhibition.di.compent.PresenterComponent;
import com.palmap.exhibition.di.module.ActivityModule;
import com.palmap.exhibition.navigator.Navigator;
import com.palmap.library.base.BaseActivity;
import com.palmap.library.presenter.Presenter;

/**
 * Created by 王天明 on 2016/6/3.
 */
public abstract class ExActivity<P extends Presenter> extends BaseActivity {

    private P p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p = inject();
    }

    public ApplicationComponent getApplicationComponent() {
        if (getApplication() instanceof AndroidApplication) {
            return ((AndroidApplication) getApplication()).getApplicationComponent();
        }
        return AndroidApplication.getInstance().getApplicationComponent();
    }

    public ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    protected abstract P inject();

    @Override
    protected void onResume() {
        super.onResume();
        p.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        p.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        p.pause();
    }

    protected PresenterComponent daggerInject() {
        return DaggerPresenterComponent.builder().activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .build();
    }

    public AndroidApplication getAndroidApplication() {
        return (AndroidApplication) getApplication();
    }

    public Navigator getNavigator() {
        return getAndroidApplication().getNavigator();
    }

}