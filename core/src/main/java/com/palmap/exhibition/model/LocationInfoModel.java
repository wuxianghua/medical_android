package com.palmap.exhibition.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wtm on 2017/1/3.
 * 基站定位model
 */
public class LocationInfoModel implements Parcelable {

    private String type;
    private PropertiesBean properties;
    private GeometryBean geometry;

    public LocationInfoModel() {
    }

    public LocationInfoModel(double x, double y, double z) {
        this.geometry = new GeometryBean();
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(x);
        coordinates.add(y);
//        coordinates.add(z);
        this.geometry.setCoordinates(coordinates);

        this.properties = new PropertiesBean();
        this.properties.setFloor_id((int)z);
    }

    public double getX(){
        try {
            return this.geometry.coordinates.get(0);
        } catch (Exception e) {
            return -1;
        }
    }

    public double getY(){
        try {
            return this.geometry.coordinates.get(1);
        } catch (Exception e) {
            return -1;
        }
    }

    public double getZ(){
        try {
            return this.properties.getFloor_id();
        } catch (Exception e) {
            return -1;
        }
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PropertiesBean getProperties() {
        return properties;
    }

    public void setProperties(PropertiesBean properties) {
        this.properties = properties;
    }

    public GeometryBean getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryBean geometry) {
        this.geometry = geometry;
    }

    public LocationInfoModel valueClone() {
        LocationInfoModel cloneModel = new LocationInfoModel();
        cloneModel.setType(this.getType());
        cloneModel.setProperties(this.getProperties());
        cloneModel.setGeometry(this.getGeometry());
        return cloneModel;
    }

    public static class PropertiesBean implements Parcelable {

        private String id_data;
        private int scene_id;
        private String id_type;
        private int floor_id;
        private int expires_in;
        private long timestamp;
        private String status;

        public String getId_data() {
            return id_data;
        }

        public void setId_data(String id_data) {
            this.id_data = id_data;
        }

        public int getScene_id() {
            return scene_id;
        }

        public void setScene_id(int scene_id) {
            this.scene_id = scene_id;
        }

        public String getId_type() {
            return id_type;
        }

        public void setId_type(String id_type) {
            this.id_type = id_type;
        }

        public int getFloor_id() {
            return floor_id;
        }

        public void setFloor_id(int floor_id) {
            this.floor_id = floor_id;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id_data);
            dest.writeInt(this.scene_id);
            dest.writeString(this.id_type);
            dest.writeInt(this.floor_id);
            dest.writeInt(this.expires_in);
            dest.writeLong(this.timestamp);
            dest.writeString(this.status);
        }

        public PropertiesBean() {
        }

        protected PropertiesBean(Parcel in) {
            this.id_data = in.readString();
            this.scene_id = in.readInt();
            this.id_type = in.readString();
            this.floor_id = in.readInt();
            this.expires_in = in.readInt();
            this.timestamp = in.readLong();
            this.status = in.readString();
        }

        public static final Creator<PropertiesBean> CREATOR = new Creator<PropertiesBean>() {
            @Override
            public PropertiesBean createFromParcel(Parcel source) {
                return new PropertiesBean(source);
            }

            @Override
            public PropertiesBean[] newArray(int size) {
                return new PropertiesBean[size];
            }
        };
    }

    public static class GeometryBean implements Parcelable {
        /**
         * type : Point
         * coordinates : [1.35121424778915E7,4515046.829209642]
         */
        private String type;
        private List<Double> coordinates;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Double> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Double> coordinates) {
            this.coordinates = coordinates;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.type);
            dest.writeList(this.coordinates);
        }

        public GeometryBean() {
        }

        protected GeometryBean(Parcel in) {
            this.type = in.readString();
            this.coordinates = new ArrayList<Double>();
            in.readList(this.coordinates, Double.class.getClassLoader());
        }

        public static final Creator<GeometryBean> CREATOR = new Creator<GeometryBean>() {
            @Override
            public GeometryBean createFromParcel(Parcel source) {
                return new GeometryBean(source);
            }

            @Override
            public GeometryBean[] newArray(int size) {
                return new GeometryBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeParcelable(this.properties, flags);
        dest.writeParcelable(this.geometry, flags);
    }

    protected LocationInfoModel(Parcel in) {
        this.type = in.readString();
        this.properties = in.readParcelable(PropertiesBean.class.getClassLoader());
        this.geometry = in.readParcelable(GeometryBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<LocationInfoModel> CREATOR = new Parcelable.Creator<LocationInfoModel>() {
        @Override
        public LocationInfoModel createFromParcel(Parcel source) {
            return new LocationInfoModel(source);
        }

        @Override
        public LocationInfoModel[] newArray(int size) {
            return new LocationInfoModel[size];
        }
    };
}
