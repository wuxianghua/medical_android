package com.palmap.library.geoFencing;

import java.util.List;

import rx.Observable;

/**
 * Created by 王天明 on 2016/9/9.
 * 围栏提供者
 */
public interface GeoFencingProvides<O> {
    Observable<List<GeoFencing<O>>> providesGeoFencing();
}
