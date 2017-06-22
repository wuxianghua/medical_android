package com.palmap.exhibition.model;

/**
 * Created by 王天明 on 2016/9/12.
 * 导航点
 */
public class NavigationPointModel {

    private long floorId;

    private long poiId;

    private String floorName;

    private String address;

    public NavigationPointModel() {
    }

    public long getFloorId() {
        return floorId;
    }

    public void setFloorId(long floorId) {
        this.floorId = floorId;
    }

    public long getPoiId() {
        return poiId;
    }

    public void setPoiId(long poiId) {
        this.poiId = poiId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
