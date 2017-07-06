package com.palmap.library.geoFencing.impl;

import com.palmap.library.model.PointD;
import com.palmaplus.nagrand.geos.Coordinate;

/**
 * Created by 王天明 on 2017/7/5.
 */
public abstract class RectGeoFencing<P> extends SimpleGeoFencing {

    private class Rect{

        private double left,top,right,bottom;

        private Rect(double left, double top, double right, double bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public boolean contains(double x,double y){
            return left < right && top > bottom
                    && x >= left && x < right && y <= top && y > bottom;
        }
    }

    private Rect rect;

    public RectGeoFencing(long floorId, PointD topLeft,PointD bottomRight) {
        super(floorId);
        rect = new Rect(
                topLeft.x,
                topLeft.y,
                bottomRight.x,
                bottomRight.y
        );
    }

    @Override
    protected boolean handlerLocation(Coordinate coordinate, long locationFloorId) {
        double x = coordinate.getX();
        double y = coordinate.getY();
        return rect.contains(x,y);
    }
}
