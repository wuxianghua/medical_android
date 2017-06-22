package com.palmap.exhibition.launcher;

import android.os.Parcel;
import android.os.Parcelable;

import com.palmap.exhibition.exception.LauncherArgsException;

/**
 * Created by aoc on 2016/9/14.
 */
public class LauncherModel implements Parcelable {


    //private long poiId;
    private long mapId;
    private long floorId;
    private long featureId;
    private String title;

    /**
     * 是否是行程规划
     */
    private boolean isTravelPlanning = false;

    public LauncherModel(long mapId, long floorId, long featureId, String title) {
        //this.poiId = poiId;
        this.mapId = mapId;
        this.floorId = floorId;
        this.featureId = featureId;
        this.title = title;
    }

    public LauncherModel(long mapId, long floorId, long featureId, String title, boolean isTravelPlanning) {
        this(mapId, floorId, featureId, title);
        this.isTravelPlanning = isTravelPlanning;
    }

    public void checkArgs() {
//        return !(poiId <= 0 || mapId <= -1);
        if (mapId <= 0) {
            throw new LauncherArgsException("mapId error:" + mapId);
        }
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public long getFloorId() {
        return floorId;
    }

    public void setFloorId(long floorId) {
        this.floorId = floorId;
    }

    public long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(long featureId) {
        this.featureId = featureId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mapId);
        dest.writeLong(this.floorId);
        dest.writeLong(this.featureId);
        dest.writeString(this.title);
    }

    public LauncherModel() {
    }

    protected LauncherModel(Parcel in) {
        this.mapId = in.readLong();
        this.floorId = in.readLong();
        this.featureId = in.readLong();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<LauncherModel> CREATOR = new Parcelable.Creator<LauncherModel>() {
        @Override
        public LauncherModel createFromParcel(Parcel source) {
            return new LauncherModel(source);
        }

        @Override
        public LauncherModel[] newArray(int size) {
            return new LauncherModel[size];
        }
    };

    @Override
    public String toString() {
        return "LauncherModel{" +
                ", mapId=" + mapId +
                ", floorId=" + floorId +
                ", featureId=" + featureId +
                ", title='" + title + '\'' +
                '}';
    }

    public boolean isTravelPlanning() {
        return isTravelPlanning;
    }

    public void setTravelPlanning(boolean travelPlanning) {
        isTravelPlanning = travelPlanning;
    }
}
