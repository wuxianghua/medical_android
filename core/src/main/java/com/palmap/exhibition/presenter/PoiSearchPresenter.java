package com.palmap.exhibition.presenter;

import com.palmap.exhibition.view.PoiSearchView;
import com.palmap.library.presenter.Presenter;

/**
 * Created by 天明 on 2016/6/28.
 */
public interface PoiSearchPresenter extends Presenter {

    void attachView(PoiSearchView view,long buildingId);

    void requestPoiData(String keyWord);

    void savePoiSearchKeyWord(String keyWord);

    void requestPoiData(String keyWord,long[] categories);

    void clearHistoryPoiKey();

    void requestExbtShopData();

    void requestMeetingData();

    void requestActivityData();

    void requestQuickSearchModel();
}