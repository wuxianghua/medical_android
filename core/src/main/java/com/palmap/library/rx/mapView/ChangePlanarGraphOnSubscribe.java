package com.palmap.library.rx.mapView;

import com.palmap.library.rx.mapView.event.ChangePlanarGraphEvent;
import com.palmaplus.nagrand.data.PlanarGraph;
import com.palmaplus.nagrand.view.MapView;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

/**
 * Created by 王天明 on 2016/5/4.
 */
public class ChangePlanarGraphOnSubscribe implements Observable.OnSubscribe<ChangePlanarGraphEvent> {

    private MapView mapView;

    public ChangePlanarGraphOnSubscribe(MapView mapView) {
        this.mapView = mapView;
    }

    @Override
    public void call(final Subscriber<? super ChangePlanarGraphEvent> subscriber) {
        MapView.OnChangePlanarGraph listener = new MapView.OnChangePlanarGraph() {
            @Override
            public void onChangePlanarGraph(PlanarGraph planarGraph, PlanarGraph planarGraph1, long l, long l1) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(new ChangePlanarGraphEvent(planarGraph,planarGraph1,l,l1));
                }
            }
        };
        mapView.setOnChangePlanarGraph(listener);
        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                mapView.setOnChangePlanarGraph(null);
            }
        });
    }
}
