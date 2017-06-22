package com.palmap.exhibition.widget.overlayer;

import android.widget.ImageView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.NavigationPointModel;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.core.Types;
import com.palmaplus.nagrand.view.MapView;

/**
 * Created by aoc on 2016/6/20.
 */
public class EndMark extends ImageView implements ExFloorMark{

    private double[] position;

    private MapView mapView;

    private NavigationPointModel navigationPointModel;

    public EndMark(MapView context,NavigationPointModel navigationPointModel) {
        super(context.getContext());
        this.mapView = context;
        this.navigationPointModel = navigationPointModel;
        setImageResource(R.mipmap.ico_map_marker_e);
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
        LogUtil.e("起点即将移动屏幕位置:" + x + "<==>" + y);
        Types.Point point = mapView.converToWorldCoordinate(x, y);
        init(new double[]{point.x, point.y});
    }

    @Override
    public double[] getGeoCoordinate() {
        return position;
    }

    public double getWorldX(){
        return position[0];
    }

    public double getWorldY(){
        return position[1];
    }

    @Override
    public void position(double[] doubles) {
        setX((float) doubles[0] - getWidth() / 2);
        setY((float) doubles[1] - getHeight());
    }

    public String getAddress() {
        return navigationPointModel.getAddress();
    }

    public String getFloorName() {
        return navigationPointModel.getFloorName();
    }

    public long getPoiId() {
        return navigationPointModel.getPoiId();
    }

    @Override
    public long getFloorId() {
        return navigationPointModel.getFloorId();
    }

    public NavigationPointModel getNavigationPointModel() {
        return navigationPointModel;
    }

    public void setNavigationPointModel(NavigationPointModel navigationPointModel) {
        this.navigationPointModel = navigationPointModel;
    }
}