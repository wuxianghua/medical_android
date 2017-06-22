package com.palmap.exhibition.model;

/**
 * Created by 王天明 on 2016/8/30.
 */
public class ExMapModel {

    private String name;
    private String eName;
    private String address;
    private int resId;
    private long mapId;
    private String imgUrl;

    public ExMapModel() {
    }

    public ExMapModel(String name, String eName, long mapId,int resId) {
        this.name = name;
        this.eName = eName;
        this.mapId = mapId;
        this.resId = resId;
        this.address = eName;
    }

    public ExMapModel(String name, String eName, long mapId,String imgUrl) {
        this.name = name;
        this.eName = eName;
        this.mapId = mapId;
        this.imgUrl = imgUrl;
        this.address = eName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }
}
