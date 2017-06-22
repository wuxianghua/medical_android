package com.palmap.exhibition.di.module;

import com.palmap.exhibition.other.ExtendLayerHelper;
import com.palmap.exhibition.sensor.LocationSensorDelegate;
import com.palmap.library.base.BaseActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 王天明 on 2016/9/21.
 */
@Module
public class DelegateModule {

    @Provides
    LocationSensorDelegate providesLocationSensorDelegate(BaseActivity activity) {
        return new LocationSensorDelegate(activity);
    }

    @Provides
    ExtendLayerHelper providesExtendLayerHelper(BaseActivity activity) {
        return new ExtendLayerHelper(activity);
    }
}
