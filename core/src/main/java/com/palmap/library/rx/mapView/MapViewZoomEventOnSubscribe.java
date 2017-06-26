package com.palmap.library.rx.mapView;

import com.palmap.library.rx.mapView.event.MapViewZoomEvent;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.gestures.OnZoomListener;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.palmap.library.utils.Preconditions.checkUiThread;

/**
 * Created by 王天明 on 2016/5/4.
 */
public class MapViewZoomEventOnSubscribe implements Observable.OnSubscribe<MapViewZoomEvent> {

    private MapView mapView;

    public MapViewZoomEventOnSubscribe(MapView mapView) {
        this.mapView = mapView;
    }

    @Override
    public void call(final Subscriber<? super MapViewZoomEvent> subscriber) {
        checkUiThread();
        OnZoomListener onZoomListener = new OnZoomListener() {
            @Override
            public void preZoom(MapView mapView, float v, float v1) {
                if (!subscriber.isUnsubscribed()) {
                    MapViewZoomEvent zoomEvent = new MapViewZoomEvent(v, v1, mapView);
                    subscriber.onNext(zoomEvent);
                }
            }

            @Override
            public void onZoom(MapView mapView, boolean b) {
                if (!subscriber.isUnsubscribed()) {
                    MapViewZoomEvent zoomEvent = new MapViewZoomEvent(mapView, b);
                    subscriber.onNext(zoomEvent);
                }
            }

            @Override
            public void postZoom(MapView mapView, float v, float v1) {
                if (!subscriber.isUnsubscribed()) {
                    MapViewZoomEvent zoomEvent = new MapViewZoomEvent(mapView, v, v1);
                    subscriber.onNext(zoomEvent);
                }
            }
        };

        mapView.setOnZoomListener(onZoomListener);
        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                mapView.setOnZoomListener(null);
            }
        });
    }
}
