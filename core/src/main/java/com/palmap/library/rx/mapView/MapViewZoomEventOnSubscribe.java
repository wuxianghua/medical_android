package com.palmap.library.rx.mapView;

import com.palmap.library.rx.mapView.event.MapViewZoomEvent;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.gestures.OnZoomListener;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.annotations.NonNull;

import static com.palmap.library.utils.Preconditions.checkUiThread;

/**
 * Created by 王天明 on 2016/5/4.
 */
public class MapViewZoomEventOnSubscribe implements ObservableOnSubscribe<MapViewZoomEvent> {

    private MapView mapView;

    public MapViewZoomEventOnSubscribe(MapView mapView) {
        this.mapView = mapView;
    }


    @Override
    public void subscribe(@NonNull final ObservableEmitter<MapViewZoomEvent> emitter) throws Exception {
        checkUiThread();
        OnZoomListener onZoomListener = new OnZoomListener() {
            @Override
            public void preZoom(MapView mapView, float v, float v1) {
                if (!emitter.isDisposed()) {
                    MapViewZoomEvent zoomEvent = new MapViewZoomEvent(v, v1, mapView);
                    emitter.onNext(zoomEvent);
                }
            }

            @Override
            public void onZoom(MapView mapView, boolean b) {
                if (!emitter.isDisposed()) {
                    MapViewZoomEvent zoomEvent = new MapViewZoomEvent(mapView, b);
                    emitter.onNext(zoomEvent);
                }
            }

            @Override
            public void postZoom(MapView mapView, float v, float v1) {
                if (!emitter.isDisposed()) {
                    MapViewZoomEvent zoomEvent = new MapViewZoomEvent(mapView, v, v1);
                    emitter.onNext(zoomEvent);
                }
            }
        };

        mapView.setOnZoomListener(onZoomListener);
        emitter.setDisposable(new MainThreadDisposable() {
            @Override
            protected void onDispose() {
                mapView.setOnZoomListener(null);
            }
        });
    }
}
