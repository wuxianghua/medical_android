package com.palmap.exhibition.fencing;

import com.palmap.exhibition.model.PushModel;
import com.palmap.library.geoFencing.impl.CircleGeoFencing;

/**
 * Created by 王天明 on 2016/9/9.
 */
public class PushCircleFencing extends CircleGeoFencing<PushModel> {

    private PushModel model;

    public PushCircleFencing(long floorId, double centerX, double centerY, double r, PushModel model) {
        super(floorId, centerX, centerY, r);
        this.model = model;
    }

    @Override
    public PushModel obtainGeoData() {
        return model;
    }
}