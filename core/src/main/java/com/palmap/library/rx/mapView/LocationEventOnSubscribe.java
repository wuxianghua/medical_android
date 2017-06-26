package com.palmap.library.rx.mapView;

import com.palmap.library.rx.mapView.event.LocationEvent;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.position.Location;
import com.palmaplus.nagrand.position.PositioningManager;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.annotations.NonNull;

import static com.palmap.library.utils.Preconditions.checkUiThread;

/**
 * Created by 王天明 on 2016/5/4.
 * 定位事件
 */
public class LocationEventOnSubscribe implements ObservableOnSubscribe<LocationEvent> {

    private PositioningManager positioningManager;

    public LocationEventOnSubscribe(PositioningManager positioningManager) {
        this.positioningManager = positioningManager;
    }

    @Override
    public void subscribe(@NonNull final ObservableEmitter<LocationEvent> emitter) throws Exception {
        checkUiThread();
        PositioningManager.OnLocationChangeListener<Location> listener = new PositioningManager.OnLocationChangeListener<Location>() {
            @Override
            public void onLocationChange(PositioningManager.LocationStatus locationStatus, Location location, Location t1) {
                if (!emitter.isDisposed()) {
                    emitter.onNext(new LocationEvent(locationStatus,location,t1));
                }
            }
        };
        positioningManager.setOnLocationChangeListener(listener);
        emitter.setDisposable(new MainThreadDisposable() {
            @Override
            protected void onDispose() {
                LogUtil.d("<=========onUnsubscribe=============>");
                positioningManager.setOnLocationChangeListener(null);
                positioningManager.stop();
                positioningManager.close();
//                positioningManager.drop();
            }
        });
    }
}
