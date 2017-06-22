package com.palmap.exhibition.launcher;

/**
 * Created by 王天明 on 2016/7/24.
 */
public interface LauncherListener {
    void onEngineLoading();

    void onLauncherComplete();

    void onError(Throwable throwable);
}
