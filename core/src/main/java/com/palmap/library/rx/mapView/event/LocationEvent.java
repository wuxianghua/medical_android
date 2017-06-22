package com.palmap.library.rx.mapView.event;

import com.palmaplus.nagrand.position.Location;
import com.palmaplus.nagrand.position.PositioningManager;

/**
 * Created by 王天明 on 2016/5/4.
 */
public class LocationEvent {

    private PositioningManager.LocationStatus status;
    private Location oldlocation;
    private Location newlocation;


    public LocationEvent(PositioningManager.LocationStatus status, Location oldlocation, Location newlocation) {
        this.status = status;
        this.oldlocation = oldlocation;
        this.newlocation = newlocation;
    }

    public PositioningManager.LocationStatus getStatus() {
        return status;
    }

    public void setStatus(PositioningManager.LocationStatus status) {
        this.status = status;
    }

    public Location getOldlocation() {
        return oldlocation;
    }

    public void setOldlocation(Location oldlocation) {
        this.oldlocation = oldlocation;
    }

    public Location getNewlocation() {
        return newlocation;
    }

    public void setNewlocation(Location newlocation) {
        this.newlocation = newlocation;
    }
}
