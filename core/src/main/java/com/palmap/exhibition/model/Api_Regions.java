package com.palmap.exhibition.model;

import com.palmap.library.model.PointD;

import java.util.ArrayList;

/**
 * Created by 王天明 on 2016/9/9.
 */
public class Api_Regions {

    private int code;

    private String errorMsg;

    private ArrayList<RegionObj> regionObjs;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ArrayList<RegionObj> getRegionObjs() {
        return regionObjs;
    }

    public void setRegionObjs(ArrayList<RegionObj> regionObjs) {
        this.regionObjs = regionObjs;
    }

    public static class RegionObj {

        private int type;
        private double r;
        private Object data;
        private long floorId;
        private long buildingId;
        private PointD[] pointDs;

        public RegionObj() {
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public double getR() {
            return r;
        }

        public void setR(double r) {
            this.r = r;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public long getFloorId() {
            return floorId;
        }

        public void setFloorId(long floorId) {
            this.floorId = floorId;
        }

        public long getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(long buildingId) {
            this.buildingId = buildingId;
        }

        public PointD[] getPointDs() {
            return pointDs;
        }

        public void setPointDs(PointD[] pointDs) {
            this.pointDs = pointDs;
        }
    }
}