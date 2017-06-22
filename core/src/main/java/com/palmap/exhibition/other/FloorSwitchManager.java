package com.palmap.exhibition.other;

import com.palmap.library.utils.LogUtil;
import com.palmap.library.utils.SynchronizedLinkedQueue;

import java.util.Iterator;

/**
 * Created by 王天明 on 2016/5/12.
 */
public class FloorSwitchManager {

    private final int maxSize;

    private final int DEFAULT_SIZE = 5;

    private SynchronizedLinkedQueue<Long> floorIdQueue;

    private long nextFloorId = -1;

    public FloorSwitchManager(int maxSize) {
        this.maxSize = maxSize == 0 ? DEFAULT_SIZE : maxSize;
        floorIdQueue = new SynchronizedLinkedQueue<>(this.maxSize);
    }

    public void putLocation(long newFloorId) {
        floorIdQueue.add(newFloorId);
        LogUtil.d("添加定位数据:" + newFloorId);
        LogUtil.d(floorIdQueue.toString());
    }

    /**
     * 根据获取到的楼层判断当前是否可以切换楼层
     * @return
     */
    public boolean isCanSwitchFloor() {
        nextFloorId = -1;
        if (floorIdQueue.size() < floorIdQueue.getMaxSize()) {
            return false;
        }
        Iterator<Long> it = floorIdQueue.iterator();
        long currentId = 0;
        while (it.hasNext()) {
            Long id = it.next();
            if (currentId == 0) {
                currentId = id;
                continue;
            }
            //有一个不等 就不是max次相同定位楼层 不可以切换楼层
            if (currentId != id) return false;
        }
        //max次的定位楼层都一直 可以切换楼层
        nextFloorId = currentId;
        return true;
    }

    public long getNextFloorId() {
        return nextFloorId;
    }

    public void setNextFloorId(long nextFloorId) {
        this.nextFloorId = nextFloorId;
    }

    public void clearData(){
        floorIdQueue.clear();
    }
}