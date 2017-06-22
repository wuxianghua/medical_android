package com.palmap.library.model;

import android.text.TextUtils;

import com.palmaplus.nagrand.data.DataElement;
import com.palmaplus.nagrand.data.Feature;
import com.palmaplus.nagrand.data.LocationModel;
import com.palmaplus.nagrand.data.Param;

import java.io.Serializable;

/**
 * Created by 王天明 on 2016/5/11.
 *
 * 封装Location类型 sdk为string类型
 */
public class LocationType implements Serializable{

    public enum Type {
        PLANAR_GRAPH,
        FLOOR,
        BUILDING,
        LOCATION
    }

    public final Type type;

    private static final Param<String> LOCATION_TYPE = new Param<String>("location_type", String.class);

    public LocationType(String typeName) {
        if (TextUtils.isEmpty(typeName)) {
            type = Type.LOCATION;
            return;
        }
        switch (typeName.toUpperCase()) {
            case "FLOOR":
                type = Type.FLOOR;
                break;
            case "PLANAR_GRAPH":
                type = Type.PLANAR_GRAPH;
                break;
            case "BUILDING":
                type = Type.BUILDING;
                break;
            default:
                type = Type.LOCATION;
                break;
        }
    }

    public LocationType(DataElement dataElement) {
        this(LocationModel.type.get(dataElement));
    }

    public LocationType(Feature feature) {
        this(LOCATION_TYPE.get(feature));
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
