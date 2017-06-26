package com.palmap.library.rx.mapView;

import com.palmap.library.rx.mapView.event.ChangePlanarGraphEvent;
import com.palmap.library.rx.mapView.event.LocationEvent;
import com.palmap.library.rx.mapView.event.MapViewZoomEvent;
import com.palmap.library.rx.mapView.event.SingleTapEvent;
import com.palmaplus.nagrand.position.PositioningManager;
import com.palmaplus.nagrand.view.MapView;

import rx.Observable;

import static com.palmap.library.utils.Preconditions.checkNotNull;

/**
 * Created by 王天明 on 2016/5/4.
 */
public class RxMapView {


    public void test(){
        MapView mapView;

    }

    /**
     * 设置MapoView缩放事件
     * @param mapView
     * @return
     */
    public static final Observable<MapViewZoomEvent> zoomEvent(MapView mapView){
        checkNotNull(mapView, "mapView == null");
        return Observable.create(new MapViewZoomEventOnSubscribe(mapView));
    }

    /**
     * 设置MapoView单击事件
     * @param mapView
     * @return
     */
    public static final Observable<SingleTapEvent> singleTapEvent(MapView mapView){
        checkNotNull(mapView, "mapView == null");
        return Observable.create(new SingTapEventOnSubscribe(mapView));
    }

    /**
     * 设置定位事件
     * @param locationPositioningManager
     * @return
     */
    public static final Observable<LocationEvent> locationEvent(PositioningManager locationPositioningManager){
        checkNotNull(locationPositioningManager, "locationPositioningManager == null");
        return Observable.create(new LocationEventOnSubscribe(locationPositioningManager));
    }

    /**
     * 图像changeEvent
     * @param mapView
     * @return
     */
    public static final Observable<ChangePlanarGraphEvent> changePlanarGraphEvent(MapView mapView){
        checkNotNull(mapView, "mapView == null");
        return Observable.create(new ChangePlanarGraphOnSubscribe(mapView));
    }

}
