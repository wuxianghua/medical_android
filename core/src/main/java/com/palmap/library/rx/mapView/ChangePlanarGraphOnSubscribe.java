package com.palmap.library.rx.mapView;

import com.palmap.library.rx.mapView.event.ChangePlanarGraphEvent;
import com.palmaplus.nagrand.data.PlanarGraph;
import com.palmaplus.nagrand.view.MapView;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.annotations.NonNull;


/**
 * Created by 王天明 on 2016/5/4.
 */
public class ChangePlanarGraphOnSubscribe implements ObservableOnSubscribe<ChangePlanarGraphEvent> {

    private MapView mapView;

    public ChangePlanarGraphOnSubscribe(MapView mapView) {
        this.mapView = mapView;
    }

    @Override
    public void subscribe(@NonNull final ObservableEmitter<ChangePlanarGraphEvent> emitter) throws Exception {
        MapView.OnChangePlanarGraph listener = new MapView.OnChangePlanarGraph() {
            @Override
            public void onChangePlanarGraph(PlanarGraph planarGraph, PlanarGraph planarGraph1, long l, long l1) {
                if (!emitter.isDisposed()) {
                    emitter.onNext(new ChangePlanarGraphEvent(planarGraph,planarGraph1,l,l1));
                }
            }
        };
        mapView.setOnChangePlanarGraph(listener);
        emitter.setDisposable(new MainThreadDisposable() {
            @Override
            protected void onDispose() {
                mapView.setOnChangePlanarGraph(null);
            }
        });
    }
}
