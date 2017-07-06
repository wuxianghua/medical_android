package com.palmap.library.geoFencing;

import com.palmap.exhibition.BuildConfig;
import com.palmap.library.rx.mapView.event.LocationEvent;
import com.palmaplus.nagrand.geos.Coordinate;
import com.palmaplus.nagrand.position.Location;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


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
        if (geoFencingListener == null) return;
        GeoFencing<O> geoFencing = eventLocationData(event, floorId);
        if (null == geoFencing) {
            geoFencingListener.onNullFencingEvent(floorId,event);
        }else{
            geoFencingListener.onFencingEvent(geoFencing);
        }
    }

    public GeoFencing<O> eventLocationData(Coordinate event,long floorId) {
        if (event == null) return null;
        if (geoFencings.size() == 0) return null;
        for (GeoFencing<O> geoFencing : geoFencings) {
            if (geoFencing.eventLocation(floorId, event)) {
                return geoFencing;
            }
        }
        return null;
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
                    .subscribe(new Consumer<List<GeoFencing<O>>>() {
                        @Override
                        public void accept(List<GeoFencing<O>> geoFencings) {
                            addAllFencing(geoFencings);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            if (BuildConfig.DEBUG) {
                                throwable.printStackTrace();
                            }
                        }
                    });
        }
    }
}