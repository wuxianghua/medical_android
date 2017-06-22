package com.palmap.exhibition.model;

import java.util.List;

/**
 * Created by 王天明 on 2016/10/19.
 * 服务器 位置消息model
 */
public class Api_PositionInfo {

    private int returnCode;
    private Object errorMsg;
    private List<ObjBean> obj;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public Object getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Object errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        private int id;
        private int mapId;
        private long floorId;
        private int activityId;
        private double longitude;
        private double latitude;
        private String altitude;
        private double diameter;
        private String logo;
        private String description_zh_cn;
        private String description_en;
        private int type;
        private String htmlCode;
        private int count;
        private String url;
        private int logoResId;

        public int getLogoResId() {
            return logoResId;
        }

        public void setLogoResId(int logoResId) {
            this.logoResId = logoResId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMapId() {
            return mapId;
        }

        public void setMapId(int mapId) {
            this.mapId = mapId;
        }

        public long getFloorId() {
            return floorId;
        }

        public void setFloorId(long floorId) {
            this.floorId = floorId;
        }

        public int getActivityId() {
            return activityId;
        }

        public void setActivityId(int activityId) {
            this.activityId = activityId;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getAltitude() {
            return altitude;
        }

        public void setAltitude(String altitude) {
            this.altitude = altitude;
        }

        public double getDiameter() {
            return diameter;
        }

        public void setDiameter(double diameter) {
            this.diameter = diameter;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getDescription_zh_cn() {
            return description_zh_cn;
        }

        public void setDescription_zh_cn(String description_zh_cn) {
            this.description_zh_cn = description_zh_cn;
        }

        public String getDescription_en() {
            return description_en;
        }

        public void setDescription_en(String description_en) {
            this.description_en = description_en;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getHtmlCode() {
            return htmlCode;
        }

        public void setHtmlCode(String htmlCode) {
            this.htmlCode = htmlCode;
        }
    }
}
