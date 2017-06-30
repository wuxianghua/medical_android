package com.palmap.exhibition.navigator;

import android.app.Activity;
import android.content.Context;

import com.palmap.exhibition.launcher.LauncherModel;
import com.palmap.exhibition.model.ExFloorModel;
import com.palmap.exhibition.view.impl.DestinationSearchActivity;
import com.palmap.exhibition.view.impl.PalmapViewActivity;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by 王天明 on 2016/5/9.
 */
@Singleton
public class Navigator {

    @Inject
    public Navigator() {
    }

    public void toPalMapView(Context activity) {
        PalmapViewActivity.navigatorThis(activity);
    }

    public void toPalMapView(Context context, LauncherModel launcherModel) {
        PalmapViewActivity.navigatorThis(context, launcherModel);
    }

    public void toPalMapView(Context that, long buildingId, long floorId, long featureId) {
        PalmapViewActivity.navigatorThis(that, buildingId, floorId, featureId);
    }

    public void toSearchViewForResult(Activity that, long buildingId, ArrayList<ExFloorModel> floorListData, int requestCode) {
        DestinationSearchActivity.navigatorThisForResult(that, buildingId, floorListData, requestCode);
    }

}
