package com.palmap.exhibition.model;

/**
 * Created by 王天明 on 2016/9/13.
 */
public class PushModel {

    private String name;
    private String logo;
    private String describe;

    public PushModel() {
    }

    public PushModel(String name, String logo, String describe) {
        this.name = name;
        this.logo = logo;
        this.describe = describe;
    }

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
}
