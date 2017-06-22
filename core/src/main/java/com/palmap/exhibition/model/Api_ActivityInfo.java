package com.palmap.exhibition.model;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by 王天明 on 2016/9/19.
 * 活动详情model
 */
public class Api_ActivityInfo {

    private int returnCode;
    private String errorMsg;
    private List<ObjBean> obj;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public Api_ActivityInfo.ObjBean getObjBeanWithId(int id) {
        if (obj == null || obj.size() == 0) {
            return null;
        }
        for (Api_ActivityInfo.ObjBean b :
                obj) {
            if (b.getId() == id) return b;
        }
        return null;
    }

    public static class ObjBean {
        private int id;
        private int mapId;
        private int buildId;
        private int floorId;
        private int poiId;
        private String activityType;
        private String startTime;
        private String endTime;
        private String activityName;
        private String activityDesc;
        private String roomNumber;
        private String ext;

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

        public int getBuildId() {
            return buildId;
        }

        public void setBuildId(int buildId) {
            this.buildId = buildId;
        }

        public int getFloorId() {
            return floorId;
        }

        public void setFloorId(int floorId) {
            this.floorId = floorId;
        }

        public int getPoiId() {
            return poiId;
        }

        public void setPoiId(int poiId) {
            this.poiId = poiId;
        }

        public String getActivityType() {
            return activityType;
        }

        public void setActivityType(String activityType) {
            this.activityType = activityType;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public String getActivityDesc() {
            return activityDesc;
        }

        public void setActivityDesc(String activityDesc) {
            this.activityDesc = activityDesc;
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        public void setRoomNumber(String roomNumber) {
            this.roomNumber = roomNumber;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        /**
         * 活动类型2（点亮活动） 的分享url存放在ext1拓展字段中
         * @return
         */
        public String getShareUrl() {
            if ("2".equals(activityType)) {
                if (ext == null) {
                    return null;
                }
                try {
                    return new Gson().fromJson(ext, Api_Ext.class).getExt1();
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        }
    }
}
