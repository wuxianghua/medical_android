package com.palmap.library.utils;

import com.palmaplus.nagrand.core.Value;
import com.palmaplus.nagrand.data.DataList;
import com.palmaplus.nagrand.data.Feature;
import com.palmaplus.nagrand.data.FeatureCollection;
import com.palmaplus.nagrand.data.LocationList;
import com.palmaplus.nagrand.data.LocationModel;
import com.palmaplus.nagrand.data.MapModel;
import com.palmaplus.nagrand.data.Param;
import com.palmaplus.nagrand.data.PlanarGraph;
import com.palmaplus.nagrand.geos.Coordinate;
import com.palmaplus.nagrand.position.ble.Point3D;
import com.palmaplus.nagrand.view.MapView;

/**
 * Created by 王天明 on 2016/5/3.
 */
public class MapUtils {

    /**
     * 获取默认的楼层id
     *
     * @param data
     * @return
     */
    public static long obtainDefaultFloorId(LocationList data) {
        if (data == null || data.getSize() == 0) {
            throw new NullPointerException("data size=0 !!!");
        }
        LocationModel model = null;
        Param<Boolean> def = new Param<Boolean>("default", Boolean.class);
        for (int i = 0, size = data.getSize(); i < size; i++) {
            model = data.getPOI(i);
            if (def.get(model)) {
                return LocationModel.id.get(model);
            }
        }
        return LocationModel.id.get(data.getPOI(0));
    }

    public static LocationModel obtainDefaultLocationModel(LocationList data) {
        if (data == null || data.getSize() == 0) {
            throw new NullPointerException("data size=0 !!!");
        }
        LocationModel model = null;
        Param<Boolean> def = new Param<Boolean>("default", Boolean.class);
        for (int i = 0, size = data.getSize(); i < size; i++) {
            model = data.getPOI(i);
            if (def.get(model)) {
                return model;
            }
        }
        return data.getPOI(0);
    }

    /**
     * 获取默认的楼层id
     *
     * @return
     */
    public static long obtainDefaultFloorId(DataList<MapModel> dataList) {
        if (dataList == null || dataList.getSize() == 0) {
            return 0;
        }
        MapModel model = null;
        Param<Boolean> def = new Param<Boolean>("default", Boolean.class);
        for (int i = 0, size = dataList.getSize(); i < size; i++) {
            model = dataList.getPOI(i);
            if (def.get(model)) {
                return LocationModel.id.get(model);
            }
        }
        return 0;
    }

    /**
     * 判断floorId是否在LocationList内
     *
     * @param dataList
     * @param floorId
     * @return
     */
    public static boolean checkFloorId(LocationList dataList, long floorId) {
        for (int i = 0; i < dataList.getSize(); i++) {
            if (floorId == LocationModel.id.get(dataList.getPOI(i))) {
                return true;
            }
        }
        return false;
    }


    /**
     * 检测一个点是否在地图的渲染区域
     *
     * @param mapView
     * @param x
     * @param y
     * @return
     */
    public static boolean checkPointInMap(MapView mapView, float x, float y) {
        return !(mapView.selectFeature(x, y) == null);
    }

    /**
     * 设置某图层的一个类型是否显示
     * 常用于设置公共设施是否显示
     *
     * @param mapView
     * @param layerName
     * @param key
     * @param categoryId
     * @param visible
     */
    public static void visibleLayerFeature(MapView mapView, String layerName, String key, long categoryId, boolean visible) {
        mapView.visibleLayerFeature(layerName, key, new Value(categoryId), visible);
    }

    /**
     * 获取二点之间的距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double pointDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((Math.abs(x1 - x2) * Math.abs(x1 - x2) + Math.abs(y1 - y2) * Math.abs(y1 - y2)));
    }

    public static double pointDistance(Coordinate coordinate1, Coordinate coordinate2) {
        return pointDistance(coordinate1.getX(), coordinate1.getY(), coordinate2.getX(), coordinate2.getY());
    }

    public static Coordinate convertCoordinate(Point3D point3D) {
        return new Coordinate(point3D.getX(), point3D.getY());
    }

    public static Coordinate convertCoordinate(Point3D point3D, long floorid) {
        return new Coordinate(point3D.getX(), point3D.getY(), floorid);
    }

    public long getPlanarGraphId(PlanarGraph planarGraph) {
        if (planarGraph == null || PlanarGraph.getPtrAddress(planarGraph) == 0) {
            return 0;
        }
        for (int i = 0; i < planarGraph.getLayerCount(); i++) {
            FeatureCollection features = planarGraph.getFeatures(i);
            if ("Frame".equals(features.getName())) {
                return Feature.planar_graph.get(features.getFirstFeature());
            }
        }
        return 0;
    }

}
