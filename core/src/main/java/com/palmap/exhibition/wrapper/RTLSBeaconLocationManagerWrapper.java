package com.palmap.exhibition.wrapper;

import android.content.Context;

import com.palmaplus.nagrand.core.Ptr;
import com.palmaplus.nagrand.data.BasicElement;
import com.palmaplus.nagrand.data.MapElement;
import com.palmaplus.nagrand.geos.Coordinate;
import com.palmaplus.nagrand.geos.Point;
import com.palmaplus.nagrand.position.PositioningManager;
import com.palmaplus.nagrand.position.ble.RTLSBeaconLocationManager;
import com.palmaplus.nagrand.position.ble.utils.Location;

/**
 * Created by 王天明 on 2017/2/28.
 */
public class RTLSBeaconLocationManagerWrapper extends PositioningManager<com.palmaplus.nagrand.position.Location> {

    private RTLSBeaconLocationManager manager;
    private com.palmaplus.nagrand.position.Location newLocation;
    private com.palmaplus.nagrand.position.Location oldLocation;

    public RTLSBeaconLocationManagerWrapper(Context context,
                                            long mallId,
                                            String appKey) {
        super(Ptr.NULLPTR);
        manager = new RTLSBeaconLocationManager(context, mallId, appKey);
        newLocation = new com.palmaplus.nagrand.position.Location();
        oldLocation = new com.palmaplus.nagrand.position.Location();
    }

    public RTLSBeaconLocationManagerWrapper(Context context,
                                            String appKey) {
        this(context,0,appKey);
    }

    @Override
    public void start() {
        if (manager != null) {
            manager.start();
        }
    }

    @Override
    public void stop() {
        if (manager != null) {
            manager.stop();
        }
    }

    @Override
    public void close() {
        if (manager != null) {
            manager.close();
        }
    }

    @Override
    public void setOnLocationChangeListener(final OnLocationChangeListener<com.palmaplus.nagrand.position.Location> listener) {
        if (listener == null || manager == null) return;
        manager.setOnLocationChangeListener(new RTLSBeaconLocationManager.OnLocationChangeListener() {
            @Override
            public void onLocationChange(RTLSBeaconLocationManager.LocationStatus locationStatus,
                                         Location oldL,
                                         Location newL) {
                LocationStatus status = LocationStatus.START;
                switch (locationStatus) {
                    case START:
                        status = LocationStatus.START;
                        break;
                    case STOP:status = LocationStatus.STOP;
                        break;
                    case CLOSE:status = LocationStatus.CLOSE;
                        break;
                    case MOVE:status = LocationStatus.MOVE;
                        break;
                    case ENTER:status = LocationStatus.ENTER;
                        break;
                    case OUT:status = LocationStatus.OUT;
                        break;
                    case HEART_BEAT:status = LocationStatus.HEART_BEAT;
                        break;
                    case ERROR:status = LocationStatus.ERROR;
                        break;
                }

                if (status != LocationStatus.MOVE) {
                    listener.onLocationChange(status, null, null);
                    return;
                }
                try {
                    MapElement mapElement = new MapElement();
                    mapElement.addElement("floor_id",new BasicElement(newL.getFloorId()));
                    newLocation.reset(new Point(
                                    new Coordinate(newL.getX(),newL.getY())),
                            mapElement);
                    if (null == oldL) {
                        listener.onLocationChange(status, null, newLocation);
                    }else{
                        MapElement oldMapElement = new MapElement();
                        oldMapElement.addElement("floor_id",new BasicElement(oldL.getFloorId()));
                        oldLocation.reset(new Point(
                                        new Coordinate(oldL.getX(),oldL.getY())),
                                oldMapElement);
                        listener.onLocationChange(status, oldLocation, newLocation);
                    }
                } catch (Exception e) {
                    listener.onLocationChange(LocationStatus.ERROR, null, null);
                }

            }
        });
    }
}
