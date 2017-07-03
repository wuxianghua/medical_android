package com.palmap.exhibition.presenter;

import android.view.View;

import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.exhibition.model.PoiModel;
import com.palmap.exhibition.view.PalMapView;
import com.palmap.exhibition.view.impl.PalmapViewState;
import com.palmap.library.presenter.Presenter;
import com.palmaplus.nagrand.data.Feature;
import com.palmaplus.nagrand.geos.Coordinate;
import com.palmaplus.nagrand.position.ble.BeaconPositioningManager;
import com.palmaplus.nagrand.view.MapOptions;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.gestures.OnDoubleTapListener;
import com.palmaplus.nagrand.view.gestures.OnLongPressListener;
import com.palmaplus.nagrand.view.gestures.OnSingleTapListener;

import java.util.List;

/**
 * Created by 王天明 on 2016/6/3.
 */
public interface PalMapViewPresenter extends Presenter,
        OnSingleTapListener,
        OnDoubleTapListener,
        MapView.OnChangePlanarGraph,
        OnLongPressListener,
        MapView.OnLoadStatusListener {

    void attachView(PalMapView view);

    void attachView(PalMapView view,long building, long floorId, long featureId);

    MapOptions configMap();

    PalmapViewState getState();

    long getCurrentFloorId();

    long getBuildingId();

    String getCurrentFloorName();

    /**
     * 获取场馆名
     *
     * @return
     */
    String arenaName();

    /**
     * 改变楼层
     *
     * @param floorId
     */
    void changeFloor(long floorId);

    void changeFloorAddMark(long floorId, long featureId, boolean isEndSet);

    void changeFloor(long buildingId, long floorId);

    void loadMapFromBuildingId(long building);

    void setCanAutoChangeFloor(boolean b);

    boolean backOutSide();

    /**
     * 进入建筑
     */
    void enterBuilding(PoiModel poiModel);

    /**
     * 是不是在户外
     *
     * @return
     */
    boolean inOutSide();

    void startMark(PoiModel poiModel);

    boolean startMarkFromLocation();

    void endMark(PoiModel poiModel);

    void startLocation(BeaconPositioningManager positioningManager);

    Coordinate getUserCoordinate();

    /**
     * 重新获取导航线
     */
    void reLoadNavigateRoad();

    void resetState();

    void refreshPoiMark(long locationId);

    void addFacilityMarks(List<Feature> facilityModelCoordinates);

    void clearFacilityMarks();

    /**
     * 开始导航
     */
    void beginNavigate();

    void startSpeaking(View v, String msg);

    /**
     * 获取点亮活动
     */
    void loadLightEventActivity();

    /**
     * 启动一个点亮活动
     * @param atyId
     */
    void beginLightEventAty(Api_ActivityInfo api_activityInfo, int atyId);

    void toggleLightEventMarkShow(boolean canShow);

    void closeLightAty();

    /**
     * 分享活动成功
     * @param id 活动Id
     */
    void shareCompleteAty(int id);

    /**
     * 加载推送活动
     */
    void loadPushEventActivity();

    void refreshData();

    void setPalmapViewState(PalmapViewState state);

    void animRefreshOverlay(int time);

    void moveToUserLocation();

    void exitNavigate();

    void removeTapAndPoiMark();

    void resetFeature();

    /**
     * 开始模拟导航
     */
    void beginMockNavigate();

}
