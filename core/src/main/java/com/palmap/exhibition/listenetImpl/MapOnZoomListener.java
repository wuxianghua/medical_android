package com.palmap.exhibition.listenetImpl;

import com.palmap.exhibition.widget.CompassView;
import com.palmap.exhibition.widget.Scale;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.gestures.OnZoomListener;

/**
 * Created by 王天明 on 2016/6/12.
 * 默认实现比例尺和指北针的操作的地图缩放监视器
 */
public class MapOnZoomListener implements OnZoomListener {

    private Scale scale;

    private CompassView compassView;

    private boolean textShow = false;

    public MapOnZoomListener(Scale scale, CompassView compassView) {
        this.scale = scale;
        this.compassView = compassView;
    }

    @Override
    public void preZoom(MapView mapView, float v, float v1) {

    }

    @Override
    public void onZoom(MapView mapView, boolean b) {
        if (compassView != null) {
//            compassView.setRotateAngle(-(float) mapView.getRotate() + Config.MAP_ANGLE);
            compassView.setRotateAngle(-(float) mapView.getRotate());
        }
        if (scale != null) {
            scale.postInvalidate();
        }
//        if (textShow != mapView.getZoomLevel() <= 6) {
//            textShow = mapView.getZoomLevel() <= 6;
//            mapView.visibleLayerAllFeature("Area_text", textShow);
//        }
    }

    @Override
    public void postZoom(MapView mapView, float v, float v1) {

    }
}
