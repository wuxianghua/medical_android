package com.palmap.library.executor;


import io.reactivex.Scheduler;

/**
 * Created by 王天明 on 2016/5/3.
 */
public interface PostExecutionThread {
    Scheduler getScheduler();
}
