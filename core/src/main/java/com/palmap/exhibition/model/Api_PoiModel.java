package com.palmap.exhibition.model;

import java.util.List;

/**
 * Created by aoc on 2016/6/16.
 */
public class Api_PoiModel {

    private int returnCode;
    private Object errorMsg;

    private ObjBean obj;

    public boolean isOk() {
        return returnCode == 0;
    }

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

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        private int poiId;
        private int mapId;
        private Object buildId;
        private int floorId;
        private String roomNumber;
        private String logo;
        private String companyName;
        private String address;
        private String description;
        private String adColumns;
        private String telephone;
        private String email;
        private String companyURL;
        private String wechatName;
        private String wechatNumber;
        private String qrCode;
        private String redirectURL;
        private String ext;
        private List<String> adList;

        public int getPoiId() {
            return poiId;
        }

        public void setPoiId(int poiId) {
            this.poiId = poiId;
        }

        public int getMapId() {
            return mapId;
        }

        public void setMapId(int mapId) {
            this.mapId = mapId;
        }

        public Object getBuildId() {
            return buildId;
        }

        public void setBuildId(Object buildId) {
            this.buildId = buildId;
        }

        public int getFloorId() {
            return floorId;
        }

        public void setFloorId(int floorId) {
            this.floorId = floorId;
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        public void setRoomNumber(String roomNumber) {
            this.roomNumber = roomNumber;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAdColumns() {
            return adColumns;
        }

        public void setAdColumns(String adColumns) {
            this.adColumns = adColumns;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCompanyURL() {
            return companyURL;
        }

        public void setCompanyURL(String companyURL) {
            this.companyURL = companyURL;
        }

        public String getWechatName() {
            return wechatName;
        }

        public void setWechatName(String wechatName) {
            this.wechatName = wechatName;
        }

        public String getWechatNumber() {
            return wechatNumber;
        }

        public void setWechatNumber(String wechatNumber) {
            this.wechatNumber = wechatNumber;
        }

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        public String getRedirectURL() {
            return redirectURL;
        }

        public void setRedirectURL(String redirectURL) {
            this.redirectURL = redirectURL;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public List<String> getAdList() {
            return adList;
        }

        public void setAdList(List<String> adList) {
            this.adList = adList;
        }
    }
}
