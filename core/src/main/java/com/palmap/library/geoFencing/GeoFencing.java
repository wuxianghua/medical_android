package com.palmap.library.geoFencing;

import com.palmaplus.nagrand.geos.Coordinate;

/**
 * Created by 王天明 on 2016/9/9.
 * 地理围栏
 */
public interface GeoFencing<O> {

    boolean eventLocation(long floorId,Coordinate coordinate);
    O obtainGeoData();
}