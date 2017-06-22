package com.palmap.exhibition.repo.impl;

import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.api.ActivityInfoService;
import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.factory.ServiceFactory;
import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.exhibition.repo.ActivityInfoRepo;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 王天明 on 2016/10/19.
 */
@Singleton
public class ActivityInfoRepoImpl implements ActivityInfoRepo {


    private static final String TYPE_MEETING = "0";
    private static final String TYPE_SIMPLE = "1";
    private static final String TYPE_PUSH = "-1";
    private static final String TYPE_LIGHT_EVENT = "2";



    @Inject
    public ActivityInfoRepoImpl() {
    }

    private Observable<Api_ActivityInfo> requestActivityInfo(String type, String keyWord, boolean isExt){
        return ServiceFactory.create(ActivityInfoService.class)
                .requestActivityInfo(Config.getLanguagePara(),
                        AndroidApplication.getInstance().getLocationMapId(),
                        type, keyWord, isExt)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    /**
     * 获取全部活动
     * @return
     */
    public Observable<Api_ActivityInfo> requestAllTypeActivity(String keyWord,boolean isExt){
        return requestActivityInfo(null, keyWord, isExt);
    }

    /**
     * 获取议程活动
     * @return
     */
    public Observable<Api_ActivityInfo> requestMeetingActivity(){
        return requestActivityInfo(TYPE_MEETING, null, false);
    }

    /**
     * 获取普通活动
     * @return
     */
    public Observable<Api_ActivityInfo> requestSimpleActivity(){
        return requestActivityInfo(TYPE_SIMPLE, null, false);
    }

    /**
     * 获取推送活动
     * @return
     */
//    public Observable<Api_ActivityInfo> requestPushActivity(){
//        return requestActivityInfo(TYPE_PUSH, null, false);
//    }

    /**
     * 获取点亮活动
     * @return
     */
    public Observable<Api_ActivityInfo> requestLightEventActivity(long mapID){
        return requestActivityInfo(TYPE_LIGHT_EVENT, null, true);
    }

}
