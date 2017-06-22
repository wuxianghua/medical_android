package com.palmap.exhibition.other;

import java.util.HashMap;

/**
 * Created by 王天明 on 2016/7/20.
 */
public class MapAngleHelper {

    private static final HashMap<Integer, Double> angleMapping = new HashMap<>();

    static {
        angleMapping.put(6, 24.2901);//图聚办公室
        angleMapping.put(2061, 70.0);//烟台
    }

    public static double getAngleWithMapId(int mapId) {
        return angleMapping.get(mapId) == null ? 0 : angleMapping.get(mapId);
    }
}
