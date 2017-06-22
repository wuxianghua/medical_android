package com.palmap.library.manager.map;

import com.palmaplus.nagrand.data.PlanarGraph;
import com.palmaplus.nagrand.view.MapView;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by 王天明 on 2016/5/5.
 */
public class MapPluginManager {

    private ArrayList<MapPlugin> plugins;

    public MapPluginManager() {
        plugins = new ArrayList<>();
    }

    public boolean attachPlugin(MapPlugin plugin) {
        return plugins.add(plugin);
    }

    public boolean detachePlugin(MapPlugin plugin) {
        return plugins.remove(plugin);
    }


    public void onChangePlanarGraph(final PlanarGraph planarGraph, final PlanarGraph planarGraph1, final long l, final long l1) {
        Observable.from(plugins).subscribe(new Action1<MapPlugin>() {
            @Override
            public void call(MapPlugin plugin) {
                plugin.getDelegate().onChangePlanarGraph(planarGraph, planarGraph1, l, l1);
            }
        });
    }

    public void onSingleTap(final MapView mapView, final float v, final float v1) {
        Observable.from(plugins).subscribe(new Action1<MapPlugin>() {
            @Override
            public void call(MapPlugin plugin) {
                plugin.getDelegate().onSingleTap(mapView, v, v1);
            }
        });
    }

    public void preZoom(final MapView mapView,final float v,final float v1) {
        Observable.from(plugins).subscribe(new Action1<MapPlugin>() {
            @Override
            public void call(MapPlugin plugin) {
                plugin.getDelegate().preZoom(mapView, v, v1);
            }
        });
    }

    public void onZoom(final MapView mapView,final boolean b) {
        Observable.from(plugins).subscribe(new Action1<MapPlugin>() {
            @Override
            public void call(MapPlugin plugin) {
                plugin.getDelegate().onZoom(mapView, b);
            }
        });
    }

    public void postZoom(final MapView mapView,final float v,final float v1) {
        Observable.from(plugins).subscribe(new Action1<MapPlugin>() {
            @Override
            public void call(MapPlugin plugin) {
                plugin.getDelegate().postZoom(mapView, v, v1);
            }
        });
    }


}
