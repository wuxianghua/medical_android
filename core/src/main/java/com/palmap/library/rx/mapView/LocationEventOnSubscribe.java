package com.palmap.library.rx.mapView;

import com.palmap.library.rx.mapView.event.LocationEvent;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.position.Location;
import com.palmaplus.nagrand.position.PositioningManager;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

/**
 * Created by 王天明 on 2016/5/4.
 * 定位事件
 */
public class LocationEventOnSubscribe implements Observable.OnSubscribe<LocationEvent> {

    private PositioningManager positioningManager;

    public LocationEventOnSubscribe(PositioningManager positioningManager) {
        this.positioningManager = positioningManager;
    }

    @Override
    public void call(final Subscriber<? super LocationEvent> subscriber) {
        checkUiThread();
        PositioningManager.OnLocationChangeListener<Location> listener = new PositioningManager.OnLocationChangeListener<Location>() {
            @Override
            public void onLocationChange(PositioningManager.LocationStatus locationStatus, Location location, Location t1) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(new LocationEvent(locationStatus,location,t1));
                }
            }
        };
        positioningManager.setOnLocationChangeListener(listener);
        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                LogUtil.d("<=========onUnsubscribe=============>");
                positioningManager.setOnLocationChangeListener(null);
                positioningManager.stop();
                positioningManager.close();
//                positioningManager.drop();
            }
        });

    }
}
