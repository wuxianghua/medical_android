package com.palmap.exhibition.presenter.impl;

import com.palmap.exhibition.BuildConfig;
import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.dao.HistoryPoiSearch;
import com.palmap.exhibition.dao.business.HistoryPoiSearchBusiness;
import com.palmap.exhibition.exception.NotFoundDataException;
import com.palmap.exhibition.presenter.PoiSearchPresenter;
import com.palmap.exhibition.view.PoiSearchView;
import com.palmap.library.rx.dataSource.RxDataSource;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.LocationModel;
import com.palmaplus.nagrand.data.LocationPagingList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by 天明 on 2016/6/28.
 */
public class PoiSearchPresenterImpl implements PoiSearchPresenter {
    @Inject
    public PoiSearchPresenterImpl() {
        dataSource = new DataSource(Config.MAP_SERVER_URL);
    }

    private PoiSearchView poiSearchView;

//    @Inject
    DataSource dataSource;

    private long buildingId;

    private HistoryPoiSearchBusiness historyPoiSearchBusiness;

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void attachView(PoiSearchView view, long buildingId) {
        this.poiSearchView = view;
        this.buildingId = buildingId;
        historyPoiSearchBusiness = new HistoryPoiSearchBusiness(poiSearchView.getContext());
        loadHistoryPoiData();
    }

    @Override
    public void requestPoiData(String keyWord) {
        requestPoiData(keyWord,null);
    }

    @Override
    public void requestPoiData(final String keyWord,long[] categories) {
        RxDataSource.search(dataSource, keyWord, 1, 2000, new long[]{
                Config.ID_FLOOR_F1,
                Config.ID_FLOOR_F2
        }, categories)
                .subscribe(new Action1<LocationPagingList>() {
                    @Override
                    public void call(LocationPagingList locationPagingList) {
                        if (locationPagingList.getSize() == 0) {
                            poiSearchView.requestPoiDataError(new NotFoundDataException());
                            return;
                        }
                        List<LocationModel> result = new ArrayList<>();
                        for (int i = 0; i < locationPagingList.getSize(); i++) {
                            result.add(locationPagingList.getPOI(i));
                        }
                        poiSearchView.readPoiData(result);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        poiSearchView.requestPoiDataError(throwable);
                        if (BuildConfig.DEBUG) {
                            throwable.printStackTrace();
                        }
                    }
                });
    }

    private void loadHistoryPoiData() {
        if (poiSearchView == null) return;
        //历史取最后10条数据
        historyPoiSearchBusiness.find_All().takeLast(10).subscribe(new Action1<List<HistoryPoiSearch>>() {
            @Override
            public void call(List<HistoryPoiSearch> historyPoiSearches) {
                poiSearchView.readHistorySearch(convertData(historyPoiSearches));
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private List<String> convertData(List<HistoryPoiSearch> data) {
        if (data == null || data.size() == 0) return null;
        List<String> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            result.add(data.get(i).getPoiKeyWord());
        }
        return result;
    }

    @Override
    public void savePoiSearchKeyWord(String keyWord) {
    }

    @Override
    public void clearHistoryPoiKey() {
    }

    @Override
    public void requestExbtShopData() {
    }

    @Override
    public void requestMeetingData() {
    }

    @Override
    public void requestActivityData() {
    }

}
