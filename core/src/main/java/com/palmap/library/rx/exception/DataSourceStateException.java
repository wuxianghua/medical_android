package com.palmap.library.rx.exception;

import com.palmaplus.nagrand.data.DataSource;

/**
 * Created by 王天明 on 2016/4/26.
 * 数据状态异常
 */
public class DataSourceStateException extends RuntimeException {

    private DataSource.ResourceState state;


    public DataSourceStateException(DataSource.ResourceState state) {
        this.state = state;
    }

    public DataSourceStateException(String detailMessage, DataSource.ResourceState state) {
        super(detailMessage);
        this.state = state;
    }

    public DataSourceStateException(String detailMessage, Throwable throwable, DataSource.ResourceState state) {
        super(detailMessage, throwable);
        this.state = state;
    }

    public DataSourceStateException(Throwable throwable, DataSource.ResourceState state) {
        super(throwable);
        this.state = state;
    }

    public DataSource.ResourceState getState() {
        return state;
    }

    @Override
    public String toString() {
        return super.toString() + "<"+state+">";
    }
}
