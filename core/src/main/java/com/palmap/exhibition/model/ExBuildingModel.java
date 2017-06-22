package com.palmap.exhibition.model;

import com.palmap.exhibition.config.MapParam;
import com.palmap.library.model.LocationType;
import com.palmaplus.nagrand.data.LocationModel;

/**
 * Created by 王天明 on 2016/7/19.
 */
public class ExBuildingModel {


    private long buildingId;

    private String name;

    private LocationType.Type locationType;


    public ExBuildingModel() {

    }

    public ExBuildingModel(LocationModel locationModel) {
        this.buildingId = MapParam.getId(locationModel);
        this.name = MapParam.getName(locationModel);
        this.locationType = new LocationType(locationModel).type;
    }

    public long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(long buildingId) {
        this.buildingId = buildingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationType.Type getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType.Type locationType) {
        this.locationType = locationType;
    }
}
