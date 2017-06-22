package com.palmap.exhibition.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.palmap.exhibition.model.LocationInfoModel;
import com.palmap.exhibition.repo.LocationListener;
import com.palmap.exhibition.repo.LocationRepo;
import com.palmap.exhibition.repo.impl.LocationRepoImpl;

public class LampSiteLocationService extends Service implements LocationListener{

    private LocationRepo locationRepo;

    public static final String LOCATION_ACTION = "lampSiteLocationBroadcast";
    public static final String MODEL_LOCATIONINFO = "LocationInfoModel";
    public static final String STATE_TAG = "state_tag";
    public static final String EXCEPTION = "exception";
    public static final String ERROR_MSG = "error_msg";
    public static final String TIMESTAMP = "timeStamp";

    public static final int STATE_COMPLETE = 0;
    public static final int STATE_FAILED = -1;

    @Override
    public void onCreate() {
        super.onCreate();
        if (locationRepo == null) {
            locationRepo = LocationRepoImpl.getInstance();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationRepo.addListener(this);
        locationRepo.stopLocation();
        locationRepo.startLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationRepo.removeListener(this);
        locationRepo.stopLocation();
    }

    public LampSiteLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onComplete(LocationInfoModel locationInfoModel, long timeStamp) {
        Intent intent = new Intent(LOCATION_ACTION);
        intent.putExtra(MODEL_LOCATIONINFO,locationInfoModel);
        intent.putExtra(STATE_TAG,STATE_COMPLETE);
        intent.putExtra(TIMESTAMP,timeStamp);
        sendBroadcast(intent);
    }

    @Override
    public void onFailed(Exception ex, String msg) {
        Intent intent = new Intent(LOCATION_ACTION);
        intent.putExtra(STATE_TAG,STATE_FAILED);
        intent.putExtra(EXCEPTION,ex);
        intent.putExtra(ERROR_MSG,msg);
        sendBroadcast(intent);
    }
}
