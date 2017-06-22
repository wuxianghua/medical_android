package com.palmap.library.geoFencing;

import com.palmap.exhibition.BuildConfig;
import com.palmap.library.rx.mapView.event.LocationEvent;
import com.palmaplus.nagrand.geos.Coordinate;
import com.palmaplus.nagrand.position.Location;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by 王天明 on 2016/9/9.
 * 围栏管理类
 */
public class GeoFencingManager<O> {

    private Set<GeoFencing<O>> geoFencings;

    private GeoFencingListener<O> geoFencingListener = null;

    private GeoFencingProvides<O> geoFencingProvides;

    public GeoFencingManager() {
        geoFencings = new HashSet<>();
    }

    public void eventLocationData(long floorId, Coordinate event) {
        if (event == null) return;
        if (geoFencings.size() == 0) return;
        if (geoFencingListener == null) return;
        for (GeoFencing<O> geoFencing : geoFencings) {
            if (geoFencing.eventLocation(floorId, event)) {
                geoFencingListener.onFencingEvent(geoFencing);
                return;
            }
        }
        geoFencingListener.onNullFencingEvent(floorId,event);
    }

    public void eventLocationData(LocationEvent locationEvent) {
        Location location = locationEvent.getNewlocation();
        eventLocationData(Location.floorId.get(location.getProperties()),
                location.getPoint().getCoordinate());
    }

    //proxy
    public boolean addFencing(GeoFencing<O> geoFencing) {
        return geoFencings.add(geoFencing);
    }

    public boolean addAllFencing(List<GeoFencing<O>> geoFencingList) {
        return geoFencings.addAll(geoFencingList);
    }

    public boolean removeFencing(GeoFencing geoFencing) {
        boolean b = false;
        Iterator<GeoFencing<O>> iterator = geoFencings.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(geoFencing)) {
                iterator.remove();
                b = true;
                break;
            }
        }
        return b;
    }

    public boolean removeFencing(int index) {
        boolean b = false;
        Iterator<GeoFencing<O>> iterator = geoFencings.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            if (i == index) {
                iterator.remove();
                b = true;
                break;
            }
            i++;
        }
        return b;
    }

    public GeoFencing<O> findFencing(O o) {
        for (GeoFencing<O> geoFencing : geoFencings) {
            if (geoFencing.obtainGeoData().equals(o)) {
                return geoFencing;
            }
        }
        return null;
    }

    public void setGeoFencingListener(GeoFencingListener<O> geoFencingListener) {
        this.geoFencingListener = geoFencingListener;
    }

    public void setGeoFencingProvides(GeoFencingProvides<O> geoFencingProvides) {
        this.geoFencingProvides = geoFencingProvides;
    }

    public void initGeoFencing() {
        if (geoFencingProvides != null) {
            geoFencingProvides.providesGeoFencing()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<GeoFencing<O>>>() {
                        @Override
                        public void call(List<GeoFencing<O>> geoFencings) {
                            addAllFencing(geoFencings);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            if (BuildConfig.DEBUG) {
                                throwable.printStackTrace();
                            }
                        }
                    });
        }
    }
}