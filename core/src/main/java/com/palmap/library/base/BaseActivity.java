package com.palmap.library.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.palmap.library.delegate.ProgressDialogDelegate;
import com.palmap.library.delegate.ToastDelegate;
import com.palmap.library.utils.IOUtils;
import com.palmap.library.view.LoadDataView;

import javax.inject.Inject;

import dagger.Lazy;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by 王天明 on 2015/12/18 0018.
 */
public class BaseActivity extends AppCompatActivity implements LoadDataView,View.OnClickListener {

    @Inject
    protected Lazy<ToastDelegate> toastDelegate;
    @Inject
    protected Lazy<ProgressDialogDelegate> proDelegate;

    public void showProDialog() {
        proDelegate.get().show();
    }

    public void hideProDialog() {
        try {
            proDelegate.get().hide();
        }catch (Exception e) {
        }
    }

    protected Handler handler = new Handler(Looper.getMainLooper());

    public ToastDelegate getToastDelegate() {
        return toastDelegate.get();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            proDelegate.get().hide();
            proDelegate = null;
        } catch (Exception e) {
        }
    }

    @Override
    public void showLoading() {
        showProDialog();
    }

    @Override
    public void hideLoading() {
        hideProDialog();
    }

    @Override
    public void showRetry(Throwable throwable,Runnable runnable) {
    }

    @Override
    public void hideRetry() {
    }

    @Override
    public void showErrorMessage(String message) {
        showMessage(message);
    }

    @Override
    public void showMessage(final String message) {
        if (IOUtils.checkMainThread()) {
            getToastDelegate().showMsgLong(message);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getToastDelegate().showMsgLong(message);
                }
            });
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public BaseApplication getAndroidApplication() {
        return (BaseApplication) getApplication();
    }

//    public Subscription postTask(final Runnable runnable) {
//        return getAndroidApplication().uiExecutor.getScheduler().createWorker().schedule(new Action0() {
//            @Override
//            public void call() {
//                runnable.run();
//            }
//        });
//    }

    public void runOnJobThread(Runnable runnable) {
        getAndroidApplication().jobExecutor.execute(runnable);
    }

    /**
     * 设置状态栏颜色
     * API >=19
     *
     * @param colorId
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void initStatusBar(int colorId) {
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(colorId));
    }

    /**
     * 获取root节点
     *
     * @return
     */
    public View getRootView() {
        return findViewById(android.R.id.content);
    }

    public void postDelayed(Runnable runnable, long time) {
        handler.postDelayed(runnable, time);
    }

    public <V extends View> V findView(int id){
        return (V)findViewById(id);
    }

    @Override
    public void onClick(View v) {

    }
}