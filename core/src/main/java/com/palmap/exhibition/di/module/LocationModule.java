package com.palmap.exhibition.di.module;

import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.di.ActivityScope;
import com.palmap.library.base.BaseActivity;
import com.palmap.library.utils.DeviceUtils;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.position.PositioningManager;
import com.palmaplus.nagrand.position.ble.BeaconPositioningManager;
import com.palmaplus.nagrand.position.wifi.SinglePositioningManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 王天明 on 2016/6/12.
 */
@Module
public class LocationModule {

    @Provides
    @ActivityScope
    public SinglePositioningManager providesWifiLocation(String macAddress) {
        SinglePositioningManager positioningManager = new SinglePositioningManager(macAddress);
        return positioningManager;
    }

    @Provides
    @ActivityScope
    public BeaconPositioningManager providesBeaconLocation(BaseActivity activity) {
        long locationMapId = AndroidApplication.getInstance().getLocationMapId();
        if (locationMapId != -1){
            LogUtil.e("从mapId注入定位器:" + locationMapId);
            return new BeaconPositioningManager(activity, locationMapId, Config.APP_KEY);
        }
        return new BeaconPositioningManager(activity, Config.APP_KEY);
//        return new BeaconPositioningManager(activity, 6, Config.APP_KEY);
    }

    @Provides
    @ActivityScope
    public PositioningManager providesPositioningManager(BaseActivity activity) {
        if (Config.getPositioningManagerType(activity) == Config.PositioningManagerType.BLE) {
            return providesBeaconLocation(activity);
        } else {
            return providesWifiLocation(providesMacAddress(activity));
        }
    }

    @Provides
    public String providesMacAddress(BaseActivity activity) {
        return DeviceUtils.getMac(activity);
    }

}
