package com.palmap.exhibition.exception;

/**
 * Created by 王天明 on 2016/9/26.
 * 启动参数错误
 */
public class LauncherArgsException extends RuntimeException {


    public LauncherArgsException() {
    }

    public LauncherArgsException(String detailMessage) {
        super(detailMessage);
    }

    public LauncherArgsException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
