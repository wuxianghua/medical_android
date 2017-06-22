package com.palmap.exhibition.di.module;

import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.api.ActivityInfoService;
import com.palmap.exhibition.di.ActivityScope;
import com.palmap.exhibition.factory.ServiceFactory;
import com.palmap.exhibition.model.Api_PositionInfo;
import com.palmap.library.geoFencing.GeoFencing;
import com.palmap.library.geoFencing.GeoFencingManager;
import com.palmap.library.geoFencing.GeoFencingProvides;
import com.palmap.library.geoFencing.impl.CircleGeoFencing;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 王天明 on 2016/9/9.
 */
@Module
public class FencingModule {

    @Provides
    @ActivityScope
    public GeoFencingManager<Api_PositionInfo.ObjBean> providesGeoFencingManager(List<GeoFencing<Api_PositionInfo.ObjBean>> geoFencings, GeoFencingProvides<Api_PositionInfo.ObjBean> provides) {
        GeoFencingManager<Api_PositionInfo.ObjBean> geoFencingManager = new GeoFencingManager<>();
        geoFencingManager.addAllFencing(geoFencings);
        geoFencingManager.setGeoFencingProvides(provides);
        return geoFencingManager;
    }

    @Provides
    @ActivityScope
    public List<GeoFencing<Api_PositionInfo.ObjBean>> providesGeoFencingList() {
        List<GeoFencing<Api_PositionInfo.ObjBean>> geoFencingList = new ArrayList<>();
        return geoFencingList;
    }

    @Provides
    @ActivityScope
    public GeoFencingProvides<Api_PositionInfo.ObjBean> providesGeoFencingProvides() {
        return new GeoFencingProvides<Api_PositionInfo.ObjBean>() {
            @Override
            public Observable<List<GeoFencing<Api_PositionInfo.ObjBean>>> providesGeoFencing() {
                // TODO: 2016/11/21 推送点位的活动ID为 固定值 -1
                return ServiceFactory.create(ActivityInfoService.class).requestPositionInfo(
                        AndroidApplication.getInstance().getLocationMapId(), -1
                ).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.computation()).map(new Func1<Api_PositionInfo, List<GeoFencing<Api_PositionInfo.ObjBean>>>() {
                            @Override
                            public List<GeoFencing<Api_PositionInfo.ObjBean>> call(Api_PositionInfo api_positionInfo) {
                                List<GeoFencing<Api_PositionInfo.ObjBean>> list = new ArrayList<>();
                                for (final Api_PositionInfo.ObjBean objBean : api_positionInfo.getObj()) {
                                    list.add(new CircleGeoFencing<Api_PositionInfo.ObjBean>(
                                            objBean.getFloorId(),
                                            objBean.getLatitude(),
                                            objBean.getLongitude(),
                                            objBean.getDiameter()) {
                                        @Override
                                        public Api_PositionInfo.ObjBean obtainGeoData() {
                                            return objBean;
                                        }
                                    });
                                }
                                return list;
                            }
                        });
            }
        };


//        return new GeoFencingProvides<Api_PositionInfo.ObjBean>() {
//            @Override
//            public Observable<List<GeoFencing<Api_PositionInfo.ObjBean>>> providesGeoFencing() {
//                return Observable.create(new Observable.OnSubscribe<List<GeoFencing<PushModel>>>() {
//                    @Override
//                    public void call(Subscriber<? super List<GeoFencing<PushModel>>> subscriber) {
//                        if (1 == 1) {
//                            subscriber.onNext(null);
//                            subscriber.onCompleted();
//                        } else {
//                            throw new RuntimeException("data is error");
//                        }
//                    }
//                });
//            }
//        };
    }

}
