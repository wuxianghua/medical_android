package com.palmap.exhibition.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.palmap.library.model.LocationType;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.data.LocationModel;
import com.palmaplus.nagrand.data.Param;

/**
 * Created by 王天明 on 2016/6/3.
 */
public class ExFloorModel implements Parcelable {
    private String name;
    private LocationType type;
    private long id;

    private static final Param<String> floor_field = new Param<>("address", String.class);

    public ExFloorModel(LocationModel locationModel) {
        name = floor_field.get(locationModel);
        type = new LocationType(locationModel);
        id = LocationModel.id.get(locationModel);
    }

    public ExFloorModel(String name, LocationType type, long id) {
        this.name = name;
        this.type = type;
        this.id = id;
    }

    /**
     * 是不是楼层
     *
     * @return
     */
    public boolean isFloor() {
        LogUtil.d("type:" + type.type);
        return type.type.equals(LocationType.Type.FLOOR);
    }


    public String getName() {
        return name;
    }

    public String getShortName() {
        if (name.contains("-")) {
            return name.substring(name.lastIndexOf("-") + 1, name.length());
        }
        return name;
    }

    public long getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeSerializable(this.type);
        dest.writeLong(this.id);
    }

    protected ExFloorModel(Parcel in) {
        this.name = in.readString();
        this.type = (LocationType) in.readSerializable();
        this.id = in.readLong();
    }

    public static final Parcelable.Creator<ExFloorModel> CREATOR = new Parcelable.Creator<ExFloorModel>() {
        @Override
        public ExFloorModel createFromParcel(Parcel source) {
            return new ExFloorModel(source);
        }

        @Override
        public ExFloorModel[] newArray(int size) {
            return new ExFloorModel[size];
        }
    };
}
