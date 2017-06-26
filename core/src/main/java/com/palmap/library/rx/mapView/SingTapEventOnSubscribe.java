package com.palmap.library.rx.mapView;

import com.palmap.library.rx.mapView.event.SingleTapEvent;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.gestures.OnSingleTapListener;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.annotations.NonNull;

import static com.palmap.library.utils.Preconditions.checkUiThread;


/**
 * Created by 王天明 on 2016/5/5.
 */
public class SingTapEventOnSubscribe implements ObservableOnSubscribe<SingleTapEvent> {

    private MapView mapView;

    public SingTapEventOnSubscribe(MapView mapView) {
        this.mapView = mapView;
    }

    @Override
    public void subscribe(@NonNull final ObservableEmitter<SingleTapEvent> emitter) throws Exception {
        checkUiThread();
        OnSingleTapListener listener = new OnSingleTapListener() {
            @Override
            public void onSingleTap(MapView mapView, float v, float v1) {
                if (!emitter.isDisposed()) {
                    SingleTapEvent event = new SingleTapEvent(mapView, v, v1);
                    emitter.onNext(event);
                }
            }
        };
        mapView.setOnSingleTapListener(listener);
        emitter.setDisposable(new MainThreadDisposable() {
            @Override
            protected void onDispose() {
                mapView.setOnSingleTapListener(null);
            }
        });
    }
}
