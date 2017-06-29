package com.palmap.exhibition.view;

import android.content.DialogInterface;

import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.exhibition.model.Api_PositionInfo;
import com.palmap.exhibition.model.FacilityModel;
import com.palmap.exhibition.model.PoiModel;
import com.palmap.exhibition.model.ServiceFacilityModel;
import com.palmap.exhibition.view.impl.PalmapViewState;
import com.palmap.library.view.LoadDataView;
import com.palmaplus.nagrand.data.Feature;
import com.palmaplus.nagrand.data.FeatureCollection;
import com.palmaplus.nagrand.data.LocationList;
import com.palmaplus.nagrand.data.PlanarGraph;
import com.palmaplus.nagrand.position.PositioningManager;
import com.palmaplus.nagrand.view.MapView;

import java.util.List;

/**
 * Created by 王天明 on 2016/6/3.
 */
public interface PalMapView extends LoadDataView {

    MapView getMapView();


    void showAlertMsg(String title, String msg);

    void moveToLocationPoint();

    void refreshCompassView(long time);

    /**
     * 读取标题
     *
     * @param title
     */
    void readTitle(String title);

    /**
     * 读取地图渲染数据
     *
     * @param planarGraph
     */
    void readMapData(PlanarGraph planarGraph);

    /**
     * 读取楼层列表数据
     *
     * @param locationList
     * @param selectFloorId
     */
    void readFloorData(LocationList locationList, long selectFloorId);

//    void readArenaData(List<ArenaModel> arenaModelList);

    /**
     * 获取当前显示的楼层名字
     * @return
     */
    String getCurrentFloorName();

    /**
     * 改变楼层回调
     *
     * @param oldFloorId
     * @param newFloorId
     */
    void onFloorChanged(long oldFloorId, long newFloorId);

    /**
     * 是否显示返回户外的ui
     *
     * @param isShow
     */
    void visibilityOutSideView(boolean isShow);

    /**
     * 读取公共设施
     *
     * @param data
     */
    void readPubFacility(List<FacilityModel> data);

    /**
     * 读取服务设置
     *
     * @param data
     */
    void readServiceFacility(List<ServiceFacilityModel> data);

    /**
     * 重置指北针
     */
    void resetCompass();

    /**
     * 刷新比例尺
     */
    void refreshScaleView();

    /**
     * 重置Feature颜色
     */
    void resetFeatureStyle(Feature feature);

    void readFeatureColor(Feature feature, int color);

    /**
     * 显示poi的额外信息视图
     */
    void showPoiMenu(PoiModel poiModel, PalmapViewState state);

    void hidePoiMenu();

    void hidePoiMenuAtFloorChange();

    void hidePoiMenuTop();

    /**
     * 显示定位效果相关信息
     *
     * @param title
     * @param msg
     */
    void showLocationMsgView(String title, String msg);

    /**
     * 获取window的旋转方向
     *
     * @return
     */
    int getWindowRotation();

    /**
     * 读取导航线
     */
    void readNavigateRoad(long[] floorIds,FeatureCollection featureCollection);

    /**
     * 清楚导航线
     */
    void clearNavigateRoad();

    /**
     * 显示定位状态UI
     *
     * @param status
     */
    void showLocationStatus(PositioningManager.LocationStatus status);

    /**
     * 显示定位错误消息
     *
     * @param throwable
     */
    void showLocationError(Throwable throwable);

    /**
     * 是否处于重新加载中
     *
     * @return
     */
    boolean isRetry();

    /**
     * 显示重新加载对话框形式
     *
     * @param throwable
     * @param runnable
     */
    void showRetryDialog(Throwable throwable, Runnable runnable);

    /**
     * 检查floorId在不在当前建筑中
     *
     * @return
     */
    boolean checkFloorIdOnCurrentBuilding(long floorId);

    void resetFailityMenu();

    /**
     * 控制mapView相关的控件是否显示，导航时不能控制地图相关操作
     */
    void hideMapViewControl();

    void showMapViewControl();

    /**
     * 显示路线描述信息视图
     */
    void showRouteInfoStart(String lable,String msg);
    void showRouteInfoEnd(String lable,String msg);
    void showRouteInfoDetails(String msg);

    /**
     * 隐藏路线描述信息视图
     */
    void hideRouteInfoView();

    /**
     * 控制路线指示上下行提示view
     */
    void showRouteUpView();
    void showRouteDownView();
    void hideRouteArrowView();
    /**
     * 显示路线长度
     * @param msg
     */
    void showRouteLength(String msg);

    /**
     * 显示推送视图
     * @param model
     */
//    void showPushView(PushModel pushModel);
    void showPushView(Api_PositionInfo.ObjBean model);

    /**
     * 显示点亮活动ui
     * @param api_activityInfo
     */
    void readLightEventActivityList(Api_ActivityInfo api_activityInfo);

    void showShareTaskView(Api_ActivityInfo.ObjBean objBean);

    String getFloorNameById(long floorId);

    /**
     * 清除设施选中状态
     */
    void clearFacilityListSelect();

    void showNavigationEndView(DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener1);

    /**
     * 是否使用prd
     * @return
     */
    boolean canUsePDR();

    void readRemainingLength(String mDynamicNaviExplain, float mRemainingLength);

    void readExitNavigate();

    /**
     * 导航完成了
     */
    void readNaviComplete();
}