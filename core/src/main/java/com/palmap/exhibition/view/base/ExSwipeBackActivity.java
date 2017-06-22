package com.palmap.exhibition.view.base;

import android.os.Bundle;
import android.view.View;

import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.di.compent.ApplicationComponent;
import com.palmap.exhibition.di.compent.DaggerPresenterComponent;
import com.palmap.exhibition.di.compent.PresenterComponent;
import com.palmap.exhibition.di.module.ActivityModule;
import com.palmap.exhibition.navigator.Navigator;
import com.palmap.library.base.BaseActivity;
import com.palmap.library.presenter.Presenter;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * Created by 王天明 on 2016/6/3.
 */
public abstract class ExSwipeBackActivity<P extends Presenter> extends BaseActivity implements SwipeBackActivityBase {

    private P p;

    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p = inject();

        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }

    public ApplicationComponent getApplicationComponent() {
        return ((AndroidApplication) getApplication()).getApplicationComponent();
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


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
