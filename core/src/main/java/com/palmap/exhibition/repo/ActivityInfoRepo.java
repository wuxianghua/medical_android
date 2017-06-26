package com.palmap.exhibition.repo;

import com.palmap.exhibition.model.Api_ActivityInfo;

import io.reactivex.Observable;


/**
 * Created by 王天明 on 2016/10/19.
 */
public interface ActivityInfoRepo {

    /**
     * 获取全部活动
     *
     * @return
     */
    Observable<Api_ActivityInfo> requestAllTypeActivity(String keyWord, boolean isExt);

    /**
     * 获取议程活动
     *
     * @return
     */
    Observable<Api_ActivityInfo> requestMeetingActivity();

    /**
     * 获取普通活动
     *
     * @return
     */
    Observable<Api_ActivityInfo> requestSimpleActivity();


    /**
     * 获取点亮活动
     *
     * @return
     */
    Observable<Api_ActivityInfo> requestLightEventActivity(long mapId);

}
