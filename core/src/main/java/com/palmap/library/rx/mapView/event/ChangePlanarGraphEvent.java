package com.palmap.library.rx.mapView.event;

import com.palmaplus.nagrand.data.PlanarGraph;

/**
 * Created by 王天明 on 2016/5/4.
 * MapView图像渲染改变事件
 */
public class ChangePlanarGraphEvent {

    /**
     * 旧的楼层数据
     */
    private PlanarGraph oldPlanarGraph;
    /**
     * 新的楼层数据
     */
    private PlanarGraph newPlanarGraph;

    /**
     * 旧楼层ID
     */
    private long oldFloorId;
    /**
     * 新楼层ID
     */
    private long newFloorId;

    public ChangePlanarGraphEvent(PlanarGraph oldPlanarGraph, PlanarGraph newPlanarGraph, long oldFloorId, long newFloorId) {
        this.oldPlanarGraph = oldPlanarGraph;
        this.newPlanarGraph = newPlanarGraph;
        this.oldFloorId = oldFloorId;
        this.newFloorId = newFloorId;
    }

    public PlanarGraph getOldPlanarGraph() {
        return oldPlanarGraph;
    }

    public PlanarGraph getNewPlanarGraph() {
        return newPlanarGraph;
    }

    public long getOldFloorId() {
        return oldFloorId;
    }

    public long getNewFloorId() {
        return newFloorId;
    }
}
