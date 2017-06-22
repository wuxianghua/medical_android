package com.palmap.exhibition.widget.overlayer;

import android.content.Context;
import android.widget.ImageView;

import com.palmap.exhibition.R;
import com.palmaplus.nagrand.view.overlay.OverlayCell;

/**
 * Created by 王天明 on 2016/6/16.
 * <p/>
 * 公共设置mark
 */
public class PubFacilityMark extends ImageView implements OverlayCell {

    private double[] position;

    private long floorId;

    public PubFacilityMark(Context context,long floorId) {
        super(context);
        this.floorId = floorId;
//        setImageResource(R.mipmap.mark_pub);
        setImageResource(R.mipmap.ico_public_marker_b);
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