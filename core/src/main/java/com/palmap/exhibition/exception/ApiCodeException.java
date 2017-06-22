package com.palmap.exhibition.exception;

/**
 * Created by 王天明 on 2016/11/21.
 * 请求服务器返回码错误
 */
public class ApiCodeException extends RuntimeException {

    public ApiCodeException() {
    }

    public ApiCodeException(String detailMessage) {
        super(detailMessage);
    }

    public ApiCodeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
