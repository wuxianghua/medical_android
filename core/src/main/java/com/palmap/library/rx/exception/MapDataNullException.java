package com.palmap.library.rx.exception;

/**
 * Created by 王天明 on 2016/4/26.
 * 地图数据空异常
 */
public class MapDataNullException extends RuntimeException{

    public MapDataNullException() {
    }

    public MapDataNullException(String detailMessage) {
        super(detailMessage);
    }

    public MapDataNullException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public MapDataNullException(Throwable throwable) {
        super(throwable);
    }
}

