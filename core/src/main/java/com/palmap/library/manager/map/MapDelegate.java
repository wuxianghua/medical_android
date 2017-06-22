package com.palmap.library.manager.map;

import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.gestures.OnSingleTapListener;
import com.palmaplus.nagrand.view.gestures.OnZoomListener;

/**
 * Created by 王天明 on 2016/5/5.
 */
public interface MapDelegate extends MapView.OnChangePlanarGraph, OnSingleTapListener,OnZoomListener {

}