package com.palmap.library.geoFencing.impl;

import com.palmap.library.utils.MapUtils;
import com.palmaplus.nagrand.geos.Coordinate;

/**
 * Created by 王天明 on 2016/9/9.
 * 圆形的地理围栏
 */
public abstract class CircleGeoFencing<P> extends SimpleGeoFencing {

    /**
     * 圆心坐标
     */
    private double centerX, centerY;
    /**
     * 半径
     */
    private double r;

    public CircleGeoFencing(long floorId, double centerX, double centerY, double r) {
        super(floorId);
        this.centerX = centerX;
        this.centerY = centerY;
        this.r = r;
    }

    /**
     * 使用2点的距离来判断
     *
     * @param coordinate
     * @param locationFloorId
     * @return
     */
    @Override
    protected boolean handlerLocation(Coordinate coordinate, long locationFloorId) {
        return r > MapUtils.pointDistance(centerX, centerY, coordinate.getX(), coordinate.getY());
    }
}
