package com.palmap.library.view;

import android.content.Context;

import com.palmap.library.base.BaseApplication;

/**
 * Created by 王天明 on 2015/12/21 0021.
 */
public interface LoadDataView {
    /**
     * 显示隐藏读取数据的view
     */
    void showLoading();
    void hideLoading();

    /**
     * 显示隐藏重新加载view
     */
    void showRetry(Throwable throwable,Runnable runnable);
    void hideRetry();

    /**
     * 显示错误 可能是对话框 也可能是toast
     * 通常使用toast
     * @param message
     */
    void showErrorMessage(String message);

    /**
     * 显示信息 可能是对话框 也可能是toast
     * @param message
     */
    void showMessage(String message);

    Context getContext();

    BaseApplication getAndroidApplication();
}
