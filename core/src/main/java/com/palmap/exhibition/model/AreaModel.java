package com.palmap.exhibition.model;

import com.palmap.library.model.PointD;

/**
 * Created by 王天明 on 2017/7/5.
 */

public class AreaModel {

    private long floorId;
    private PointD topLeft;
    private PointD bottomRight;
    private String name;

    public long getFloorId() {
        return floorId;
    }

    public void setFloorId(long floorId) {
        this.floorId = floorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PointD getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(PointD topLeft) {
        this.topLeft = topLeft;
    }

    public PointD getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(PointD bottomRight) {
        this.bottomRight = bottomRight;
    }
}
