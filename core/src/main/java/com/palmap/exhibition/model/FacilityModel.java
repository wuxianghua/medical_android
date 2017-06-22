package com.palmap.exhibition.model;

import java.util.ArrayList;

/**
 * Created by 王天明 on 2016/6/6.
 * 设施
 */
public class FacilityModel {

    private long id;
    private String name;
    private int resId;
    /**
     * 它可以代表一类设施
     */
    private ArrayList<Long> ids;

    private double z;

    public FacilityModel() {
    }

    public FacilityModel(String name, ArrayList<Long> ids, int resId) {
        this.name = name;
        this.ids = ids;
        this.resId = resId;
    }

    public FacilityModel(long id, String name, int resId) {
        this.id = id;
        this.name = name;
        this.resId = resId;
    }

    public FacilityModel(long id, int resId) {
        this.id = id;
        this.resId = resId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public ArrayList<Long> getIds() {
        return ids;
    }

    public void setIds(ArrayList<Long> ids) {
        this.ids = ids;
    }
}
