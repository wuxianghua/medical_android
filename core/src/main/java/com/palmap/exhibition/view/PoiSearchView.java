package com.palmap.exhibition.view;

import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.exhibition.model.QuickSearchKeyWordModel;
import com.palmap.library.view.LoadDataView;
import com.palmaplus.nagrand.data.LocationModel;
import com.palmaplus.nagrand.data.LocationPagingList;

import java.util.List;

/**
 * Created by 天明 on 2016/6/28.
 */
public interface PoiSearchView extends LoadDataView {

    void readPoiData(List<LocationModel> data);

    void readHistorySearch(List<String> data);

    void hideAllTypeDataView();

    void readExbtShopData(LocationPagingList data);

    void readAllMeetingData(Api_ActivityInfo info);

    void readActivityData(Api_ActivityInfo info);

    void readSearchMeetingData(Api_ActivityInfo info);

    void readQuickSearchData(List<QuickSearchKeyWordModel> data);

    /**
     * 获取楼层名字
     * @param floorId
     */
    String obtainFloorNameById(long floorId);

    /**
     * 获取场馆名
     * @param floorId
     */
    String obtainVenueName(long floorId);

    void requestPoiDataError(Throwable throwable);
}
