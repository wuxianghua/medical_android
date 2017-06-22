package com.palmap.exhibition.view.impl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.R;
import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.exception.NetWorkTypeException;
import com.palmap.exhibition.launcher.LauncherModel;
import com.palmap.exhibition.listenetImpl.MapOnZoomListener;
import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.exhibition.model.Api_PositionInfo;
import com.palmap.exhibition.model.ExFloorModel;
import com.palmap.exhibition.model.FacilityModel;
import com.palmap.exhibition.model.PoiModel;
import com.palmap.exhibition.model.ServiceFacilityModel;
import com.palmap.exhibition.other.delegate.ViewAnimDelegate;
import com.palmap.exhibition.presenter.PalMapViewPresenter;
import com.palmap.exhibition.service.LampSiteLocationService;
import com.palmap.exhibition.view.FloorDataProvides;
import com.palmap.exhibition.view.PalMapView;
import com.palmap.exhibition.view.adapter.FacilitiesListAdapter;
import com.palmap.exhibition.view.adapter.FloorListAdapter;
import com.palmap.exhibition.view.base.ExActivity;
import com.palmap.exhibition.widget.CompassView;
import com.palmap.exhibition.widget.RouteInfoView;
import com.palmap.exhibition.widget.Scale;
import com.palmap.exhibition.widget.TaskShareDialog;
import com.palmap.exhibition.widget.YanTaiPoiMenuLayout;
import com.palmap.library.model.LocationType;
import com.palmap.library.utils.ActivityUtils;
import com.palmap.library.utils.DeviceUtils;
import com.palmap.library.utils.IOUtils;
import com.palmap.library.utils.LogUtil;
import com.palmap.library.utils.ViewUtils;
import com.palmaplus.nagrand.core.Types;
import com.palmaplus.nagrand.core.Value;
import com.palmaplus.nagrand.data.Feature;
import com.palmaplus.nagrand.data.FeatureCollection;
import com.palmaplus.nagrand.data.LocationList;
import com.palmaplus.nagrand.data.LocationModel;
import com.palmaplus.nagrand.data.PlanarGraph;
import com.palmaplus.nagrand.position.PositioningManager;
import com.palmaplus.nagrand.position.ble.BeaconPositioningManager;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.layer.FeatureLayer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

public class PalmapViewActivity extends ExActivity<PalMapViewPresenter> implements PalMapView, FloorDataProvides, View.OnClickListener {

    private static final String KEY_LOCATION_ID = "key_location_id";
    private static final String KEY_LOCATION_TYPE = "key_location_type";
    private static final String KEY_FLOOR_ID = "key_floor_id";
    private static final String KEY_FEATURE_ID = "key_feature_id";
    private static final String KEY_MAP_ID = "key_map_id";
    private static final String KEY_SEARCH_TEXT = "key_searchText";
    private static final String KEY_TITLE_NAME = "key_title_name";
    private static final String KEY_IS_TRAVEL_PLANNING = "key_isTravelPlanning";
    private static final int CODE_SEARCH_REQUEST = 1001;//搜索code

    @Inject
    PalMapViewPresenter presenter;
    @Inject
    BeaconPositioningManager beaconPositioningManager;
    MapView mapView;
    ListView facilitiesListView;
    ViewGroup layout_tv_floor;
    TextView tvFloor;
    ListView floorListView;
    CompassView compassView;
    ImageView mapLocation;
    RelativeLayout layoutServiceFacility;
    Scale scale;
    Toolbar toolBar;
    ViewGroup layout_overlay;
    TextView tvTitle;
    RelativeLayout containerRetry;
    TextView tvLocationMessage;
    YanTaiPoiMenuLayout layoutPoiMenu;
    ViewGroup layout_mapView;
    ViewGroup layoutBack;
    View layout_search;
    RouteInfoView routeInfoView;
    ImageView img_search;
    ViewGroup layout_location;
    ViewGroup layout_floor;
    FloorListAdapter floorListAdapter;
    FacilitiesListAdapter facilitiesListAdapter;
    ViewAnimDelegate floorListViewDelegate;
    ViewGroup layout_zoom;
    private FeatureLayer navigateLayer;

    /**
     * 是否添加了导航层
     */
    private boolean isAttachNavigateLayer = false;
    private ArrayList<ExFloorModel> floorModelList;

    private boolean usePDR = false;

    private PalmapViewActivity self;

    /**
     * 搜索字符串
     */
    private String searchText = null;

    public static void navigatorThis(Context that) {
        Intent intent = new Intent(that, PalmapViewActivity.class);
        that.startActivity(intent);
    }

    public static void navigatorThis(Context that, long mapId, long floorId, long featureId) {
        navigatorThis(that, null, mapId, floorId, featureId, false);
    }

    public static void navigatorThis(Context that, String title, long mapId, long floorId, long featureId, boolean isTravelPlanning) {
        Intent intent = new Intent(that, PalmapViewActivity.class);
        intent.putExtra(KEY_FLOOR_ID, floorId);
        intent.putExtra(KEY_FEATURE_ID, featureId);
        intent.putExtra(KEY_MAP_ID, mapId);
        intent.putExtra(KEY_TITLE_NAME, title);
        intent.putExtra(KEY_IS_TRAVEL_PLANNING, isTravelPlanning);
        that.startActivity(intent);
    }

    public static void navigatorThis(Context that, LauncherModel launcherModel) {
        if (launcherModel == null) return;
        AndroidApplication.getInstance().setLocationMapId(launcherModel.getMapId());
        navigatorThis(
                that,
                launcherModel.getTitle(),
                launcherModel.getMapId(),
                launcherModel.getFloorId(),
                launcherModel.getFeatureId(),
                launcherModel.isTravelPlanning()
        );
    }

    public static Intent getPoiSearchResultIntent(long locationModelId, long floorId, String searchText, LocationType.Type locationType) {
        Intent intent = new Intent();
        intent.putExtra(KEY_LOCATION_ID, locationModelId);
        intent.putExtra(KEY_LOCATION_TYPE, locationType);
        intent.putExtra(KEY_FLOOR_ID, floorId);
        intent.putExtra(KEY_SEARCH_TEXT, searchText);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.noBackground(this);
        setContentView(R.layout.yantai_view_palmap);
        self = this;
        initView();
        setSupportActionBar(toolBar);
        initStatusBar(R.color.ngr_colorPrimary);
        initMapView();
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            final long floorId = intent.getExtras().getLong(KEY_FLOOR_ID, -1);
            final long featureId = intent.getExtras().getLong(KEY_FEATURE_ID, -1);
            final long mapId = intent.getExtras().getLong(KEY_MAP_ID, -1);
            final String title = intent.getExtras().getString(KEY_TITLE_NAME, null);
            tvTitle.setText(title);
            presenter.attachView(self, mapId, floorId, featureId);
        } else {
            presenter.attachView(self);
        }
        IOUtils.postMainThreadDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.loadPushEventActivity();
            }
        }, 150);
    }

    private static int LAUNCHER_INDEX = 0;

    private void initMapView() {
        LogUtil.e("LAUNCHER_INDEX :" + LAUNCHER_INDEX);
        mapView = new MapView("default" + LAUNCHER_INDEX % 7, getContext());
        mapView.start();
        layout_mapView.addView(mapView);
        mapView.setBackgroundColor(0xfffafafa);
        mapView.setMinAngle(45);

        mapView.initRatio(1.1f);

        mapView.setMaxScale(1500);

        registerLister();
        LAUNCHER_INDEX++;
    }

    private void registerLister() {
        mapView.setOverlayContainer(layout_overlay);
        mapView.setMapOptions(presenter.configMap());

        mapView.setOnSingleTapListener(presenter);
        mapView.setOnChangePlanarGraph(presenter);
        mapView.setOnDoubleTapListener(presenter);
        mapView.setOnLongPressListener(presenter);

        mapView.setOnZoomListener(new MapOnZoomListener(scale, compassView));
    }

    protected PalMapViewPresenter inject() {
        daggerInject().inject(this);
        return presenter;
    }

    @Override
    public MapView getMapView() {
        return mapView;
    }

    @Override
    public void moveToLocationPoint() {
        if (presenter.getUserCoordinate() != null && presenter.getUserCoordinate().getZ() - presenter.getCurrentFloorId() == 0) {
            mapView.moveToPoint(presenter.getUserCoordinate());
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mapView.getOverlayController().refresh();
                }
            }, 50);
        }
    }

    @Override
    public void readTitle(String title) {
        if (tvTitle.getText() != null) {
            return;
        }
        tvTitle.setText(title);
    }

    @Override
    public void readMapData(final PlanarGraph planarGraph) {
        resetCompass();
        mapView.initRotate(Config.MAP_ANGLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mapView.drawPlanarGraph(planarGraph);
                mapView.setMaxScale(1500);
            }
        }).start();
        scale.setMapView(mapView);
    }

    @Override
    public void resetCompass() {
        compassView.setRotateAngle(Config.MAP_ANGLE);
    }

    @Override
    public void refreshScaleView() {
        if (scale != null) scale.postInvalidate();

    }

    @Override
    public void refreshCompassView(long time) {
        if (compassView != null) {
            if (time > 0) {
                compassView.animRefresh(mapView, time);
            } else {
                compassView.setRotateAngle(-(float) mapView.getRotate());
            }
        }
    }

    @Override
    public void resetFeatureStyle(Feature feature) {
        if (feature == null) return;
        mapView.resetOriginStyle("Area", LocationModel.id.get(feature));
    }

    @Override
    public void readFeatureColor(Feature feature, int color) {
        mapView.setRenderableColor("Area", LocationModel.id.get(feature), color);
    }

    @Override
    public void showPoiMenu(final PoiModel poiModel, PalmapViewState state) {
        layoutPoiMenu.setHaveLocationPoint(!(null == presenter.getUserCoordinate()));
        if (poiModel == null) {
            layoutPoiMenu.refreshView(state);
            return;
        }
        layoutPoiMenu.refreshView(poiModel, state);
        layoutPoiMenu.animShow();
    }


    @Override
    public void hidePoiMenuAtFloorChange() {
        if (!layoutPoiMenu.layoutTipIsShow() && presenter.getState() != PalmapViewState.Navigating) {
            hidePoiMenu();
        }
    }


    @Override
    public void hidePoiMenu() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (presenter.getState().equals(PalmapViewState.Normal)
                        || presenter.getState().equals(PalmapViewState.ENd_SET)) {
                    layoutPoiMenu.animHide();
                } else {
                    showPoiMenu(null, presenter.getState());
                    hidePoiMenuTop();
                }
            }
        });
    }

    @Override
    public void hidePoiMenuTop() {
    }

    @Override
    public void showLocationMsgView(String title, String msg) {
    }

    @Override
    public int getWindowRotation() {
        return getWindowManager().getDefaultDisplay().getRotation();
    }

    @Override
    public void readNavigateRoad(long[] floorIds, FeatureCollection featureCollection) {
        attachNavigateLayer();
        navigateLayer.clearFeatures();
        navigateLayer.addFeatures(featureCollection);
        layoutPoiMenu.showRouteInfo();
    }

    @Override
    public void clearNavigateRoad() {
        if (navigateLayer != null && isAttachNavigateLayer) {
            navigateLayer.clearFeatures();
            detachNavigateLayer();
        }
    }

    @Override
    public void showLocationStatus(PositioningManager.LocationStatus status) {
        switch (status) {
            case START:
                showLocationMsgView(getString(R.string.ngr_msg_locationing));
                break;
            case ERROR:
                mapLocation.setSelected(false);
                showLocationMsgView(getString(R.string.ngr_msg_locationErr));
                break;
            case MOVE:
                mapLocation.setSelected(true);
            default:
                tvLocationMessage.setVisibility(View.GONE);
                break;
        }
    }

    private void showLocationMsgView(String msg) {
        tvLocationMessage.setText(msg);
        tvLocationMessage.setVisibility(View.VISIBLE);
        AnimationDrawable ellipsis =
                (AnimationDrawable) ContextCompat.getDrawable(getContext(), R.drawable.dancing_ellipsis);
        tvLocationMessage.setCompoundDrawablesWithIntrinsicBounds(null, null, ellipsis, null);
        ellipsis.start();

        handler.postDelayed(goneLocationMsgViewTask, 5000);
    }

    /**
     * 移除定位提示语
     */
    private Runnable goneLocationMsgViewTask = new Runnable() {
        @Override
        public void run() {
            if (tvLocationMessage != null) {
                tvLocationMessage.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void showLocationError(final Throwable throwable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (throwable instanceof NetWorkTypeException) {
                    LogUtil.e("非联通4G无法定位");
                    showLocationMsgView(getString(R.string.ngr_msg_locationErr));
                    mapLocation.setSelected(false);
                } else {
                    showLocationMsgView(getString(R.string.ngr_msg_locationErr));
                }
            }
        });
    }

    @Override
    public void readFloorData(LocationList locationList, long selectFloorId) {
        if ((locationList == null || locationList.getSize() == 0) && selectFloorId == 0) {
            if (floorListAdapter != null) {
                floorListAdapter.clear();
                floorListAdapter = null;
            }
            layout_tv_floor.setVisibility(View.INVISIBLE);
            floorListView.setVisibility(View.GONE);
            return;
        }
        this.floorModelList = new ArrayList<>();
        for (int i = locationList.getSize() - 1; i >= 0; i--) {
            ExFloorModel temp = new ExFloorModel(locationList.getPOI(i));
            if (temp.isFloor()) {
                floorModelList.add(temp);
            }
        }
        floorListAdapter = new FloorListAdapter(this, floorModelList, selectFloorId);
        floorListViewDelegate = new ViewAnimDelegate(floorListView, ViewAnimDelegate.Gravity.Bottom);
        floorListView.setAdapter(floorListAdapter);
        if (floorListAdapter.getCount() > 6) {
            ViewUtils.setListViewBasedOnItem(floorListView, 6, false);
        }
        layout_tv_floor.setVisibility(View.VISIBLE);
        tvFloor.setText(floorListAdapter.getLocationModelName(selectFloorId));
        floorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  LocationModel locationModel = floorListAdapter.getItem(position);
                ExFloorModel floorModel = floorListAdapter.getItem(position);
                long nextFloorId = floorModel.getId();
                if (nextFloorId == presenter.getCurrentFloorId()) {
                    floorListViewDelegate.hideOnAnim();
                    return;
                }
                //TODO 准备切换楼层 先去除所有覆盖层
                mapView.removeAllOverlay();
                //TODO 自动手动切换了楼层 就取消自动切换楼层的支持了
                presenter.setCanAutoChangeFloor(false);
                LogUtil.d("changeFloor:" + nextFloorId);
                presenter.changeFloor(nextFloorId);
            }
        });
    }

    @Override
    public void onFloorChanged(final long oldFloorId, final long newFloorId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mapView.doCollisionDetection();
                        hideLoading();
                    }
                }, 500);
                clearFacilityListSelect();
                if (floorListAdapter != null) {
                    floorListAdapter.setSelectFloorId(newFloorId);
                    floorListAdapter.notifyDataSetChanged();
                    tvFloor.setText(floorListAdapter.getLocationModelName(newFloorId));
                    floorListViewDelegate.hideOnAnim();
                }
                if (oldFloorId != newFloorId) {
                    isAttachNavigateLayer = false;
                }
            }
        });
    }

    @Override
    public void visibilityOutSideView(boolean isShow) {
    }

    /**
     * 清除设施选中item
     */
    @Override
    public void clearFacilityListSelect() {
        if (facilitiesListAdapter != null) {
            facilitiesListAdapter.setSelectPosition(-1);
        }
    }

    @Override
    public void showNavigationEndView(DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("您已到达目的地");
        builder.setNegativeButton("取消", onClickListener1)
                .setPositiveButton("确定", onClickListener);
        builder.create().show();
    }

    @Override
    public boolean canUsePDR() {
        return usePDR;
    }

    @Override
    public void readRemainingLength(float mRemainingLength) {
        layoutPoiMenu.readRemainingLength((int) mRemainingLength);
    }

    @Override
    public void readPubFacility(final List<FacilityModel> data) {
        if (data.size() == 0) return;
        facilitiesListAdapter = new FacilitiesListAdapter(getContext(), data);
        facilitiesListView.setAdapter(facilitiesListAdapter);
        RxAdapterView.itemClicks(facilitiesListView)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer position) {
                        if (facilitiesListAdapter.getSelectPosition() == position) {
                            presenter.clearFacilityMarks();
                            facilitiesListAdapter.setSelectPosition(-1);
                            return;
                        }
                        presenter.clearFacilityMarks();
                        ArrayList<Long> ids = data.get(position).getIds();
                        List<Feature> facilityModelCoordinates = new ArrayList<>();
                        for (int i = 0; i < ids.size(); i++) {
                            List<Feature> tempResult = mapView.searchFeature("Area", "category", new Value(ids.get(i)));
                            if (tempResult != null && tempResult.size() > 0) {
                                facilityModelCoordinates.addAll(tempResult);
                            }
                        }
                        if (facilityModelCoordinates.size() == 0) {
                            showMessage(getString(R.string.ngr_msg_facility_none));
                            facilitiesListAdapter.setSelectPosition(-1);
                            return;
                        } else {
                            facilitiesListAdapter.setSelectPosition(position);
                            presenter.addFacilityMarks(facilityModelCoordinates);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtil.e(throwable.toString());
                    }
                });
    }

    @Override
    public void readServiceFacility(List<ServiceFacilityModel> data) {
    }

    void floorClick() {
        if (presenter.getState() == PalmapViewState.Navigating) {
            return;
        }
        if (floorListViewDelegate != null) {
            floorListViewDelegate.switchVisibilityOnAnim();
            floorListView.smoothScrollToPosition(floorListAdapter.getSelectIndex());
        }
    }

    void compassViewClick() {
        if (compassView.isSelected()) {
            mapView.rotateByNorth(new Types.Point(DeviceUtils.getWidth(this) / 2, DeviceUtils.getHeight(this) / 2), Config.MAP_ANGLE);
            compassView.setRotateAngle(Config.MAP_ANGLE);
        } else {
            mapView.rotateByNorth(new Types.Point(DeviceUtils.getWidth(this) / 2, DeviceUtils.getHeight(this) / 2), 0);
            compassView.reset();
        }
        delayedDoCollisionDetection();
        compassView.setSelected(!compassView.isSelected());
    }

    private void delayedDoCollisionDetection() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mapView != null) {
                    mapView.doCollisionDetection();
                }
            }
        }, 50);
    }

    void locationClick() {
        presenter.startLocation(beaconPositioningManager);//实用lampsite点位 传null
    }

    void backClick() {
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_SEARCH_REQUEST && resultCode == RESULT_OK) {
            // TODO: 2016/6/29 获取poi搜索结果
            try {
                LocationType.Type locationType = (LocationType.Type) data.getExtras().getSerializable(KEY_LOCATION_TYPE);
                searchText = data.getExtras().getString(KEY_SEARCH_TEXT);
                if (null == locationType) return;
                long locationId = data.getExtras().getLong(KEY_LOCATION_ID);
                long floorId = data.getExtras().getLong(KEY_FLOOR_ID);
                switch (locationType) {
                    case FLOOR:
                        presenter.setCanAutoChangeFloor(false);
                        presenter.changeFloor(locationId);
                        break;
                    default:
                        presenter.setCanAutoChangeFloor(false);
                        presenter.changeFloorAddMark(floorId, locationId);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null && !mapView.getPtr().isRelase()) {
            mapView.drop();
        }
        // TODO: 2016/7/24 恢复语言
        Resources resources = getResources();//获得res资源对象
        Configuration config = resources.getConfiguration();//获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
        config.locale = Config.oldLanguage;
        resources.updateConfiguration(config, dm);
        // TODO: 2016/10/26 移除隐藏定位提示语的任务 防止内存泄漏
        handler.removeCallbacks(goneLocationMsgViewTask);
    }

    @Override
    public void showRetry(final Throwable throwable, final Runnable runnable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.ngr_progressDialogTitle)).setMessage(getString(R.string.ngr_errorMsg));
                builder.setPositiveButton(getString(R.string.ngr_reload), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        runOnUiThread(runnable);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        presenter.refreshData();
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public void showRetryDialog(final Throwable throwable, final Runnable runnable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.ngr_progressDialogTitle)).setMessage(throwable.getMessage());
                builder.setPositiveButton(getString(R.string.ngr_reload), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        runOnUiThread(runnable);
                    }
                });
                builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        presenter.resetState();
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public void hideRetry() {
        super.hideRetry();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                containerRetry.removeAllViews();
                containerRetry.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean isRetry() {
        return !(containerRetry.getChildCount() == 0);
    }

    @Override
    public boolean checkFloorIdOnCurrentBuilding(long floorId) {
        if (null == floorListAdapter) {
            return false;
        }
        return floorListAdapter.checkFloorId(floorId);
    }

    @Override
    public void resetFailityMenu() {
    }

    @Override
    public void hideMapViewControl() {
        img_search.setVisibility(View.GONE);
        floorListView.setVisibility(View.INVISIBLE);
        layout_floor.setVisibility(View.INVISIBLE);
        scale.setVisibility(View.INVISIBLE);
        layout_location.setVisibility(View.INVISIBLE);
        layout_zoom.setVisibility(View.INVISIBLE);
        facilitiesListView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMapViewControl() {
        img_search.setVisibility(View.VISIBLE);
        layout_floor.setVisibility(View.VISIBLE);
        floorListView.setVisibility(View.VISIBLE);
        scale.setVisibility(View.VISIBLE);
        layout_location.setVisibility(View.VISIBLE);
        layout_zoom.setVisibility(View.VISIBLE);
        facilitiesListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRouteInfoStart(String lable, String msg) {
        layoutPoiMenu.showStartMsg(lable, msg);
    }

    @Override
    public void showRouteInfoEnd(String lable, String msg) {
        layoutPoiMenu.showEndMsg(lable, msg);
    }

    @Override
    public void hideRouteInfoView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                routeInfoView.setVisibility(View.GONE);
                routeInfoView.clear();
            }
        });
    }

    @Override
    public void showRouteUpView() {
        routeInfoView.showUpView();
    }

    @Override
    public void showRouteDownView() {
        routeInfoView.showDownView();
    }

    @Override
    public void hideRouteArrowView() {
        routeInfoView.hideArrowView();
    }

    /**
     * 显示网页推送信息
     *
     * @param model
     */
    @Override
    public void showPushView(final Api_PositionInfo.ObjBean model) {
    }

    /**
     * 显示点亮活动UI
     *
     * @param api_activityInfo
     */
    @Override
    public void readLightEventActivityList(final Api_ActivityInfo api_activityInfo) {
    }

    @Override
    public void showShareTaskView(Api_ActivityInfo.ObjBean objBean) {
        TaskShareDialog dialog = new TaskShareDialog(this, objBean);
        dialog.setOnShareComplete(new TaskShareDialog.OnShareComplete() {
            @Override
            public void onComplete(Api_ActivityInfo.ObjBean obj) {
                presenter.shareCompleteAty(obj.getId());
            }
        });
        dialog.show();
    }

    @Override
    public String getCurrentFloorName() {
        if (null == tvFloor) return "";
        return tvFloor.getText().toString();
    }

    @Override
    public void showRouteInfoDetails(String msg) {
        layoutPoiMenu.showDetailsMsg(msg);
    }

    /**
     * 附加上导航层
     */
    private void attachNavigateLayer() {
        if (isAttachNavigateLayer) return;
        isAttachNavigateLayer = true;
        //定位层
        navigateLayer = new FeatureLayer("navigate");
        mapView.addLayer(navigateLayer);
        mapView.setLayerOffset(navigateLayer);
    }

    private void detachNavigateLayer() {
        if (navigateLayer != null) {
            mapView.removeLayer(navigateLayer);
        }
        isAttachNavigateLayer = false;
    }

    private void initView() {
        tvFloor = findView(R.id.tv_floor);
        floorListView = findView(R.id.floorListView);
        compassView = findView(R.id.compassView);
        compassView.setAlpha(.9f);

        mapLocation = findView(R.id.map_location);
        layout_tv_floor = findView(R.id.layout_tv_floor);
        layoutServiceFacility = findView(R.id.layout_serviceFacility);
        facilitiesListView = findView(R.id.list_facilities);
        routeInfoView = findView(R.id.routeInfoView);
        scale = findView(R.id.scale);
        toolBar = findView(R.id.toolBar);
        layout_overlay = findView(R.id.layout_overlay);
        tvTitle = findView(R.id.tv_title);
        containerRetry = findView(R.id.container_retry);
        tvLocationMessage = findView(R.id.tv_locationMessage);
        layoutPoiMenu = findView(R.id.layout_poiMenu);
        layout_mapView = findView(R.id.layout_mapView);
        layoutBack = findView(R.id.layout_back);
        layout_search = findView(R.id.layout_search);

        layout_search.setVisibility(View.GONE);

        img_search = findView(R.id.img_search);

        layout_location = findView(R.id.layout_location);
        layout_zoom = findView(R.id.layout_zoom);

        tvFloor.setOnClickListener(this);
        compassView.setOnClickListener(this);
        mapLocation.setOnClickListener(this);
        layoutBack.setOnClickListener(this);
        layout_search.setOnClickListener(this);
        img_search.setOnClickListener(this);

        tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                usePDR = !usePDR;
                if (usePDR) {
                    showMessage("添加惯导");
                } else {
                    showMessage("取消惯导");
                }
                return true;
            }
        });

        Switch switch_test_location = findView(R.id.switch_test_location);
        switch_test_location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Config.isTestLocation = isChecked;
                startService(new Intent(self, LampSiteLocationService.class));
            }
        });
        initLayoutPoiMenu();
    }

    /**
     * 延迟刷新地图覆盖物
     */
    private void postRefreshOverlayer() {
        postRefreshOverlayer(50);
    }

    private void postRefreshOverlayer(long time) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mapView.getOverlayController().refresh();
            }
        }, time);
    }

    private void showPoiMark(long locationId) {
        presenter.refreshPoiMark(locationId);
        showPoiMenu(new PoiModel(getMapView().selectFeature(locationId)), PalmapViewState.Normal);
    }

    private void initLayoutPoiMenu() {
        layoutPoiMenu.setListener(new YanTaiPoiMenuLayout.Listener() {
            @Override
            public void onStartClick(View v) {
                presenter.startMark(layoutPoiMenu.getPoiModel());
            }

            @Override
            public void onEndClick(View v) {

            }

            @Override
            public boolean onGoClick(View v) {
                //去那里
                if (presenter.startMarkFromLocation()) {
                    presenter.endMark(layoutPoiMenu.getPoiModel());
                    return true;
                } else {
                    //showMessage("当前没有定位点");
                    presenter.setPalmapViewState(PalmapViewState.ENd_SET);
                    presenter.endMark(layoutPoiMenu.getPoiModel());
                    return false;
                }
            }

            @Override
            public void onClearClick() {
                presenter.resetState();
            }

            @Override
            public void onStartNavigateClick(View v) {
                if (presenter.getState() == PalmapViewState.Navigating) {
                    presenter.exitNavigate();
                } else {
                    presenter.beginNavigate();
                }
            }

        });
    }

    @Override
    public void readExitNavigate() {
        layoutPoiMenu.exitNavigate();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.map_location) {
            locationClick();
        } else if (i == R.id.layout_back) {
            backClick();
        } else if (i == R.id.compassView) {
            compassViewClick();
        } else if (i == R.id.tv_floor) {
            floorClick();
        } else if (i == R.id.img_search) {
            toYanTaiSearchView();
        }
    }

    private void toYanTaiSearchView() {
        presenter.resetState();
        presenter.clearFacilityMarks();
        clearFacilityListSelect();
        getNavigator().toSearchViewForResult(this, presenter.getBuildingId(), searchText, floorModelList, CODE_SEARCH_REQUEST);
    }

    private void changeFloor(int floorId) {
        if (floorId == presenter.getCurrentFloorId()) {
            return;
        }
        //TODO 准备切换楼层 先去除所有覆盖层
        mapView.removeAllOverlay();
        //TODO 自动手动切换了楼层 就取消自动切换楼层的支持了
        presenter.setCanAutoChangeFloor(false);
        presenter.changeFloor(floorId);
    }

    public void facilitiesClick(View v) {
        presenter.clearFacilityMarks();
        if (facilitiesListAdapter != null) {
            facilitiesListAdapter.setSelectPosition(-1);
        }
    }

    @Override
    public void showMessage(final String message) {
        showAlertMsg(message, null);
    }

    @Override
    public void showErrorMessage(String message) {
        showAlertMsg(message, null);
    }

    public void showAlertMsg(final String msg, final String subMsg) {
        toastDelegate.get().showMsg(msg);
    }

    @Override
    public List<ExFloorModel> floorModels() {
        return floorModelList;
    }

    @Override
    public String getFloorNameById(long floorId) {
        if (floorModelList == null || floorModelList.size() == 0) {
            return null;
        }
        for (ExFloorModel m : floorModelList) {
            if (m.getId() == floorId) return m.getName();
        }
        return null;
    }

    public void mapZoomInClick(View view) {
        if (mapView != null) {
            mapView.zoomIn();
            delayedDoCollisionDetection();
            refreshScaleView();
        }
    }

    public void mapZoomOutClick(View view) {
        if (mapView != null) {
            mapView.zoomOut();
            delayedDoCollisionDetection();
            refreshScaleView();
        }
    }
}