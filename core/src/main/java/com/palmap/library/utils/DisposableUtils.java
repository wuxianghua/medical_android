package com.palmap.library.utils;

import io.reactivex.disposables.Disposable;

/**
 * Created by 王天明 on 2016/12/22.
 */

public class DisposableUtils {

    public static void unsubscribeAll(Disposable... disposablesArr) {
        if (null == disposablesArr) {
            return;
        }
        for (Disposable disposable : disposablesArr) {
            try {
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
