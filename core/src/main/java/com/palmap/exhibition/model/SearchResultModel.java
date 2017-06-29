package com.palmap.exhibition.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;

import com.palmap.exhibition.config.MapParam;
import com.palmap.library.model.LocationType;
import com.palmaplus.nagrand.data.LocationModel;

/**
 * Created by 王天明 on 2016/7/18.
 */
public class SearchResultModel implements Comparable<SearchResultModel>, Parcelable {

    private String name;
    private long id;
    private LocationType.Type locationType = LocationType.Type.LOCATION;
    private String venueName;
    private boolean isMeeting = false;

    private long mapId = -1;
    private long floorId;

    private String startTime;
    private String endTime;

    private String address;
    private Spannable nameSpan;

    public SearchResultModel(LocationModel locationModel, Spannable nameSpan) {
        name = MapParam.getDisplay(locationModel);
        id = MapParam.getId(locationModel);
        this.nameSpan = nameSpan;
        floorId = LocationModel.parent.get(locationModel);
        locationType = new LocationType(locationModel).type;
        address = LocationModel.address.get(locationModel);
    }

    public SearchResultModel(Api_ActivityInfo.ObjBean objBean) {
        name = objBean.getActivityName();
        id = objBean.getPoiId();
        floorId = objBean.getFloorId();
        locationType = LocationType.Type.LOCATION;
        isMeeting = true;
        mapId = objBean.getMapId();
        address = objBean.getRoomNumber();
        startTime = objBean.getStartTime();
        endTime = objBean.getEndTime();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocationType.Type getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType.Type locationType) {
        this.locationType = locationType;
    }

    public Spannable getNameSpan() {
        return nameSpan;
    }

    public void setNameSpan(Spannable nameSpan) {
        this.nameSpan = nameSpan;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public boolean isMeeting() {
        return isMeeting;
    }

    public long getFloorId() {
        return floorId;
    }

    public void setFloorId(long floorId) {
        this.floorId = floorId;
    }

    public void setMeeting(boolean meeting) {
        isMeeting = meeting;
    }

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public int compareTo(SearchResultModel another) {
        try {
            return Integer.parseInt(this.address) - Integer.parseInt(another.address);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeLong(this.id);
        dest.writeInt(this.locationType == null ? -1 : this.locationType.ordinal());
        dest.writeString(this.venueName);
        dest.writeByte(this.isMeeting ? (byte) 1 : (byte) 0);
        dest.writeLong(this.mapId);
        dest.writeLong(this.floorId);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.address);
    }

    protected SearchResultModel(Parcel in) {
        this.name = in.readString();
        this.id = in.readLong();
        int tmpLocationType = in.readInt();
        this.locationType = tmpLocationType == -1 ? null : LocationType.Type.values()[tmpLocationType];
        this.venueName = in.readString();
        this.isMeeting = in.readByte() != 0;
        this.mapId = in.readLong();
        this.floorId = in.readLong();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.address = in.readString();
        this.nameSpan = null;
    }

    public static final Parcelable.Creator<SearchResultModel> CREATOR = new Parcelable.Creator<SearchResultModel>() {
        @Override
        public SearchResultModel createFromParcel(Parcel source) {
            return new SearchResultModel(source);
        }

        @Override
        public SearchResultModel[] newArray(int size) {
            return new SearchResultModel[size];
        }
    };
}
