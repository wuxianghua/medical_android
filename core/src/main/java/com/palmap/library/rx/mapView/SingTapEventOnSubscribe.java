package com.palmap.library.rx.mapView;

import com.palmap.library.rx.mapView.event.SingleTapEvent;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.gestures.OnSingleTapListener;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

/**
 * Created by 王天明 on 2016/5/5.
 */
public class SingTapEventOnSubscribe implements Observable.OnSubscribe<SingleTapEvent> {

    private MapView mapView;

    public SingTapEventOnSubscribe(MapView mapView) {
        this.mapView = mapView;
    }

    @Override
    public void call(final Subscriber<? super SingleTapEvent> subscriber) {
        checkUiThread();
        OnSingleTapListener listener = new OnSingleTapListener() {
            @Override
            public void onSingleTap(MapView mapView, float v, float v1) {
                if (!subscriber.isUnsubscribed()) {
                    SingleTapEvent event = new SingleTapEvent(mapView, v, v1);
                    subscriber.onNext(event);
                }
            }
        };
        mapView.setOnSingleTapListener(listener);
        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                mapView.setOnSingleTapListener(null);
            }
        });
    }
}
