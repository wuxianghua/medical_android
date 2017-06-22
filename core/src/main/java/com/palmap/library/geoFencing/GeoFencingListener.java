package com.palmap.library.geoFencing;

import com.palmaplus.nagrand.geos.Coordinate;

/**
 * Created by 王天明 on 2016/9/9.
 */
public interface GeoFencingListener<O> {

    void onFencingEvent(GeoFencing<O> geoFencing);

    void onNullFencingEvent(long floorId,Coordinate event);
}