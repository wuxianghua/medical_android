package com.palmap.exhibition.repo.impl;


import android.accounts.NetworkErrorException;
import android.os.Handler;
import android.os.Looper;

import com.palmap.exhibition.api.LocationService;
import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.factory.ServiceFactory;
import com.palmap.exhibition.model.LocationInfoModel;
import com.palmap.exhibition.repo.LocationListener;
import com.palmap.exhibition.repo.LocationRepo;
import com.palmap.library.utils.IpUtils;
import com.palmap.library.utils.LogUtil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by wtm on 2017/1/3.
 */

public class LocationRepoImpl implements LocationRepo {

    private ArrayList<LocationListener> listeners;

    private static LocationRepoImpl instance;

    private LocationService locationService;

    //api 请求得到的定位model
    private LocationInfoModel locationInfoModel = null;

    private Handler callBackHandler;

    private TimeCallBack timeCallBack;

    private Disposable apiSubscription;

    private String ipAddress = null;

    public synchronized static LocationRepo getInstance() {
        if (instance == null) {
            instance = new LocationRepoImpl();
        }
        return instance;
    }

    private LocationRepoImpl() {
        listeners = new ArrayList<>();
        locationService = ServiceFactory.create(LocationService.class);
        callBackHandler = new Handler(Looper.getMainLooper());
        ipAddress = IpUtils.getIpAddress();
    }

    @Override
    public synchronized void startLocation() {
        if (null == timeCallBack) {
            timeCallBack = new TimeCallBack();
            timeCallBack.start(callBackHandler);
            sendApi();
        }
    }

    private void sendApi() {
        LogUtil.e("ip:" + IpUtils.getIpAddress());
        if (Config.isTestLocation) {
            LogUtil.e("请求测试的定位数据");

            apiSubscription = locationService.requestLocationTest("ip",
                    "10.0.20.197")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.computation())
                    .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
                        @Override
                        public ObservableSource<?> apply(@NonNull Observable<Object> objectObservable) throws Exception {
                            return objectObservable.delay(2000L, TimeUnit.MILLISECONDS);
                        }
                    }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                        @Override
                        public ObservableSource<?> apply(@NonNull Observable<Throwable> throwableObservable) throws Exception {
                            return throwableObservable.delay(2000L, TimeUnit.MILLISECONDS);
                        }
                    }).subscribe(new Consumer<LocationInfoModel>() {
                        @Override
                        public void accept(@NonNull LocationInfoModel locationInfoModel) throws Exception {
                            LocationRepoImpl.this.locationInfoModel = locationInfoModel;
                            callBackHandler.removeCallbacks(timeCallBack);
                            timeCallBack = new TimeCallBack();
                            timeCallBack.start(callBackHandler);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            locationInfoModel = null;
                        }
                    });

            return;
        }
        LogUtil.e("请求真实的定位数据");
        apiSubscription =
                requestLocation()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.computation())
                        .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(@NonNull Observable<Object> objectObservable) throws Exception {
                                return objectObservable.delay(2000L, TimeUnit.MILLISECONDS);
                            }
                        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Observable<Throwable> throwableObservable) throws Exception {
                        return throwableObservable.delay(2000L, TimeUnit.MILLISECONDS);
                    }
                }).subscribe(new Consumer<LocationInfoModel>() {
                    @Override
                    public void accept(LocationInfoModel locationInfoModel) {
                        LocationRepoImpl.this.locationInfoModel = locationInfoModel;
                        callBackHandler.removeCallbacks(timeCallBack);
                        timeCallBack = new TimeCallBack();
                        timeCallBack.start(callBackHandler);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        locationInfoModel = null;
                    }
                });
    }

    private Observable<LocationInfoModel> requestLocation() {
        return Observable.create(new ObservableOnSubscribe<LocationInfoModel>() {
            @Override
            public void subscribe(final ObservableEmitter<LocationInfoModel> emitter) throws Exception {
                locationService.requestLocation(
                        Config.APP_KEY,
                        "ip",
                        IpUtils.getIpAddress())
                        .subscribe(new Consumer<LocationInfoModel>() {
                            @Override
                            public void accept(LocationInfoModel locationInfoModel) {
                                emitter.onNext(locationInfoModel);
                                emitter.onComplete();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                emitter.onError(throwable);
                            }
                        });
            }
        });
    }

    /*static int index = 0;
    LocationInfoModel[] testLocation = {
            new LocationInfoModel(1.351231634014287E7, 4514890.368934499, Config.ID_FLOOR_F1),
            new LocationInfoModel(1.3512271370399999E7, 4514895.467600001, Config.ID_FLOOR_F1),
            new LocationInfoModel(1.3512343066619657E7, 4514945.023061262, Config.ID_FLOOR_F1),
            new LocationInfoModel(1.3512342951641958E7, 4514894.288585303, Config.ID_FLOOR_F1),
            new LocationInfoModel(1.3512362735900002E7, 4514899.780300001, Config.ID_FLOOR_F1),
    };


    private Observable<LocationInfoModel> requestLocationTest() {
        return Observable.create(new Observable.OnSubscribe<LocationInfoModel>() {
            @Override
            public void call(Subscriber<? super LocationInfoModel> subscriber) {
                index++;
                if (index % 5 == 0) {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                    return;
                }
                subscriber.onNext(testLocation[index % testLocation.length]);
                subscriber.onCompleted();
            }
        });
    }*/

    @Override
    public synchronized void stopLocation() {
        locationInfoModel = null;
        if (timeCallBack != null) {
            LogUtil.e("停止stopLocation");
            callBackHandler.removeCallbacks(timeCallBack);
            timeCallBack = null;
        }
        if (null != apiSubscription && !apiSubscription.isDisposed()) {
            apiSubscription.dispose();
            apiSubscription = null;
        }
    }

    @Override
    public synchronized void addListener(LocationListener locationListener) {
        if (listeners != null && !listeners.contains(locationListener)) {
            listeners.add(locationListener);
        }
    }

    @Override
    public synchronized void removeListener(LocationListener locationListener) {
        if (listeners != null && listeners.contains(locationListener)) {
            listeners.remove(locationListener);
        }
        if (listeners == null || listeners.size() == 0) {
            stopLocation();
        }
    }

    private final class TimeCallBack implements Runnable {

        private long timeSpace = 2000L;

        private Handler handler;

        public TimeCallBack() {
        }

        public TimeCallBack(long timeSpace) {
            this.timeSpace = timeSpace;
        }

        private void start(Handler handler) {
            this.handler = handler;
            handler.postDelayed(this, timeSpace);
        }

        @Override
        public void run() {
            if (null == locationInfoModel) {
                for (LocationListener l : listeners) {
                    l.onFailed(new NetworkErrorException(), "request error");
                }
            } else {
                LocationInfoModel model = locationInfoModel.valueClone();
                for (LocationListener l : listeners) {
                    l.onComplete(model, System.currentTimeMillis());
                }
                locationInfoModel = null;
            }
            handler.postDelayed(this, timeSpace);
        }
    }
}
