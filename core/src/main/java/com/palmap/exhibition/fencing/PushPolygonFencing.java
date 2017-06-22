package com.palmap.exhibition.fencing;

import com.palmap.exhibition.model.PushModel;
import com.palmap.library.geoFencing.impl.PolygonGeoFencing;
import com.palmap.library.model.PointD;

/**
 * Created by 王天明 on 2016/9/9.
 */
public class PushPolygonFencing extends PolygonGeoFencing<PushModel> {

    private PushModel model;

    public PushPolygonFencing(long floorId, PointD[] pointDs, PushModel model) {
        super(floorId, pointDs);
        this.model = model;
    }

    @Override
    public PushModel obtainGeoData() {
        return model;
    }
}