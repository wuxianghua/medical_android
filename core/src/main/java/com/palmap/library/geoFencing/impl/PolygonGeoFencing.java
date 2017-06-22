package com.palmap.library.geoFencing.impl;

import com.palmap.library.model.PointD;
import com.palmap.library.utils.Algorithm;
import com.palmaplus.nagrand.geos.Coordinate;

/**
 * Created by 王天明 on 2016/9/9.
 * 多边形的地理围栏
 */
public abstract class PolygonGeoFencing<P> extends SimpleGeoFencing {

    /**
     * 多边形每个点
     * 必须按照顺序能够构造成多边形
     * 不能随便顺序
     */
    private PointD[] pointDs;

    public PolygonGeoFencing(long floorId, PointD[] pointDs) {
        super(floorId);
        this.pointDs = pointDs;
    }

    /**
     * 使用射线方法判断 一个点在不在多边形内
     * @param coordinate
     * @param locationFloorId
     * @return
     */
    @Override
    protected boolean handlerLocation(Coordinate coordinate, long locationFloorId) {
        double x = coordinate.getX();
        double y = coordinate.getY();
        return Algorithm.insidePolygon(pointDs, x, y);
    }
}
