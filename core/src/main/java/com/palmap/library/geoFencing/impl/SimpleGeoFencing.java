package com.palmap.library.geoFencing.impl;

import com.palmap.library.geoFencing.GeoFencing;
import com.palmaplus.nagrand.geos.Coordinate;

/**
 * Created by aoc on 2016/9/9.
 */
public abstract class SimpleGeoFencing implements GeoFencing {

    private long floorId;

    public SimpleGeoFencing(long floorId) {
        this.floorId = floorId;
    }

    @Override
    public boolean eventLocation(long locationFloorId,Coordinate coordinate) {
        //Location location = locationEvent.getNewlocation();
        if (this.floorId != locationFloorId) return false;
        return handlerLocation(coordinate,locationFloorId);
    }

    protected abstract boolean handlerLocation(Coordinate coordinate,long locationFloorId);

}
