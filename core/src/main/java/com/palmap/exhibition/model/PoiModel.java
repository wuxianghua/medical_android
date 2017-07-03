package com.palmap.exhibition.model;

import android.text.TextUtils;

import com.palmap.exhibition.config.MapParam;
import com.palmap.library.model.LocationType;
import com.palmaplus.nagrand.data.DataElement;
import com.palmaplus.nagrand.data.Feature;

/**
 * Created by 王天明 on 2016/6/7.
 */
public class PoiModel {

    private String name = "";
    private String logo = "";
    private String describe = "";
    private String disPlay = "";
    private long categoryId;
    private LocationType type;
    private String address = "";
    private String floorName = "";

    /**
     * 是不是设施
     */
    private boolean isFaciliy = false;

    /**
     * 代表世界坐标
     */
    private double x;
    private double y;
    private double z;

    private long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public String getDisPlay() {
        if (TextUtils.isEmpty(disPlay)) {
            return name;
        }
        return disPlay;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public void setDisPlay(String disPlay) {
        this.disPlay = disPlay;
    }

    public boolean isFaciliy() {
        return isFaciliy;
    }

    public void setFaciliy(boolean faciliy) {
        isFaciliy = faciliy;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getAddress() {
        if (address == null || "null".equals(address.toLowerCase().toLowerCase())) return "";
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PoiModel(Feature feature) {
        if (feature!=null){
            x = feature.getCentroid().getX();
            y = feature.getCentroid().getY();
            z = feature.getCentroid().getZ();
            id = MapParam.getId(feature);
            name = MapParam.getName(feature);
            type = new LocationType(feature);
            disPlay = MapParam.getDisplay(feature);
            categoryId = MapParam.getCategoryId(feature);
            address = MapParam.getAddress(feature);
        }
    }

    public PoiModel(DataElement dataElement) {
        if (dataElement != null) {
            id = MapParam.getId(dataElement);
            name = MapParam.getName(dataElement);
            disPlay = MapParam.getDisplay(dataElement);
            type = new LocationType(dataElement);
            categoryId = MapParam.getCategoryId(dataElement);
            address = MapParam.getAddress(dataElement);
        }
    }

    public PoiModel() {
    }

    public PoiModel(long id, double x, double y, double z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

