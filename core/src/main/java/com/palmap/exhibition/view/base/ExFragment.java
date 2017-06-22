package com.palmap.exhibition.view.base;

import android.content.Context;

import com.palmap.exhibition.AndroidApplication;
import com.palmap.library.base.BaseApplication;
import com.palmap.library.base.fragment.BaseFragment;
import com.palmap.library.delegate.ProgressDialogDelegate;
import com.palmap.library.delegate.ToastDelegate;
import com.palmap.library.utils.IOUtils;
import com.palmap.library.view.LoadDataView;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by aoc on 2016/8/19.
 */
public abstract class ExFragment extends BaseFragment implements LoadDataView {

    @Inject
    protected Lazy<ToastDelegate> toastDelegate;
    @Inject
    protected Lazy<ProgressDialogDelegate> proDelegate;

    public ToastDelegate getToastDelegate() {
        return toastDelegate.get();
    }

    @Override
    public void onDestroy() {
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
    public void showRetry(Throwable throwable, Runnable runnable) {
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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getToastDelegate().showMsgLong(message);
                }
            });
        }
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    public void showProDialog() {
        proDelegate.get().show();
    }

    public void hideProDialog() {
        try {
            proDelegate.get().hide();
        } catch (Exception e) {
        }
    }

    @Override
    public BaseApplication getAndroidApplication() {
        return AndroidApplication.getInstance();
    }
}
