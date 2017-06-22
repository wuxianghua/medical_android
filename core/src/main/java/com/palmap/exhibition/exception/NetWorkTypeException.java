package com.palmap.exhibition.exception;

/**
 * Created by 王天明 on 2017/2/27.
 * 网络类型错误
 */
public class NetWorkTypeException extends RuntimeException {

    public NetWorkTypeException() {
    }

    public NetWorkTypeException(String detailMessage) {
        super(detailMessage);
    }
}
