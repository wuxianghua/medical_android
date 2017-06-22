package com.palmap.library.rx.mapView.event;

import com.palmaplus.nagrand.view.MapView;

/**
 * Created by 王天明 on 2016/5/4.
 */
public class MapViewZoomEvent {

    public final MapView mapView;

    public PreZoomEvent preZoomEvent;
    public OnZoomEvent onZoomEvent;
    public PostZoomEvent postZoomEvent;
    public final State state;

    public enum State {
        PRE,
        ON,
        POST
    }

    public MapViewZoomEvent(MapView mapView, float v, float v1) {
        this.mapView = mapView;
        preZoomEvent = new PreZoomEvent(v, v1);
        state = State.PRE;
    }

    public MapViewZoomEvent(MapView mapView, boolean b) {
        this.mapView = mapView;
        onZoomEvent = new OnZoomEvent(b);
        state = State.ON;
    }

    public MapViewZoomEvent(float v, float v1, MapView mapView) {
        this.mapView = mapView;
        postZoomEvent = new PostZoomEvent(v, v1);
        state = State.POST;
    }

    public class PreZoomEvent {
        public float f1;
        public float f2;

        public PreZoomEvent(float f1, float f2) {
            this.f1 = f1;
            this.f2 = f2;
        }
    }

    public class OnZoomEvent {
        public boolean b;

        public OnZoomEvent(boolean b) {
            this.b = b;
        }
    }

    public class PostZoomEvent {
        public float f1;
        public float f2;

        public PostZoomEvent(float f1, float f2) {
            this.f1 = f1;
            this.f2 = f2;
        }
    }

}
