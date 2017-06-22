package com.palmap.exhibition.widget.overlayer;

import com.palmaplus.nagrand.view.MapView;

/**
 * Created by 天明 on 2016/6/29.
 * 搜索结果指示mark
 */
public class PoiMark extends TapMark {

    private long locationId;

    public PoiMark(MapView context,long floorId, long locationId) {
        super(context,floorId);
        this.locationId = locationId;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }
}
