package com.palmap.exhibition.presenter.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorEvent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.BuildConfig;
import com.palmap.exhibition.R;
import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.config.MapParam;
import com.palmap.exhibition.dao.business.ActivityInfoBusiness;
import com.palmap.exhibition.dao.business.CoordinateBusiness;
import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.exhibition.model.Api_PositionInfo;
import com.palmap.exhibition.model.ExBuildingModel;
import com.palmap.exhibition.model.FacilityModel;
import com.palmap.exhibition.model.LocationInfoModel;
import com.palmap.exhibition.model.NavigationPointModel;
import com.palmap.exhibition.model.PoiModel;
import com.palmap.exhibition.other.FloorSwitchManager;
import com.palmap.exhibition.other.MapAngleHelper;
import com.palmap.exhibition.other.OverLayerManager;
import com.palmap.exhibition.other.PMPDynamicNavigationManager;
import com.palmap.exhibition.presenter.PalMapViewPresenter;
import com.palmap.exhibition.repo.ActivityInfoRepo;
import com.palmap.exhibition.repo.LocationListener;
import com.palmap.exhibition.sensor.LocationSensorDelegate;
import com.palmap.exhibition.service.LampSiteLocationService;
import com.palmap.exhibition.view.PalMapView;
import com.palmap.exhibition.view.impl.PalmapViewState;
import com.palmap.exhibition.widget.overlayer.EndMark;
import com.palmap.exhibition.widget.overlayer.PubFacilityMark;
import com.palmap.exhibition.widget.overlayer.StartMark;
import com.palmap.library.geoFencing.GeoFencing;
import com.palmap.library.geoFencing.GeoFencingListener;
import com.palmap.library.geoFencing.GeoFencingManager;
import com.palmap.library.model.LocationType;
import com.palmap.library.rx.dataSource.RxDataSource;
import com.palmap.library.utils.DisposableUtils;
import com.palmap.library.utils.FloorUtils;
import com.palmap.library.utils.IOUtils;
import com.palmap.library.utils.LogUtil;
import com.palmap.library.utils.MapUtils;
import com.palmaplus.nagrand.core.Types;
import com.palmaplus.nagrand.data.DataList;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.Feature;
import com.palmaplus.nagrand.data.FeatureCollection;
import com.palmaplus.nagrand.data.LocationList;
import com.palmaplus.nagrand.data.LocationModel;
import com.palmaplus.nagrand.data.MapModel;
import com.palmaplus.nagrand.data.PlanarGraph;
import com.palmaplus.nagrand.geos.Coordinate;
import com.palmaplus.nagrand.navigate.ConnectedInfo;
import com.palmaplus.nagrand.navigate.CoordinateInfo;
import com.palmaplus.nagrand.navigate.DynamicNavigateParams;
import com.palmaplus.nagrand.navigate.DynamicNavigateWrapper;
import com.palmaplus.nagrand.navigate.DynamicNavigationMode;
import com.palmaplus.nagrand.navigate.LineMode;
import com.palmaplus.nagrand.navigate.NavigateManager;
import com.palmaplus.nagrand.navigate.OnDynamicNavigateListener;
import com.palmaplus.nagrand.position.Location;
import com.palmaplus.nagrand.position.PositioningManager;
import com.palmaplus.nagrand.position.ble.BeaconPositioningManager;
import com.palmaplus.nagrand.position.ble.utils.BleLocation;
import com.palmaplus.nagrand.rtls.pdr.LocationPair;
import com.palmaplus.nagrand.rtls.pdr.PDR;
import com.palmaplus.nagrand.view.MapOptions;
import com.palmaplus.nagrand.view.MapView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * Created by 王天明 on 2016/6/3.
 */
public class PalMapViewPresenterImpl implements PalMapViewPresenter, OverLayerManager.Listener {

    /**
     * 无效的ID
     */
    private static final long ID_NONE = -1;

    private NavigateManager navigateManager;

    private PalMapView palMapView;

    private Feature mFeature = null;

    private PlanarGraph currentPlanarGraph;

    /**
     * 当前定位点位置
     */
    private Coordinate userCoordinate;

    private PalmapViewState state = PalmapViewState.Normal;

    /**
     * 是否可以根据定位自动切换到当前楼层
     */
    private boolean canAutoChangeFloor = true;

    private boolean isRun = false;

    private MyLocationListener myLocationListener = new MyLocationListener();

    private BeaconPositioningManager positioningManager;

    /**
     * 是否有户外图
     */
    private boolean isHaveOutSide = false;

    /**
     * 当前楼层ID
     */
    private long currentFloorId = ID_NONE;
    private long planarGraphId = ID_NONE;

    /**
     * 将要跳转的下一个楼层id
     */
    private long nextFloorId;

    /**
     * 导航时偏差导航线超出距离次数
     */
    private int navigationOutOfBounds = 0;
    /**
     * 导航时偏差导航线超出距离次数大于这个值 需要重新规划导航线
     */
    private static final int maxNavigationOutOfBounds = 6;

    /**
     * 判断当前是否可以切换楼层
     */
    private FloorSwitchManager floorSwitchManager;

    private ExBuildingModel exBuildingModel;

    private long featureId = ID_NONE;
    private long initBuilding = ID_NONE;

    private OverLayerManager overLayerManager;

    /**
     * 判断是否请求导航线(清楚之后第一次)
     */
    private boolean requestNaviRoad = false;

    @Inject
    List<FacilityModel> facilityModels;

    @Inject
    LocationSensorDelegate locationSensorDelegate;
    @Inject
    DataSource dataSource;
    @Inject
    ActivityInfoRepo activityInfoRepo;
    @Inject
    ActivityInfoBusiness activityInfoBusiness;
    @Inject
    CoordinateBusiness coordinateBusiness;

    private LocationBroadcastReceiver locationBroadcastReceiver;

    private GeoFencingManager<Api_PositionInfo.ObjBean> lightEventFencingManager;

    private HashMap<Long, PlanarGraph> mapDataCache;

    private Disposable loadMapSubscribe;

    private Disposable loadMapWithBuildingIdSubscription;

    private static boolean openMapCache = false;

    private int naviTotalLength;
    /**
     * 导航线外包盒
     */
    private Coordinate[] envelopeCoordinateArr;

    private PMPDynamicNavigationManager pmpDynamicNavigationManager = new PMPDynamicNavigationManager();

    /**
     * 定位惯导算法
     */
    private PDR pdr;

    /**
     * 是不是模拟导航
     */
    private AtomicBoolean isMockNavi = new AtomicBoolean(false);

    @Inject
    public PalMapViewPresenterImpl() {
        navigateManager = new NavigateManager();
        navigateManager.setOnNavigateComplete(new NavigateListener());
        floorSwitchManager = new FloorSwitchManager(3);

        lightEventFencingManager = new GeoFencingManager<>();

        if (openMapCache) {
            mapDataCache = new HashMap<>();
        }
    }

    @Override
    public void attachView(PalMapView view) {
        palMapView = view;
        palMapView.readPubFacility(facilityModels);
        getOverLayerManager().registerListener(this);
        // TODO: 2017/2/23 清空点亮的点位信息
        coordinateBusiness.clear();
        loadMap(this.initBuilding, this.currentFloorId, this.featureId);
        registerGeoFencingListener();
        registerLocationSensorListener();
        pdr = new PDR(palMapView.getContext());
    }

    @Override
    public void attachView(PalMapView view, long building, long floorId, long featureId) {
        currentFloorId = floorId;
        this.initBuilding = building;
        this.featureId = featureId;
        attachView(view);
    }

    private Observable<PlanarGraph> loadPlanarGraph(final long floorId) {
        if (openMapCache) {
            if (mapDataCache.get(floorId) != null) {
                return Observable.create(new ObservableOnSubscribe<PlanarGraph>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<PlanarGraph> e) throws Exception {
                        e.onNext(mapDataCache.get(floorId));
                        e.onComplete();
                    }
                });
            }
        }
        LogUtil.e("load map with network");
        return RxDataSource.requestPlanarGraph(dataSource, floorId);
    }

    private Observable<Long> checkMapId(final long mapId) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(final ObservableEmitter<Long> e) throws Exception {
                if (mapId == ID_NONE) {
                    RxDataSource.requestMaps(dataSource).subscribe(new Consumer<DataList<MapModel>>() {
                        @Override
                        public void accept(DataList<MapModel> mapModelDataList) {
                            MapModel poi = mapModelDataList.getPOI(0);
                            e.onNext(MapParam.getId(poi));
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            e.onError(throwable);
                        }
                    });
                } else {
                    e.onNext(mapId);
                }
            }
        });
    }

    private void loadMap(long mapId) {
        loadMap(mapId, ID_NONE);
    }

    private void loadMap(long mapId, long floorId) {
        loadMap(mapId, floorId, ID_NONE);
    }

    private void loadMap(final long mapId, final long floorId, final long featureId) {
        // TODO: 2016/12/1  修改不传入mapId的错误
        loadMapSubscribe = checkMapId(mapId).flatMap(new Function<Long, Observable<MapModel>>() {
            @Override
            public Observable<MapModel> apply(@NonNull Long aLong) throws Exception {
                LogUtil.e("加载地图 mapID:" + aLong);
                return RxDataSource.requestMap(dataSource, aLong);
            }
        }).subscribe(new Consumer<MapModel>() {
            @Override
            public void accept(@NonNull MapModel mapModel) throws Exception {
                long mapid = MapParam.getId(mapModel);
                AndroidApplication.getInstance().setLocationMapId(mapid);
                loadMapWithBuildingId(MapModel.POI.get(mapModel), floorId, featureId);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
                palMapView.hideLoading();
                nextFloorId = ID_NONE;
                palMapView.showRetry(throwable, new Runnable() {
                    @Override
                    public void run() {
                        loadMap(mapId, floorId, featureId);
                    }
                });
            }
        });

    }

    private void loadMapWithBuildingId(final long buildingId, final long floorId, final long featureId) {
//        Config.MAP_ANGLE = MapAngleHelper.getAngleWithMapId(buildingId);
        Config.MAP_ANGLE = (float) MapAngleHelper.getAngleWithMapId((int) AndroidApplication.getInstance().getLocationMapId());
        nextFloorId = floorId;

        loadMapWithBuildingIdSubscription = RxDataSource.requestPOI(dataSource, buildingId).flatMap(new Function<LocationModel, Observable<LocationList>>() {
            @Override
            public Observable<LocationList> apply(LocationModel locationModel) {
                exBuildingModel = new ExBuildingModel(locationModel);
                palMapView.readTitle(exBuildingModel.getName());
                LocationType locationType = new LocationType(locationModel);
                switch (locationType.type) {
                    //楼层或者图像可以直接加载地图数据了
                    case PLANAR_GRAPH:
                        isHaveOutSide = true;
                        planarGraphId = buildingId;
                        return Observable.create(new ObservableOnSubscribe<LocationList>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<LocationList> e) throws Exception {
                                e.onNext(null);
                            }
                        });
                    default:
                        return RxDataSource.requestPOIChildren(dataSource, buildingId);
                }
            }
        }).flatMap(new Function<LocationList, Observable<PlanarGraph>>() {
            @Override
            public Observable<PlanarGraph> apply(LocationList locationList) {
                if (locationList == null) {
                    palMapView.readFloorData(null, 0);
                    return loadPlanarGraph(buildingId);
                }
                nextFloorId = floorId;
                if (nextFloorId == ID_NONE) {
                    LocationModel locationModel = MapUtils.obtainDefaultLocationModel(locationList);
                    nextFloorId = LocationModel.id.get(locationModel);
                }
                palMapView.readFloorData(locationList, nextFloorId);
                return loadPlanarGraph(nextFloorId);
            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                palMapView.showLoading();
            }
        }).subscribe(new Consumer<PlanarGraph>() {
            @Override
            public void accept(@NonNull PlanarGraph planarGraph) throws Exception {
                palMapView.hideRetry();
                PalMapViewPresenterImpl.this.featureId = featureId;
                getOverLayerManager().hideStartAndEnd();
                if (openMapCache) {
                    planarGraph.obtain();
                    mapDataCache.put(nextFloorId, planarGraph);
                }
                palMapView.readMapData(planarGraph);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
                palMapView.hideLoading();
                nextFloorId = ID_NONE;
                palMapView.showRetry(throwable, new Runnable() {
                    @Override
                    public void run() {
                        loadMapWithBuildingId(buildingId, floorId, featureId);
                    }
                });
            }
        });

    }

    @Override
    public void onStartEndAddedListener(MapView mapView, StartMark startMark, EndMark endMark) {
        getOverLayerManager().removeTapMark();
        reLoadNavigateRoad();
    }

    @Override
    public void onStartAddedListener(MapView mapView, StartMark startMark) {
        getOverLayerManager().removeTapMark();
//        userCoordinate = new Coordinate(startMark.getWorldX(), startMark.getWorldY());
//        userCoordinate.setZ(currentFloorId);
    }

    @Override
    public void onEndAddedListener(MapView mapView, EndMark endMark) {
        getOverLayerManager().removeTapMark();
    }

    private void registerLocationSensorListener() {
        if (locationSensorDelegate != null) {
            locationSensorDelegate.setListener(new LocationSensorDelegate.AcceleroListener() {
                private long timestamp;
                private double oldMapRotate = 0;

                @Override
                public void onSensorChanged(SensorEvent event, double angle) {
                    if (timestamp == 0) {
                        timestamp = System.currentTimeMillis();
                        return;
                    }
                    if (System.currentTimeMillis() - timestamp < 20) {
                        return;
                    }
                    double currentMapRotate = palMapView.getMapView().getRotate();
                    if (currentMapRotate == 0) {
                        currentMapRotate = oldMapRotate;
                    } else {
                        oldMapRotate = currentMapRotate;
                    }
                    float rotate = (float) (angle - currentMapRotate);
                    getOverLayerManager().rotateLocation(rotate);

                    timestamp = System.currentTimeMillis();
                }
            });
        }
    }

    /**
     * 注册地理围栏回调
     */
    private void registerGeoFencingListener() {
        lightEventFencingManager.setGeoFencingListener(new GeoFencingListener<Api_PositionInfo.ObjBean>() {
            @Override
            public void onFencingEvent(GeoFencing<Api_PositionInfo.ObjBean> geoFencing) {
                LogUtil.e("定位在活动:" + geoFencing.obtainGeoData().getActivityId() + "区域中");
                // TODO: 2016/10/20 选中到过的点
                getOverLayerManager().selectLightEventMark(geoFencing.obtainGeoData().getId());
            }

            @Override
            public void onNullFencingEvent(long floorId, Coordinate event) {
                LogUtil.e("不在任何的点亮活动点区域内");
            }
        });
    }

    @Override
    public MapOptions configMap() {
        MapOptions options = new MapOptions();
//        options.setSkewEnabled(false);
//        options.setRotateEnabled(false);
        return options;
    }

    @Override
    public PalmapViewState getState() {
        return state;
    }

    public long getCurrentFloorId() {
        return currentFloorId;
    }

    @Override
    public long getBuildingId() {
        if (exBuildingModel == null) return 0;
        return exBuildingModel.getBuildingId();
    }

    @Override
    public String getCurrentFloorName() {
        return palMapView.getCurrentFloorName();
    }

    @Override
    public String arenaName() {
        return null;
    }

    @Override
    public void changeFloor(final long floorId) {
        LogUtil.e("floorId:" + floorId);
        nextFloorId = floorId;
        // TODO: 2016/6/23 先判断切换的楼层是不是当前的建筑
        if (floorId == getBuildingId()) {
            backOutSide();
        } else {
            RxDataSource.requestPOI(dataSource, floorId).subscribe(new Consumer<LocationModel>() {
                @Override
                public void accept(LocationModel locationModel) {
                    long buildingId = LocationModel.parent.get(locationModel);
                    if (buildingId == 1271335) {
                        buildingId = 1318733;
                    }
                    changeFloor(buildingId, floorId);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) {
                    palMapView.showRetry(throwable, new Runnable() {
                        @Override
                        public void run() {
                            changeFloor(floorId);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void changeFloorAddMark(long floorId, long featureId) {
        this.featureId = featureId;
        if (currentFloorId == floorId) {
            // TODO: 2016/9/27 优化切换当前楼层
            getOverLayerManager().removeTapMark();
            onChangePlanarGraph(currentPlanarGraph, currentPlanarGraph, currentFloorId, currentFloorId);
        } else {
            palMapView.showMessage("已为您跳转至最近楼层");
            changeFloor(floorId);
        }
    }

    @Override
    public void changeFloor(long buildingId, final long floorId) {
        LogUtil.e("floorId:" + floorId);
        getOverLayerManager().removeTapMark();
        getOverLayerManager().hideLocation();
        // TODO: 2016/6/23 先判断切换的楼层是不是当前的建筑
        if (floorId == getBuildingId()) {
            backOutSide();
        } else {
            loadMapWithBuildingId(buildingId, floorId, featureId);
        }
    }

    /**
     * 从mapId直接加载地图(默认楼层)
     *
     * @param buildingId
     */
    @Override
    public void loadMapFromBuildingId(final long buildingId) {
        if (exBuildingModel != null && buildingId == exBuildingModel.getBuildingId()) {
            return;
        }
        featureId = ID_NONE;
        mFeature = null;
        LogUtil.e("buildingId:" + buildingId);
        getOverLayerManager().clearAll();
        setPalmapViewState(PalmapViewState.Normal);
        navigateManager.clear();
        setCanAutoChangeFloor(false);
        loadMapWithBuildingId(buildingId, ID_NONE, ID_NONE);
    }

    @Override
    public void setCanAutoChangeFloor(boolean b) {
        canAutoChangeFloor = b;
        featureId = ID_NONE;
        LogUtil.e("setCanAutoChangeFloor:" + b);
    }

    @Override
    public boolean backOutSide() {
        if (!isHaveOutSide) return false;
        if (!inOutSide() && planarGraphId != ID_NONE) {
            palMapView.showLoading();
            //  palMapView.readFloorData(null, 0);
            floorSwitchManager.clearData();
//            loadPoi(getCurrentFloorId());
            loadMap(planarGraphId);
            return true;
        }
        return false;
    }

    /**
     * //进入建筑
     *
     * @param poiModel
     */
    @Override
    public void enterBuilding(PoiModel poiModel) {
        palMapView.readTitle(poiModel.getDisPlay());
        loadMap(poiModel.getId());
    }

    @Override
    public void setPalmapViewState(PalmapViewState state) {
        this.state = state;
    }

    @Override
    public void animRefreshOverlay(int time) {
        getOverLayerManager().animRefreshOverlay(time);
    }

    @Override
    public void moveToUserLocation() {
        if (this.getUserCoordinate() != null
                && this.getCurrentFloorId() == this.getUserCoordinate().getZ()) {
            palMapView.getMapView().moveToPoint(this.getUserCoordinate(), true, 300);
            getOverLayerManager().animRefreshOverlay(300);
        }
    }

    @Override
    public boolean inOutSide() {
        return isHaveOutSide && currentFloorId == getBuildingId();
    }

    @Override
    public void startMark(PoiModel poiModel) {
        palMapView.showRouteInfoStart(poiModel.getDisPlay(), getCurrentFloorName() + " " + poiModel.getAddress());
        getOverLayerManager().addStartMark(createNavigationPointModelWithPoiModel(poiModel), poiModel.getX(), poiModel.getY());
    }

    @Override
    public boolean startMarkFromLocation() {
        if (userCoordinate == null) {
            return false;
        }
        long userFloorId = (long) userCoordinate.getZ();
        Feature userFeature = palMapView.getMapView().selectFeature((float) userCoordinate.getX(), (float) userCoordinate.getY());
        long userFeatureId = -1;
        //String name = palMapView.getContext().getString(R.string.ngr_theWay);
        String address = "";
        if (userFeature != null) {
            userFeatureId = MapParam.getId(userFeature);
            /*if (userFeatureId != getCurrentFloorId()) {
                name = MapParam.getName(userFeature);
            }*/
            address = MapParam.getAddress(userFeature);
        }
        NavigationPointModel navigationPointModel = new NavigationPointModel();
        navigationPointModel.setPoiId(userFeatureId);
        navigationPointModel.setFloorId(userFloorId);
        getOverLayerManager().addStartMark(navigationPointModel, userCoordinate.getX(), userCoordinate.getY(),
                getCurrentFloorId() - userFloorId == 0);
        palMapView.showRouteInfoStart("我的位置", palMapView.getFloorNameById(userFloorId) + " " + address);

        setPalmapViewState(PalmapViewState.RoutePlanning);
        palMapView.hidePoiMenuTop();
        getOverLayerManager().removeTapMark();
        return true;
    }

    @Override
    public void endMark(PoiModel poiModel) {
        palMapView.showRouteInfoEnd(poiModel.getDisPlay(), getCurrentFloorName() + " " + poiModel.getAddress());
        getOverLayerManager().removePoiMark();
        getOverLayerManager().addEndMark(createNavigationPointModelWithPoiModel(poiModel), poiModel.getX(), poiModel.getY());
    }

    private NavigationPointModel createNavigationPointModelWithPoiModel(PoiModel poiModel) {
        NavigationPointModel navigationPointModel = new NavigationPointModel();
        navigationPointModel.setPoiId(poiModel.getId());
        navigationPointModel.setFloorId(getCurrentFloorId());
        navigationPointModel.setFloorName(getCurrentFloorName());
        navigationPointModel.setAddress(poiModel.getAddress());
        return navigationPointModel;
    }

    private OverLayerManager getOverLayerManager() {
        if (overLayerManager == null) {
            overLayerManager = new OverLayerManager(palMapView.getMapView());
        }
        return overLayerManager;
    }

    @Override
    public Coordinate getUserCoordinate() {
        return userCoordinate;
    }

    @Override
    public void reLoadNavigateRoad() {
        LogUtil.d("获取导航线。。。");
        canAutoChangeFloor = false;
        if (!getOverLayerManager().isHaveStartEnd()) {
            palMapView.showErrorMessage(palMapView.getContext().getString(R.string.ngr_msg_err_notStartOrEnd));
            palMapView.hideRetry();
            return;
        }
        LogUtil.e("floorid:" + getCurrentFloorId());

        LogUtil.e("startPoiId:" + getOverLayerManager().getStartMark().getPoiId());
        LogUtil.e("endPoiId:" + getOverLayerManager().getEndMark().getPoiId());

        // TODO: 2016/6/27 起始点距离小于最小距离 不能进行路线规划
        //2个点个id相等 但是不等于0
        if (getOverLayerManager().getStartMark().getPoiId() != 0
                && getOverLayerManager().getStartMark().getPoiId() != getCurrentFloorId()
                && getOverLayerManager().getStartMark().getPoiId() == getOverLayerManager().getEndMark().getPoiId()
                || (getOverLayerManager().getStartMark().getFloorId() == getOverLayerManager().getEndMark().getFloorId()
                && getOverLayerManager().measureStateEndDistance() < Config.PATHPLANNING_MIN_DISTANCE)) {

            palMapView.showErrorMessage(palMapView.getContext().getString(R.string.ngr_msg_distanceShort));
            LogUtil.e("起始点距离:" + getOverLayerManager().measureStateEndDistance());
            getOverLayerManager().clearAllNotLocation();
            palMapView.hideRouteInfoView();
            if (mFeature != null) {
                palMapView.resetFeatureStyle(mFeature);
            }
            resetState();
            return;
        }
        // TODO: 2016/6/27 设置为路劲规划状态
        if (state == PalmapViewState.Normal || state == PalmapViewState.ENd_SET) {
            setPalmapViewState(PalmapViewState.RoutePlanning);
        }
        palMapView.showPoiMenu(null, state);
        palMapView.showLoading();
        //起点终点都设置了

        navigateManager.navigation(
                getOverLayerManager().getStartMark().getWorldX(),
                getOverLayerManager().getStartMark().getWorldY(),
                getOverLayerManager().getStartMark().getFloorId(),
                getOverLayerManager().getEndMark().getWorldX(),
                getOverLayerManager().getEndMark().getWorldY(),
                getOverLayerManager().getEndMark().getFloorId(),
                getCurrentFloorId()
        );
        requestNaviRoad = true;
        endNavigationDialogCanShow = true;
    }

    /**
     * 重置状态
     * 1.状态置为正常
     * 2.清除起始点mark，tapMark
     * 3.隐藏poiMenu
     * 4.恢复变色Feature
     * 5.清空导航线
     */
    @Override
    public void resetState() {
        setPalmapViewState(PalmapViewState.Normal);
        getOverLayerManager().clearAllNotLocation();
        palMapView.clearNavigateRoad();
        palMapView.hidePoiMenu();
        resetFeature();
        navigateManager.clear();
        palMapView.hideRouteInfoView();
        endNavigationDialogCanShow = true;
        if (navigateManager != null) {
            navigateManager.stop();
        }
    }

    @Override
    public void refreshPoiMark(long locationId) {
        getOverLayerManager().addPoiMark(currentFloorId, locationId);
        mFeature = palMapView.getMapView().selectFeature(locationId);
        if (mFeature != null) {
            palMapView.readFeatureColor(mFeature, 0xffFFB5B5);
        }
    }

    @Override
    public void addFacilityMarks(final List<Feature> facilityModelCoordinates) {
        // TODO: 2017/2/14  得到所有mark构建的区域 移动地图到该区域
        if (facilityModelCoordinates.size() == 1) {
            palMapView.getMapView().moveToPoint(
                    facilityModelCoordinates.get(0).getCentroid()
            );
            return;
        }

        double minX = 0, minY = 0;
        double maxX = 0, maxY = 0;
        for (Feature f : facilityModelCoordinates) {

            if (minX == 0 || minX > f.getCentroid().getX()) {
                minX = f.getCentroid().getX();
            }

            if (minY == 0 || minY > f.getCentroid().getY()) {
                minY = f.getCentroid().getY();
            }

            if (f.getCentroid().getX() > maxX) {
                maxX = f.getCentroid().getX();
            }

            if (f.getCentroid().getY() > maxY) {
                maxY = f.getCentroid().getY();
            }

        }
        palMapView.getMapView().moveToRect(
                minX,
                minY,
                maxX,
                maxY,
                true,
                300
        );
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                palMapView.getMapView().doCollisionDetection();
                palMapView.refreshScaleView();
                getOverLayerManager().addFacilityMarks(facilityModelCoordinates, currentFloorId, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v instanceof PubFacilityMark) {
                            PubFacilityMark pubFacilityMark = (PubFacilityMark) v;
                            Types.Point tempPoint = palMapView.getMapView().converToScreenCoordinate(
                                    pubFacilityMark.getGeoCoordinate()[0],
                                    pubFacilityMark.getGeoCoordinate()[1]);
                            getOverLayerManager().clearFacilityMarks();
                            palMapView.clearFacilityListSelect();
                            onSingleTap(palMapView.getMapView(), (float) tempPoint.x, (float) tempPoint.y);
                        }
                    }
                });
            }
        }, 300);
    }

    @Override
    public void clearFacilityMarks() {
        getOverLayerManager().clearFacilityMarks();
    }

    //退出导航
    @Override
    public void exitNavigate() {
        setPalmapViewState(PalmapViewState.RoutePlanning);
        palMapView.showRouteInfoDetails(naviTotalLength + "m");
        palMapView.readExitNavigate();
        navigateManager.stop();
        if (envelopeCoordinateArr != null) {
            palMapView.getMapView().moveToRect(
                    envelopeCoordinateArr[0].getX(),
                    envelopeCoordinateArr[0].getY(),
                    envelopeCoordinateArr[1].getX(),
                    envelopeCoordinateArr[1].getY(),
                    true,
                    200
            );
            getOverLayerManager().refreshOverlayDelayed(200);
        }
    }

    @Override
    public void beginNavigate() {
        if (navigateManager == null || null == userCoordinate || PalmapViewState.Navigating == getState()) {
            return;
        }
        palMapView.getMapView().zoom(2.0f);
        //getOverLayerManager().refreshOverlayDelayed(10);
        setPalmapViewState(PalmapViewState.Navigating);
        setCanAutoChangeFloor(true);
        DynamicNavigateParams mDynamicNavigateParams = new DynamicNavigateParams();
        mDynamicNavigateParams.mDynamicNavigationMode = DynamicNavigationMode.FOLLOW;
        mDynamicNavigateParams.mFloorId = getCurrentFloorId();
        mDynamicNavigateParams.mLineMode = LineMode.CHANGE_COLOR;
        mDynamicNavigateParams.mJudgeArriveLength = 3;

        navigateManager.initParams(mDynamicNavigateParams);
        navigateManager.setOnDynamicNavigateListener(new OnDynamicNavigateListener() {
            @Override
            public void onNavigate(DynamicNavigateWrapper wrapper) {
                handleOnDynamicNavigate(wrapper);
            }
        });

        palMapView.getMapView().moveToPoint(userCoordinate, true, 300);
        getOverLayerManager().animRefreshOverlay(500);

        palMapView.getMapView().postDelayed(new Runnable() {
            @Override
            public void run() {
                palMapView.getMapView().doCollisionDetection();
            }
        }, 50);
        navigateManager.start(userCoordinate, currentFloorId, 0);
    }

    @Override
    public void beginMockNavigate() {
        if (navigateManager == null) {
            palMapView.showMessage("模拟导航失败!!!");
            return;
        }

        isMockNavi.set(true);

        final CoordinateInfo[] coordinates = navigateManager.getSimulationLocationPoint(3);
        Observable
                .interval(1000, TimeUnit.MILLISECONDS)
                .take(coordinates.length)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        boolean b = IOUtils.checkMainThread();
                        LogUtil.e("checkUI :" + b);
                        CoordinateInfo coordinate = coordinates[aLong.intValue()];
                        myLocationListener.onMockLocation(
                                new LocationInfoModel(coordinate.x, coordinate.y, coordinate.floorId),
                                System.currentTimeMillis()
                        );
                        beginNavigate();
                    }
                });
    }


    @Override
    public void startSpeaking(final View v, String msg) {
    }

    @Override
    public void resetFeature() {
        if (mFeature != null) {
            palMapView.resetFeatureStyle(mFeature);
            mFeature = null;
        }
    }


    @Override
    public void startLocation(BeaconPositioningManager positioningManager) {
        if (BuildConfig.useSVA) {
            registerLocationListener();
            palMapView.getContext().startService(new Intent(
                    palMapView.getContext(),
                    LampSiteLocationService.class
            ));
        } else {
            if (this.positioningManager == null && positioningManager != null) {
                this.positioningManager = positioningManager;
                this.positioningManager.start();
                this.positioningManager.setOnLocationChangeListener(new PositioningManager.OnLocationChangeListener<BleLocation>() {
                    @Override
                    public void onLocationChange(PositioningManager.LocationStatus locationStatus, BleLocation bleLocation, BleLocation t1) {
                        if (PositioningManager.LocationStatus.MOVE == locationStatus && t1 != null) {
                            Coordinate coordinate = t1.getPoint().getCoordinate();
                            long locationFloorId = Location.floorId.get(t1.getProperties());
                            myLocationListener.onComplete(new LocationInfoModel(
                                            coordinate.getX(),
                                            coordinate.getY(),
                                            locationFloorId
                                    ),
                                    System.currentTimeMillis());
                        } else {
                            if (PositioningManager.LocationStatus.START != locationStatus || PositioningManager.LocationStatus.STOP != locationStatus) {
                                myLocationListener.onFailed(new IllegalArgumentException(), "定位失败");
                            }
                        }
                    }
                });
            }
        }
    }

    private boolean endNavigationDialogCanShow = true;

    /**
     * 导航结束 用户到达终点附近
     */
    private void onNavigationEnd() {
        if (!endNavigationDialogCanShow) {
            return;
        }
        endNavigationDialogCanShow = false;
        if (isMockNavi.compareAndSet(true, false)) {
            userCoordinate = null;
            getOverLayerManager().hideLocation();
            resetFeature();
        }
        palMapView.showNavigationEndView(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                endNavigationDialogCanShow = true;
                getOverLayerManager().clearAllNotLocation();
                palMapView.clearNavigateRoad();
                navigateManager.clear();
                setPalmapViewState(PalmapViewState.Normal);
                palMapView.hidePoiMenu();
                palMapView.showMapViewControl();
                dialog.dismiss();
                isMockNavi.set(false);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    /**
     * 检测是否需要切换楼层
     *
     * @param floorId 需要切换楼层的楼层ID
     * @return
     */
    private boolean checkFloorChange(long floorId) {
        //TODO 将定位信息添加到楼层切换控制器
        floorSwitchManager.putLocation(floorId);
        //TODO 如果当前可以切换楼层 那么就切换了
        if (floorSwitchManager.isCanSwitchFloor() && canAutoChangeFloor) {
            //楼层不一样 就可以换了
            floorSwitchManager.clearData();
            if (floorSwitchManager.getNextFloorId() != getCurrentFloorId()) {
                changeFloor(floorId);
                return true;
            }
        }
        return false;
    }

    @Override
    public void resume() {
        LogUtil.e("resume");
        isRun = true;
        if (locationSensorDelegate != null) {
            locationSensorDelegate.onResume();
        }
    }

    @Override
    public void pause() {
        LogUtil.e("pause");
        isRun = false;
        if (locationSensorDelegate != null) {
            locationSensorDelegate.onPause();
        }
    }

    @Override
    public void destroy() {
        DisposableUtils.unsubscribeAll(loadMapSubscribe, loadMapWithBuildingIdSubscription);
        if (navigateManager != null && !navigateManager.getPtr().isRelase()) {
            navigateManager.clear();
            navigateManager.drop();
        }
        if (mapDataCache != null) {
            for (Map.Entry<Long, PlanarGraph> entry : mapDataCache.entrySet()) {
                entry.getValue().drop();
            }
            mapDataCache.clear();
            mapDataCache = null;
        }
        unRegisterLocationListener();
    }

    private void registerLocationListener() {
        if (null == locationBroadcastReceiver) {
            LogUtil.e("注册定位广播回调");
            locationBroadcastReceiver = new LocationBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(LampSiteLocationService.LOCATION_ACTION);
            palMapView.getContext().registerReceiver(locationBroadcastReceiver, intentFilter);
        }
    }

    private class LocationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null
                    || !intent.getAction().equals(LampSiteLocationService.LOCATION_ACTION)) {
                return;
            }
            int stateTag = intent.getIntExtra(LampSiteLocationService.STATE_TAG, LampSiteLocationService.STATE_FAILED);
            if (stateTag == LampSiteLocationService.STATE_COMPLETE) {
                LogUtil.e("定位成功");
                myLocationListener.onComplete((LocationInfoModel) intent.getParcelableExtra(LampSiteLocationService.MODEL_LOCATIONINFO),
                        intent.getLongExtra(LampSiteLocationService.TIMESTAMP, -1));
            } else {
                LogUtil.e("定位失败");
                myLocationListener.onFailed((Exception) intent.getSerializableExtra(LampSiteLocationService.EXCEPTION),
                        intent.getStringExtra(LampSiteLocationService.ERROR_MSG));
            }
        }
    }

    private void unRegisterLocationListener() {
        if (locationBroadcastReceiver != null) {
            palMapView.getContext().unregisterReceiver(locationBroadcastReceiver);
        }
    }

    private void setCurrentFloorId(long currentFloorId) {
        this.currentFloorId = currentFloorId;
    }

    @Override
    public void loadLightEventActivity() {
    }

    /**
     * 开始一个点亮活动
     */
    @Override
    public void beginLightEventAty(final Api_ActivityInfo api_activityInfo, final int atyId) {
    }

    @Override
    public void toggleLightEventMarkShow(boolean canShow) {
        getOverLayerManager().toggleLightEventMarkShow(canShow);
    }

    @Override
    public void shareCompleteAty(int id) {
        activityInfoBusiness.updateStateWitiAtyId(id, 3);
    }

    /**
     * 加载推送活动
     */
    @Override
    public void loadPushEventActivity() {
    }

    @Override
    public void refreshData() {
        nextFloorId = ID_NONE;
    }

    @Override
    public void closeLightAty() {
        activityInfoBusiness.clear();
        coordinateBusiness.clear();
        getOverLayerManager().removeAllLightEventMark();
    }

    /**************************************
     * ======listener====
     ********************************************************/
    @Override
    public void onSingleTap(MapView mapView, float x, float y) {
        if (null == palMapView) {
            return;
        }
        if (state.equals(PalmapViewState.Navigating) || state == PalmapViewState.RoutePlanning) {
            return;
        }

        Feature feature = mapView.selectFeature(x, y);

//        Types.Point p = mapView.converToWorldCoordinate(x, y);
//        LogUtil.e("点击坐标:" + p.x + ":" + p.y);
//        fencingManager.eventLocationData(getCurrentFloorId(), new Coordinate(p.x, p.y));
//        lightEventFencingManager.eventLocationData(getCurrentFloorId(), new Coordinate(p.x, p.y));

//        if (feature != null) {
//            Coordinate coordinate = feature.getCentroid();
//            LogUtil.e("点击坐标:" + coordinate.x + ":" + coordinate.y);
//            LogUtil.e("name:" + MapParam.getName(feature));
//            LogUtil.e("featureCategoryId:" + MapParam.getCategoryId(feature));
//            LogUtil.e("featureId:" + MapParam.getId(feature));
//        }

        //恢复变色的feature
        resetFeature();
        if (feature != null) {

            long categoryId = MapParam.getCategoryId(feature);
            if (categoryId == 37000000 //室外道路不可点击
                    || categoryId == 23062000 //中空区域不可点击
                    || categoryId == 23999000) {//不可到达区域不可点击
                palMapView.hidePoiMenu();
                featureId = ID_NONE;
                removeTapAndPoiMark();
                return;
            }

            getOverLayerManager().animRefreshOverlay(300);
            PoiModel poiModel;
            // TODO: 2016/6/23 如果点击的是中空区域
            if (MapParam.getCategoryId(feature) == 23062000) {
                //中空区域
                palMapView.hidePoiMenu();
                return;
            }

            if (MapParam.getId(feature) == currentFloorId) {
                //点击在楼道
                poiModel = new PoiModel();
                poiModel.setName(palMapView.getContext().getString(R.string.ngr_theWay));

                Types.Point wordPoint = mapView.converToWorldCoordinate(x, y);
                poiModel.setX(wordPoint.x);
                poiModel.setY(wordPoint.y);
            } else {
                poiModel = new PoiModel(feature);
                // TODO: 2016/6/22 检查当前feature是不是一个设施
                if (!TextUtils.isEmpty(poiModel.getName())) {
                    palMapView.readFeatureColor(feature, 0xffFFB5B5);
                }
//                Coordinate featureCentroid = feature.getCentroid();
                Coordinate featureCentroid = feature.getTextureCentroid();
                poiModel.setX(featureCentroid.x);
                poiModel.setY(featureCentroid.y);
            }
            poiModel.setZ(currentFloorId);
            // TODO: 2016/7/13 地址加上场馆名
            getOverLayerManager().refreshTapMark(currentFloorId, new double[]{
                    poiModel.getX(), poiModel.getY()
            });
            palMapView.getMapView().moveToPoint(new Coordinate(poiModel.getX(), poiModel.getY()), true, 300);

            //判断当前处于什么状态
            palMapView.showPoiMenu(poiModel, state);

            mFeature = feature;
        } else {
            palMapView.hidePoiMenu();
            featureId = ID_NONE;
            removeTapAndPoiMark();
        }
    }

    @Override
    public void removeTapAndPoiMark() {
        getOverLayerManager().removeTapMark();
        getOverLayerManager().removePoiMark();
    }

    @Override
    public void onChangePlanarGraph(final PlanarGraph planarGraph, final PlanarGraph planarGraph1, final long oldFloorId, final long newFloorId) {
        IOUtils.postMainThread(new Runnable() {
            @Override
            public void run() {
                setCurrentFloorId(newFloorId);
                nextFloorId = ID_NONE;
                palMapView.onFloorChanged(oldFloorId, newFloorId);

                if (oldFloorId == newFloorId && mFeature != null) {
                    palMapView.resetFeatureStyle(mFeature);
                }
                mFeature = null;
                currentPlanarGraph = planarGraph1;
                if (!isHaveOutSide) {
                    palMapView.visibilityOutSideView(false);
                } else {
                    palMapView.visibilityOutSideView(!inOutSide());
                }
                getOverLayerManager().removeTapMark();
                getOverLayerManager().onChangePlanarGraph(oldFloorId, newFloorId);
                if (navigateManager != null && newFloorId != oldFloorId) {
                    navigateManager.switchPlanarGraph(newFloorId);
                }
                if (featureId != ID_NONE) {
                    mFeature = palMapView.getMapView().selectFeature(featureId);
                    if (mFeature != null) {
                        Runnable tempTask = new Runnable() {
                            @Override
                            public void run() {
                                setPalmapViewState(PalmapViewState.Normal);
                                PoiModel poiModel = new PoiModel(mFeature);
                                poiModel.setZ(currentFloorId);
                                LogUtil.e("添加addPoiMark");
                                getOverLayerManager().addPoiMark(currentFloorId, featureId);
                                LogUtil.e("showPoiMenu:" + state);
                                palMapView.showPoiMenu(poiModel, state);
                                palMapView.readFeatureColor(mFeature, 0xffFFB5B5);

                                palMapView.getMapView().moveToPoint(
                                        palMapView.getMapView().selectFeature(featureId).getCentroid(),
                                        true,
                                        300
                                );
                                getOverLayerManager().animRefreshOverlay(300);

                            }
                        };
                        if (oldFloorId == newFloorId) {
                            Coordinate tempCoordinate = mFeature.getCentroid();
                            palMapView.getMapView().moveToPoint(tempCoordinate, true, 500);
                            IOUtils.postMainThreadDelayed(tempTask, 500);
                        } else {
                            IOUtils.postMainThread(tempTask);
                        }

                    } else {
                        palMapView.hidePoiMenuAtFloorChange();
                    }
                } else {
                    featureId = ID_NONE;
                    palMapView.hidePoiMenuAtFloorChange();
                }

                palMapView.resetFailityMenu();

                // TODO: 2016/9/12 切换导航线上下行view
                LogUtil.e("state:" + state);
                switchRouteArrowView();
            }
        });
    }

    /**
     * 控制导航线上下行view
     */
    private void switchRouteArrowView() {
        if (getOverLayerManager().getEndMark() == null || state != PalmapViewState.Navigating) {
            palMapView.hideRouteArrowView();
            return;
        }
        int floorCompare = FloorUtils.compare(
                getCurrentFloorName(),
                getOverLayerManager().getEndMark().getNavigationPointModel().getFloorName());
        if (floorCompare > 0) {//提示下行
            palMapView.showRouteDownView();
        }
        if (floorCompare < 0) {//提示上行
            palMapView.showRouteUpView();
        }
        if (floorCompare == 0) {
            palMapView.hideRouteArrowView();
        }
    }

    @Override
    public boolean onDoubleTap(MapView mapView, float v, float v1) {
        palMapView.refreshScaleView();
        return true;
    }

    @Override
    public void onLongPress(MapView mapView, float x, float y) {
    }

    private void handleOnDynamicNavigate(DynamicNavigateWrapper wrapper) {
        float mRemainingLength = wrapper.mDynamicNavigateOutput.mRemainingLength;//剩余距离
        if (userCoordinate.getZ() - getOverLayerManager().getEndMark().getFloorId() == 0 &&
                mRemainingLength < Config.END_NAVI_DISTANCE) {
            onNavigationEnd();
            return;
        }
        palMapView.getMapView().navigateOperate(wrapper.mNavigateMapOperate);
        palMapView.readRemainingLength(mRemainingLength);
        palMapView.refreshScaleView();
        palMapView.refreshCompassView(1000);
        getOverLayerManager().animRefreshOverlay(1000);
    }

    private void eventFencing(Coordinate userCoordinate, long locationFloorId) {
        if (lightEventFencingManager != null) {
            lightEventFencingManager.eventLocationData(locationFloorId, userCoordinate);
        }
    }

    private void initPDR(double x, double y) {
        if (userCoordinate == null) {
            pdr.initLocation(new LocationPair(x, y,
                    System.currentTimeMillis()));
        }
    }

    private class NavigateListener implements NavigateManager.OnNavigateComplete {
        @Override
        public void onNavigateComplete(final NavigateManager.NavigateState navigateState, final FeatureCollection featureCollection) {
            palMapView.hideLoading();
            palMapView.hideRetry();
            navigationOutOfBounds = 0;
            LogUtil.e("NavigateState:" + navigateState);

            pmpDynamicNavigationManager.config(navigateManager, featureCollection);

            // TODO: 2016/6/21 如果请求导航线成功（包含切割）
            if (navigateState == NavigateManager.NavigateState.OK
                    || navigateState == NavigateManager.NavigateState.CLIP_NAVIGATE_SUCCESS
                    || navigateState == NavigateManager.NavigateState.SWITCH_NAVIGATE_SUCCESS) {
                IOUtils.postMainThread(new Runnable() {
                    @Override
                    public void run() {
                        StartMark startMark = getOverLayerManager().getStartMark();
                        EndMark endMark = getOverLayerManager().getEndMark();
                        if (!(navigateState == NavigateManager.NavigateState.CLIP_NAVIGATE_SUCCESS)) {
                            // TODO: 2016/9/12 移动起始点的mark 对其导航线
                            if (requestNaviRoad) {
                                setCanAutoChangeFloor(true);
                                // TODO: 2016/9/12 如果是第一次从服务器请求路线 并且当前楼层和起始点不一致 要切换楼层
                                if (getCurrentFloorId() - startMark.getFloorId() != 0) {
                                    changeFloor(startMark.getFloorId());
                                }
                                envelopeCoordinateArr = featureCollection.getFeature(0).getEnvelope();
                                palMapView.getMapView().moveToRect(
                                        envelopeCoordinateArr[0].getX(),
                                        envelopeCoordinateArr[0].getY(),
                                        envelopeCoordinateArr[1].getX(),
                                        envelopeCoordinateArr[1].getY(),
                                        true,
                                        200
                                );
//                                getOverLayerManager().refreshOverlayDelayed(200);
                                getOverLayerManager().animRefreshOverlay(300);

                            }
                            requestNaviRoad = false;
                        }
                        if (featureCollection.getSize() > 0) {
                            palMapView.getMapView().getOverlayController().refresh();
                            // TODO: 2016/9/12 获取路线长度
                            if (state != PalmapViewState.Navigating) {
                                naviTotalLength = (int) navigateManager.getTotalLineLength() + 1;
                                palMapView.showRouteInfoDetails(naviTotalLength + "m");
                            }
                            // TODO: 2016/10/13 获取导航线所在楼层的集合
                            //获取导航线所在楼层的ID集合
                            palMapView.readNavigateRoad(navigateManager.getAllPlanarGraphId(), featureCollection);

                            // TODO: 2017/2/15 如果起始点不在同一个楼层 需要在起点的楼层电梯处添加电梯的mark
                            if (startMark.getFloorId() - endMark.getFloorId() != 0
                                    && currentFloorId == startMark.getFloorId()
                                    ) {
                                //导航线的终点
                                long connectInfoCategoryId = 0;
                                Coordinate endCoordinate = null;
                                ConnectedInfo[] connectInfo = navigateManager.getConnectInfo();
                                for (int i = 0; i < connectInfo.length; i++) {
                                    ConnectedInfo info = connectInfo[i];
                                    LogUtil.e("infoName:" + info.mName);
                                    if (info.getFloorId() == currentFloorId) {
                                        connectInfoCategoryId = info.getCategoryId();
                                        Feature endFeature = featureCollection.getEedFeature();
                                        endCoordinate = navigateManager.getCoordinateByFeature(endFeature, navigateManager.getFeatureLength(endFeature) - 1);
                                        getOverLayerManager().refreshLadderMark(startMark.getFloorId(), new double[]{endCoordinate.getX(), endCoordinate.getY()}
                                                , connectInfoCategoryId,
                                                currentFloorId == Config.ID_FLOOR_F1 ? "至F2" : "至F1"
                                        );
                                        break;
                                    }
                                }
                            }

                        } else {
                            resetState();
                        }
                    }
                });

            } else {
                // TODO: 2016/6/27 切割导航线失败
                if (navigateState == NavigateManager.NavigateState.CLIP_NAVIGATE_ERROR) {
//                palMapView.showErrorMessage("没找到导航线");
                    palMapView.hidePoiMenu();
                    resetState();
                    return;
                }
                // TODO: 2017/2/15 如果是没有当前楼层的导航线 不提示
                if (navigateManager.getAllPlanarGraphId().length > 0) {
                    return;
                }
                // TODO: 2016/6/23 获取导航线失败
                if (navigateState == NavigateManager.NavigateState.NAVIGATE_NOT_FOUND) {
                    palMapView.showErrorMessage(palMapView.getContext().getString(R.string.ngr_msg_navigateNotFound));
                    return;
                } else {// TODO: 2016/6/27 其他错误
                    palMapView.showMessage("导航线获取失败，请重新选择");
                    palMapView.hidePoiMenu();
                    resetState();
                }
            }
        }
    }

    private class MyLocationListener implements LocationListener {

        private int locationErrorMsgCount = 6;
        private static final int LOCATION_ERROR_MSG_MAX_COUNT = 5;

        @Override
        public void onComplete(LocationInfoModel locationInfoModel, long timeStamp) {
            locationErrorMsgCount = 0;
            palMapView.showLocationStatus(PositioningManager.LocationStatus.MOVE);
            // TODO: 2017/1/4 默认定位的楼层id为1F
            long locationFloorId = (long) locationInfoModel.getZ();
            initPDR(locationInfoModel.getX(), locationInfoModel.getY());

            Coordinate locationCoordinate = null;

            if (palMapView.canUsePDR()) {
                pdr.start();
                double[] dr = pdr.dr(new double[]{locationInfoModel.getX(), locationInfoModel.getY()});
                locationCoordinate = new Coordinate(dr[0], dr[1]);
            } else {
                locationCoordinate = new Coordinate(locationInfoModel.getX(), locationInfoModel.getY());
                pdr.stop();
            }
            locationCoordinate.setZ(locationFloorId);

            eventFencing(locationCoordinate, locationFloorId);

            // TODO: 2016/6/22 如果下一个楼层id和当前楼层Id不一致 说明正在处于切换楼层动作中
            if (palMapView.isRetry() || nextFloorId != ID_NONE && getCurrentFloorId() != nextFloorId) {
                floorSwitchManager.clearData();
                return;
            }
            if (!isRun || checkFloorChange(locationFloorId)) return;

            // TODO: 2017/4/10 正常移动定位图标
            if ((getState() != PalmapViewState.RoutePlanning && getState() != PalmapViewState.Navigating)
                    || navigateManager.getMinDistanceByPoint(locationCoordinate) > Config.MIN_DISTANCE) {
                navigationOutOfBounds++;
                userCoordinate = locationCoordinate;
                if (getState() == PalmapViewState.Navigating) {
                    if (navigationOutOfBounds >= maxNavigationOutOfBounds) {
                        navigationOutOfBounds = 0;
                        LogUtil.e("outOfBounds = 0 :" + navigationOutOfBounds);
                        //重新规划线条
                        palMapView.showLocationMsgView(palMapView.getContext().getString(R.string.ngr_msg_outOfNavigation), palMapView.getContext().getString(R.string.ngr_msg_sub_outOfNavigation));
                        palMapView.showLoading();
                        //起点终点都设置了
                        getOverLayerManager().addStartMark(createNavigationPointModelWithPoiModel(new PoiModel()),
                                userCoordinate.getX(),
                                userCoordinate.getY());
                    } else {
                        if (navigationOutOfBounds == maxNavigationOutOfBounds / 2) {
                            palMapView.showErrorMessage("您与导航线偏差过大,即将为您更换最优路线");
                        }
                    }
                }
                if (locationFloorId == getCurrentFloorId()) {
                    getOverLayerManager().animLocation(userCoordinate, locationFloorId);
                } else {
                    getOverLayerManager().hideLocation();
                }
                return;
            }
            if (null == userCoordinate) {
                return;
            }

            // TODO 如果当前楼层ID和终点楼层ID一致 并且当前定位点和终点的距离小于临界值 则导航成功
            if (userCoordinate.getZ() - getOverLayerManager().getEndMark().getFloorId() == 0 &&
                    MapUtils.pointDistance(
                            locationCoordinate.getX(),
                            locationCoordinate.getY(),
                            getOverLayerManager().getEndMark().getWorldX(),
                            getOverLayerManager().getEndMark().getWorldY()
                    ) < Config.END_NAVI_DISTANCE) {
                onNavigationEnd();
                return;
            }
            // TODO: 2016/7/11 路网吸附
            final Coordinate pointOfIntersectioan = navigateManager.getPointOfIntersectioanByPoint(locationCoordinate);
            userCoordinate.setX(pointOfIntersectioan.getX());
            userCoordinate.setY(pointOfIntersectioan.getY());
            if (locationFloorId == getCurrentFloorId()) {
                getOverLayerManager().animLocation(userCoordinate, locationFloorId);
                if (getState() == PalmapViewState.Navigating) {
                    navigateManager.dynamicNavigate(userCoordinate, currentFloorId, 0);
                }
            }
        }

        @Override
        public void onFailed(Exception ex, String msg) {
            locationErrorMsgCount++;
            if (locationErrorMsgCount > LOCATION_ERROR_MSG_MAX_COUNT) {
                palMapView.showLocationError(ex);
                locationErrorMsgCount = 0;
            }
        }

        @Override
        public void onMockLocation(LocationInfoModel locationInfoModel, long timeStamp) {
            long locationFloorId = (long) locationInfoModel.getZ();
            Coordinate locationCoordinate = new Coordinate(locationInfoModel.getX(), locationInfoModel.getY());
            locationCoordinate.setZ(locationFloorId);
            if (userCoordinate == null) {
                userCoordinate = locationCoordinate;
            }
            //模拟导航
            if (userCoordinate.getZ() - getOverLayerManager().getEndMark().getFloorId() == 0 &&
                    MapUtils.pointDistance(
                            locationCoordinate.getX(),
                            locationCoordinate.getY(),
                            getOverLayerManager().getEndMark().getWorldX(),
                            getOverLayerManager().getEndMark().getWorldY()
                    ) < Config.END_NAVI_DISTANCE) {
                onNavigationEnd();
                return;
            }
            // TODO: 2016/7/11 路网吸附
            final Coordinate pointOfIntersectioan = navigateManager.getPointOfIntersectioanByPoint(locationCoordinate);
            userCoordinate.setX(pointOfIntersectioan.getX());
            userCoordinate.setY(pointOfIntersectioan.getY());
            if (locationFloorId == getCurrentFloorId()) {
                getOverLayerManager().animLocation(userCoordinate, locationFloorId);
                if (getState() == PalmapViewState.Navigating) {
                    navigateManager.dynamicNavigate(userCoordinate, currentFloorId, 0);
                }
            }
        }
    }

    /**************************************
     * ======listener====
     ********************************************************/
}
