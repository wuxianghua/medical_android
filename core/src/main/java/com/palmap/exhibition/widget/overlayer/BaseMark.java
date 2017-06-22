package com.palmap.exhibition.widget.overlayer;

import android.widget.LinearLayout;

import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.overlay.OverlayCell;

/**
 * Created by 王天明 on 2016/5/4.
 */
public class BaseMark extends LinearLayout implements OverlayCell,ExFloorMark {

    private double[] position;

    protected MapView mapView;
    protected long floorId;

    public BaseMark(MapView mapView,long floorId) {
        super(mapView.getContext());
        this.mapView = mapView;
        this.floorId = floorId;
    }

    @Override
    public void init(double[] doubles) {
        position = doubles;
    }

    @Override
    public double[] getGeoCoordinate() {
        return position;
    }

    @Override
    public void position(double[] doubles) {
        setX((float) doubles[0] - getWidth() / 2);
        setY((float) doubles[1] - getHeight());
    }

    @Override
    public long getFloorId() {
        return floorId;
    }
}
