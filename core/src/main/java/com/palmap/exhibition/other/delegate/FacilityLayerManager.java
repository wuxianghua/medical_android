package com.palmap.exhibition.other.delegate;

import com.palmaplus.nagrand.core.Value;
import com.palmaplus.nagrand.view.MapView;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Created by 王天明 on 2016/5/5.
 * 设施图层管理
 */
public class FacilityLayerManager {

    private MapView mapView;

    /**
     * 公共设施层的名字为固定值
     */
    private static final String LAYER_NAME = "Facility";
    private static final String KEY = "category";

    /**
     * 电梯ID
     */
    public static final long ID_ELEVATOR = 24091000L;
    /**
     * 男厕所ID
     */
    public static final long ID_TOILETS_MAN = 23024000L;
    /**
     * 女厕所ID
     */
    public static final long ID_TOILETS_WOMAN = 23025000L;
    /**
     * 安全出口ID
     */
    public static final long ID_EXIT = 23061000L;
    /**
     * 楼梯ID
     */
    public static final long ID_STAIRS = 24097000L;
    /**
     * 扶梯ID
     */
    public static final long ID_Escalator = 24093000L;


    public FacilityLayerManager(MapView mapView) {
        checkNotNull(mapView, "mapView == null");
        this.mapView = mapView;
    }

    /**
     * 显示所有的设施
     */
    public void showAll() {
        all(true);
    }

    /**
     * 隐藏所有设施
     */
    public void hideAll() {
        all(false);
    }

    /*****************************************基本设施start****************************************************/

    /**
     * 是否显示电梯
     *
     * @param show
     */
    public void elevator(boolean show) {
        one(ID_ELEVATOR, show);
    }

    /**
     * 是否显示男厕所
     *
     * @param show
     */
    public void toilets_man(boolean show) {
        one(ID_TOILETS_MAN, show);
    }


    /**
     * 是否显示男厕所
     *
     * @param show
     */
    public void toilets_woman(boolean show) {
        one(ID_TOILETS_WOMAN, show);
    }

    /**
     * 是否显示厕所
     *
     * @param show
     */
    public void toilets(boolean show) {
        toilets_man(show);
        toilets_woman(show);
    }

    /**
     * 是否显示安全出口
     *
     * @param show
     */
    public void exit(boolean show) {
        one(ID_EXIT, show);
    }

    /**
     * 是否显示楼梯
     *
     * @param show
     */
    public void stairs(boolean show) {
        one(ID_STAIRS, show);
    }

    /**
     * 是否显示扶梯
     *
     * @param show
     */
    public void escalator(boolean show) {
        one(ID_Escalator, show);
    }
    /****************************************** 基本设施end****************************************************/

    public void all(boolean show) {
        mapView.visibleLayerAllFeature(LAYER_NAME, show);
    }

    public void one(long id, boolean show) {
        mapView.visibleLayerFeature(LAYER_NAME, KEY, new Value(id), show);
    }

}
