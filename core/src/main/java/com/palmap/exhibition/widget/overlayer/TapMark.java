package com.palmap.exhibition.widget.overlayer;

import android.widget.ImageView;

import com.palmap.exhibition.R;
import com.palmaplus.nagrand.core.Types;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.overlay.OverlayCell;

/**
 * Created by 王天明 on 2016/6/23.
 */
public class TapMark extends ImageView implements OverlayCell {

    private double[] position;

    private MapView mapView;

    private long floorId;

    public TapMark(MapView context,long floorId) {
        super(context.getContext());
        this.mapView = context;
        this.floorId = floorId;
//        setImageResource(R.mipmap.ico_marker);
        setImageResource(R.mipmap.ico_public_marker_r);
    }

    @Override
    public void init(double[] doubles) {
        position = doubles;
    }

    /**
     * 屏幕坐标
     *
     * @param x
     * @param y
     */
    public void init(double x, double y) {
        Types.Point point = mapView.converToWorldCoordinate(x, y);
        init(new double[]{point.x, point.y});
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
