package com.palmap.library.rx.mapView.event;

import com.palmaplus.nagrand.view.MapView;

/**
 * Created by aoc on 2016/5/5.
 */
public class SingleTapEvent {

    private MapView mapView;
    private float x, y;

    public SingleTapEvent(MapView mapView, float x, float y) {
        this.mapView = mapView;
        this.x = x;
        this.y = y;
    }

    public MapView getMapView() {
        return mapView;
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
