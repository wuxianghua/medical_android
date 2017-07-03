package com.palmap.exhibition.di.module;

import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.di.ActivityScope;
import com.palmap.exhibition.wrapper.RTLSBeaconLocationManagerWrapper;
import com.palmap.library.base.BaseActivity;
import com.palmap.library.utils.DeviceUtils;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.position.PositioningManager;
import com.palmaplus.nagrand.position.wifi.SinglePositioningManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 王天明 on 2016/6/12.
 */
@Module
public class LocationModule {


    private SinglePositioningManager createWifiLocation(String macAddress) {
        SinglePositioningManager positioningManager = new SinglePositioningManager(macAddress);
        return positioningManager;
    }

    private PositioningManager createBeaconLocation(BaseActivity activity) {
        long locationMapId = AndroidApplication.getInstance().getLocationMapId();
        if (locationMapId != -1){
            LogUtil.e("从mapId注入定位器:" + locationMapId);
            return new RTLSBeaconLocationManagerWrapper(activity, locationMapId, Config.APP_KEY);
        }
        return new RTLSBeaconLocationManagerWrapper(
                activity,
                Config.APP_KEY);
    }

    @Provides
    @ActivityScope
    public PositioningManager providesPositioningManager(BaseActivity activity) {
        if (Config.getPositioningManagerType(activity) == Config.PositioningManagerType.BLE) {
            return createBeaconLocation(activity);
        } else {
            return createWifiLocation(providesMacAddress(activity));
        }
    }

    @Provides
    public String providesMacAddress(BaseActivity activity) {
        return DeviceUtils.getMac(activity);
    }

}
