package com.palmap.exhibition.other;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.View;

import com.palmap.exhibition.model.Api_PositionInfo;
import com.palmap.exhibition.model.NavigationPointModel;
import com.palmap.exhibition.widget.overlayer.EndMark;
import com.palmap.exhibition.widget.overlayer.ExFloorMark;
import com.palmap.exhibition.widget.overlayer.LadderMark;
import com.palmap.exhibition.widget.overlayer.LightEventMark;
import com.palmap.exhibition.widget.overlayer.LocationMark;
import com.palmap.exhibition.widget.overlayer.PoiMark;
import com.palmap.exhibition.widget.overlayer.PubFacilityMark;
import com.palmap.exhibition.widget.overlayer.StartMark;
import com.palmap.exhibition.widget.overlayer.TapMark;
import com.palmap.library.utils.LogUtil;
import com.palmap.library.utils.MapUtils;
import com.palmaplus.nagrand.core.Types;
import com.palmaplus.nagrand.data.Feature;
import com.palmaplus.nagrand.geos.Coordinate;
import com.palmaplus.nagrand.position.Location;
import com.palmaplus.nagrand.view.MapView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by 王天明 on 2016/6/20.
 * <p/>
 * 叠加层管理器
 */
public class OverLayerManager {

    private LocationMark locationMark;

    private StartMark startMark;

    private EndMark endMark;

    private MapView mapView;

    private TapMark tapMark;

    private PoiMark poiMark;

    private Handler mainHandler;

    private LadderMark ladderMark;

    private class AnimRefreshOverlayTask implements Runnable {
        private long max;
        private int animRefreshOverLayerIndex = 0;

        private AnimRefreshOverlayTask(long maxTime) {
            this.max = maxTime / 15 +1;
        }
        @Override
        public void run() {
            mapView.getOverlayController().refresh();
            mainHandler.postDelayed(this, 15);
            animRefreshOverLayerIndex++;
            if (animRefreshOverLayerIndex > max) {
                mainHandler.removeCallbacks(this);
                animRefreshOverLayerIndex = 0;
                mapView.doCollisionDetection();
            }
        }
    }

    private AnimRefreshOverlayTask animRefreshOverlayTask = null;

    public void animRefreshOverlay(final long time) {
        if (mainHandler == null) mainHandler = new Handler(Looper.getMainLooper());
        if (animRefreshOverlayTask != null) {
            mainHandler.removeCallbacks(animRefreshOverlayTask);
        }
        animRefreshOverlayTask = new AnimRefreshOverlayTask(time);
        mainHandler.post(animRefreshOverlayTask);
    }

    public void refreshOverlayDelayed(long time) {
        if (mainHandler == null) mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mapView.getOverlayController().refresh();
            }
        }, time);
    }


    /**
     * 点亮活动
     * key:活动id
     * val: LightEventMark
     */
    private SparseArray<ArrayList<LightEventMark>> lightEventMarks;
    /**
     * 不显示的活动列表id
     */
    private LinkedHashSet<Integer> cannotShowActivityIds = new LinkedHashSet<>();
    /**
     * 当前正在显示的 LightEventMarks 当前楼层
     */
    private ArrayList<LightEventMark> currentLightEventMarks = new ArrayList<>();
    /**
     * 是否可以显示点亮活动的mark
     */
    private boolean canShowLightEventMarks = true;

    /**
     * 公共设施显示的mark
     */
    private List<PubFacilityMark> facilityMarkList;

    private Listener listener;

    public void registerListener(Listener listener) {
        this.listener = listener;
    }

    public OverLayerManager(MapView mapView) {
        this.mapView = mapView;
        locationMark = new LocationMark(mapView.getContext(), mapView);
    }

    /**
     * 刷新定位点
     *
     * @param location
     */
    public void refreshLocation(Location location) {
        refreshLocation(location.getPoint().getCoordinate());
    }

    public void refreshLocation(Coordinate coordinate) {
        double x = coordinate.getX();
        double y = coordinate.getY();
        locationMark.init(new double[]{x, y});
        mapView.addOverlay(locationMark);
        mapView.getOverlayController().refresh();
    }

    /**
     * 动画移动定位点
     *
     * @param location
     */
    public void animLocation(Location location, long floorId) {
        animLocation(location.getPoint().getCoordinate(), floorId);
    }

    public void animLocation(Coordinate coordinate, long floorId) {
        double x = coordinate.getX();
        double y = coordinate.getY();
        locationMark.init(new double[]{x, y});
        locationMark.setFloorId(floorId);
        Types.Point point = mapView.converToScreenCoordinate(x, y);
        locationMark.animTo(point.x, point.y);
        mapView.addOverlay(locationMark);
    }

    public void hideLocation() {
        if (locationMark != null) {
            mapView.removeOverlay(locationMark);
            mapView.getOverlayController().refresh();
        }
    }


    public void hideStartAndEnd() {
        if (startMark != null) {
            mapView.removeOverlay(startMark);
        }

        if (endMark != null) {
            mapView.removeOverlay(endMark);
        }
    }

    public void rotateLocation(float angle) {
        if (locationMark != null) {
            locationMark.setRotation(angle);
        }
    }

    public void rotateLocationAnim(float angle) {
        if (locationMark != null) {
            locationMark.rotationAnim(angle);
        }
    }

    public void addStartMark(NavigationPointModel navigationPointModel, double x, double y, boolean isShow) {
        addStartMark(navigationPointModel, x, y, true, isShow);
    }

    public void addStartMark(NavigationPointModel navigationPointModel, double x, double y) {
        addStartMark(navigationPointModel, x, y, true, true);
    }

    public void addStartMark(NavigationPointModel navigationPointModel, double x, double y, boolean canListener, boolean isShow) {
        removeTapMark();
        if (startMark != null) {
            startMark.setNavigationPointModel(navigationPointModel);
        } else {
            startMark = new StartMark(mapView, navigationPointModel);
        }
        startMark.init(new double[]{x, y});
        //起始点相同
        if (endMark != null && endMark.getFloorId() == startMark.getFloorId() && endMark.getGeoCoordinate()[0] == startMark.getGeoCoordinate()[0] && endMark.getGeoCoordinate()[1] == startMark.getGeoCoordinate()[1]) {
            mapView.removeOverlay(endMark);
            endMark = null;
        }
        if (isShow) {
            mapView.addOverlay(startMark);
            mapView.getOverlayController().refresh();
        }
        if (listener != null && canListener) {
            if (endMark != null) {
                listener.onStartEndAddedListener(mapView, startMark, endMark);
            } else {
                listener.onStartAddedListener(mapView, startMark);
            }
        }
    }

    public void addEndMark(NavigationPointModel navigationPointModel, double x, double y) {
        addEndMark(navigationPointModel, x, y, true);
    }

    public void addEndMark(NavigationPointModel navigationPointModel, double x, double y, boolean canListener) {
        removeTapMark();
        if (endMark != null) {
            endMark.setNavigationPointModel(navigationPointModel);
        } else {
            endMark = new EndMark(mapView, navigationPointModel);
        }
        endMark.init(new double[]{x, y});
        //起始点相同
        if (startMark != null && endMark.getFloorId() == startMark.getFloorId() && endMark.getGeoCoordinate()[0] == startMark.getGeoCoordinate()[0] && endMark.getGeoCoordinate()[1] == startMark.getGeoCoordinate()[1]) {
            mapView.removeOverlay(startMark);
            startMark = null;
        }
        mapView.addOverlay(endMark);
        mapView.getOverlayController().refresh();
        if (listener != null && canListener) {
            if (startMark != null) {
                listener.onStartEndAddedListener(mapView, startMark, endMark);
            } else {
                listener.onEndAddedListener(mapView, endMark);
            }
        }
    }

    public void addFacilityMarks(List<Feature> features, long floorId) {
        if (facilityMarkList == null) {
            facilityMarkList = new ArrayList<>();
        }
        for (Feature feature : features) {
            PubFacilityMark mark = new PubFacilityMark(mapView.getContext(), floorId);
            mark.init(new double[]{feature.getCentroid().getX(), feature.getCentroid().getY()});
            mapView.addOverlay(mark);
            facilityMarkList.add(mark);
        }
    }

    public void addFacilityMarks(List<Feature> features, long floorId, View.OnClickListener clickListener) {
        if (facilityMarkList == null) {
            facilityMarkList = new ArrayList<>();
        }
        for (Feature feature : features) {
            PubFacilityMark mark = new PubFacilityMark(mapView.getContext(), floorId);
            mark.init(new double[]{feature.getCentroid().getX(), feature.getCentroid().getY()});
            mark.setOnClickListener(clickListener);
            mapView.addOverlay(mark);
            facilityMarkList.add(mark);
        }
    }

    public void refreshTapMark(long floorId, double screenX, double screenY) {
        removePoiMark();
        removeTapMark();
        tapMark = new TapMark(mapView, floorId);
        tapMark.init(screenX, screenY);
        mapView.addOverlay(tapMark);
    }

    public void refreshTapMark(long floorId, double[] wordXY) {
        removePoiMark();
        removeTapMark();
        tapMark = new TapMark(mapView, floorId);
        tapMark.init(wordXY);
        mapView.addOverlay(tapMark);
    }

//    public void refreshTapMark(double[] wordXY) {
//        removePoiMark();
//        removeTapMark();
//        tapMark = new TapMark(mapView);
//        tapMark.init(wordXY);
//        mapView.addOverlay(tapMark);
//    }

    public void refreshLadderMark(long floorId, double[] wordXY, long categoryId, String msg) {
        removeLadderMark();
        ladderMark = new LadderMark(mapView, floorId, categoryId, msg);
        ladderMark.init(wordXY);
        mapView.addOverlay(ladderMark);
    }

    public void removeLadderMark() {
        if (ladderMark != null) {
            mapView.removeOverlay(ladderMark);
            ladderMark = null;
        }
    }


    public void removeTapMark() {
        if (tapMark != null) {
            mapView.removeOverlay(tapMark);
            tapMark = null;
        }
    }

    /**
     * 清除除了定位点的mark
     */
    public void clearAllNotLocation() {
        removeLadderMark();
        removeTapMark();
        removePoiMark();
        mapView.removeOverlay(startMark);
        mapView.removeOverlay(endMark);
        startMark = null;
        endMark = null;
    }


    public void clearAll() {
        clearAllNotLocation();
        hideLocation();
    }

    /**
     * 测量起始点距离(米)
     * 如果无起始点 返回-1
     *
     * @return
     */
    public double measureStateEndDistance() {
        if (startMark == null || endMark == null) {
            return -1;
        }
        return MapUtils.pointDistance(startMark.getWorldX(), startMark.getWorldY(),
                endMark.getWorldX(), endMark.getWorldY());
    }

    public void addPoiMark(long floorId, long locationId) {
        removePoiMark();
        poiMark = new PoiMark(mapView, floorId, locationId);
        Feature feature = mapView.selectFeature(poiMark.getLocationId());
        if (feature != null) {
            Coordinate coordinate = feature.getCentroid();
            poiMark.init(new double[]{coordinate.getX(), coordinate.getY()});
            mapView.addOverlay(poiMark);
        }
        mapView.getOverlayController().refresh();
    }

    public void clearFacilityMarks() {
        if (facilityMarkList != null) {
            for (int i = 0; i < facilityMarkList.size(); i++) {
                mapView.removeOverlay(facilityMarkList.get(i));
            }
            facilityMarkList.clear();
        }
    }

    public interface Listener {
        /**
         * 当起点终点设置完成监听
         */
        void onStartEndAddedListener(MapView mapView, StartMark startMark, EndMark endMark);

        /**
         * 起点设置完成监听
         */
        void onStartAddedListener(MapView mapView, StartMark startMark);

        /**
         * 终点设置完成监听
         */
        void onEndAddedListener(MapView mapView, EndMark endMark);
    }


    private void checkMarkIsShow(long newFloorId, ExFloorMark... marks) {
        for (ExFloorMark exFloorMark : marks) {
            if (exFloorMark != null) {
                if (newFloorId == exFloorMark.getFloorId()) {
                    mapView.addOverlay(exFloorMark);
                } else {
                    mapView.removeOverlay(exFloorMark);
                }
            }
        }
    }

    public void onChangePlanarGraph(long oldFloorId, long newFloorId) {
//        checkMarkIsShow(newFloorId,locationMark,startMark,endMark);
        if (oldFloorId == newFloorId) return;
        currentLightEventMarks.clear();
        checkMarkIsShow(newFloorId, startMark, endMark);
//        if (poiMark != null) {
//            if (newFloorId == poiMark.getFloorId()) {
//                Feature feature = mapView.selectFeature(poiMark.getLocationId());
//                if (feature != null) {
//                    Coordinate coordinate = feature.getCentroid();
//                    poiMark.init(new double[]{coordinate.getX(), coordinate.getY()});
//                    mapView.addOverlay(poiMark);
//                }
//            } else {
//                mapView.removeOverlay(poiMark);
//            }
//        }

        checkLightEventMarkShow(oldFloorId, newFloorId);

        removePoiMark();
        clearFacilityMarks();
        mapView.getOverlayController().refresh();
    }

    /**
     * 显示当前楼层的点亮活动mark
     *
     * @param oldFloorId
     * @param newFloorId
     */
    private void checkLightEventMarkShow(long oldFloorId, long newFloorId) {
        if (lightEventMarks == null || lightEventMarks.size() == 0) return;
        for (int i = 0; i < lightEventMarks.size(); i++) {
            int atyId = lightEventMarks.keyAt(i);
            ArrayList<LightEventMark> marks = lightEventMarks.get(atyId);
            for (LightEventMark tempMark : marks) {
                if (tempMark.getFloorId() != newFloorId) {
                    continue;
                }
                LogUtil.e("显示点亮活动mark:" + tempMark.getAtyId());
                addLightEventMark(tempMark);
            }
        }
    }

    private void addLightEventMark(LightEventMark tempMark) {
        if (canShowLightEventMarks) {
            mapView.addOverlay(tempMark);
        }
        currentLightEventMarks.add(tempMark);
    }

    public void toggleLightEventMarkShow(boolean canShow) {
        if (canShow == canShowLightEventMarks) return;
        canShowLightEventMarks = canShow;
        if (currentLightEventMarks.size() > 0) {
            for (LightEventMark m : currentLightEventMarks) {
                if (canShowLightEventMarks) {
                    mapView.addOverlay(m);
                } else {
                    mapView.removeOverlay(m);
                }
            }
        }
    }

    public void removeLightEventMarkWithAtyId(int atyId) {
        Iterator<LightEventMark> it = currentLightEventMarks.iterator();
        while (it.hasNext()) {
            LightEventMark mark = it.next();
            if (mark.getAtyId() == atyId) {
                mapView.removeOverlay(mark);
                it.remove();
            }
        }
        lightEventMarks.remove(atyId);
    }

    /**
     * 添加点亮活动的图标
     *
     * @param currentFloorId
     */
    public void addLightEventMarks(long currentFloorId, Api_PositionInfo api_positionInfo) {
        addLightEventMarks(currentFloorId, api_positionInfo, null);
    }

    /**
     * 添加点亮活动的图标
     *
     * @param currentFloorId
     */
    public void addLightEventMarks(long currentFloorId, Api_PositionInfo api_positionInfo, View.OnClickListener onClickListener) {
        if (lightEventMarks == null) {
            lightEventMarks = new SparseArray<>();
        }

        ArrayList<LightEventMark> markList = new ArrayList<>();
        int atyId = 0;
        for (int i = 0; i < api_positionInfo.getObj().size(); i++) {
            Api_PositionInfo.ObjBean objBean = api_positionInfo.getObj().get(i);
            long floorId = objBean.getFloorId();
            double x = objBean.getLatitude();
            double y = objBean.getLongitude();

            atyId = objBean.getActivityId();

            LightEventMark tempMark = new LightEventMark(mapView, floorId);

            if (objBean.getLogoResId() != 0) {
                tempMark.setLogoResId(objBean.getLogoResId());
            } else {
                tempMark.setImgUrl(objBean.getLogo());
            }

            tempMark.init(new double[]{x, y});
            tempMark.setAtyId(atyId);
            tempMark.setPointId(objBean.getId());
            tempMark.setOnClickListener(onClickListener);
            markList.add(tempMark);
            if (floorId == currentFloorId) {
                addLightEventMark(tempMark);
            }
        }
        lightEventMarks.append(atyId, markList);
    }

    /**
     * 从一个poisitionID查找一个 点亮活动 mark
     *
     * @param positionId
     * @return
     */
    private LightEventMark findLightEventMark(int positionId) {
        /*if (lightEventMarks == null || lightEventMarks.size() == 0) {
            return null;
        }
        for (int i = 0; i < lightEventMarks.size(); i++) {
            int atyId = lightEventMarks.keyAt(i);
            ArrayList<LightEventMark> marks = lightEventMarks.get(atyId);
            for (LightEventMark tempMark : marks) {
                if (tempMark.getPointId() == positionId) {
                    return tempMark;
                }
            }
        }
        return null;*/
        // TODO: 2016/10/24 优化  从当前楼层显示的查找
        for (LightEventMark tempMark : currentLightEventMarks) {
            if (tempMark.getPointId() == positionId) {
                return tempMark;
            }
        }
        return null;
    }

    public void selectLightEventMark(int positionId) {
        LightEventMark mark = findLightEventMark(positionId);
        if (mark != null) {
            mark.setSelected(true);
        } else {
            for (LightEventMark m : currentLightEventMarks) {
                m.setSelected(false);
            }
        }
    }

    public void removeAllLightEventMark() {
        Iterator<LightEventMark> it = currentLightEventMarks.iterator();
        while (it.hasNext()) {
            LightEventMark mark = it.next();
            mapView.removeOverlay(mark);
            it.remove();
        }
        if (lightEventMarks != null) {
            lightEventMarks.clear();
        }
    }

//    private void toggleLightEventMarkBuAtyId(int atyId, boolean show, long currentFloorId) {
//        //如果已经不显示了
//        if (!show && cannotShowActivityIds.contains(atyId)) {
//            return;
//        }
//        cannotShowActivityIds.remove(atyId);
//        refreshCurrentFloorLightMark(currentFloorId);
//    }
//
//    private void refreshCurrentFloorLightMark(long currentFloorId) {
//
//    }

    public void removePoiMark() {
        if (poiMark != null) {
            mapView.removeOverlay(poiMark);
            poiMark = null;
        }
    }

    public LocationMark getLocationMark() {
        return locationMark;
    }

    public StartMark getStartMark() {
        return startMark;
    }

    public EndMark getEndMark() {
        return endMark;
    }

    public TapMark getTapMark() {
        return tapMark;
    }

    public PoiMark getPoiMark() {
        return poiMark;
    }

    public boolean isHaveStartEnd() {
        return !(startMark == null || endMark == null);
    }
}
