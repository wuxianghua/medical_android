package com.palmap.exhibition.presenter.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.palmap.exhibition.BuildConfig;
import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.dao.business.HistoryPoiSearchBusiness;
import com.palmap.exhibition.exception.NotFoundDataException;
import com.palmap.exhibition.model.QuickSearchKeyWordModel;
import com.palmap.exhibition.presenter.PoiSearchPresenter;
import com.palmap.exhibition.view.PoiSearchView;
import com.palmap.library.rx.dataSource.RxDataSource;
import com.palmap.library.utils.FileUtils;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.LocationModel;
import com.palmaplus.nagrand.data.LocationPagingList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 天明 on 2016/6/28.
 */
public class PoiSearchPresenterImpl implements PoiSearchPresenter {

    private static final String quickSearchPanelPath = "json/QuickSearchPanelConfig.json";
    private static final String quickSearchGroupPath = "json/QuickSearchGroupConfig.json";

    private PoiSearchView poiSearchView;
    //    @Inject
    DataSource dataSource;
    private long buildingId;
    private HistoryPoiSearchBusiness historyPoiSearchBusiness;
    private static final Gson gson = new Gson();

    @Inject
    public PoiSearchPresenterImpl() {
        dataSource = new DataSource(Config.MAP_SERVER_URL);
    }

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
    }

    @Override
    public void requestPoiData(String keyWord) {
        requestPoiData(keyWord, null);
    }

    @Override
    public void requestPoiData(final String keyWord, long[] categories) {
        RxDataSource.search(dataSource, keyWord, 1, 2000, new long[]{
                buildingId
        }, categories)
                .subscribe(new Consumer<LocationPagingList>() {
                    @Override
                    public void accept(LocationPagingList locationPagingList) {
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
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        poiSearchView.requestPoiDataError(throwable);
                        if (BuildConfig.DEBUG) {
                            throwable.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void savePoiSearchKeyWord(String keyWord) {
        historyPoiSearchBusiness.insert(keyWord);
    }

    @Override
    public void clearHistoryPoiKey() {
        historyPoiSearchBusiness.deleteAll();
    }

    @Override
    public boolean deleteHistoryPoiKey(String keyWord) {
        if (keyWord == null || keyWord.isEmpty()) {
            return false;
        }
        return historyPoiSearchBusiness.delete(keyWord);
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

    //ui层请求快捷搜索的数据
    @Override
    public void requestQuickSearchModel() {
        readPanelConfig();
        readGroupConfig();
    }

    private void readPanelConfig() {
       /* Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                String jsonString = FileUtils.readFileFromAssets(
                        poiSearchView.getContext(), "json/QuickSearchPanelConfig.json");
                emitter.onNext(jsonString);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).map(new Function<String, List<QuickSearchKeyWordModel>>() {
            @Override
            public List<QuickSearchKeyWordModel> apply(@NonNull String s) throws Exception {
                Type listType = new TypeToken<ArrayList<QuickSearchKeyWordModel>>() {
                }.getType();
                return gson.fromJson(s, listType);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Consumer<List<QuickSearchKeyWordModel>>() {
                    @Override
                    public void accept(@NonNull List<QuickSearchKeyWordModel> keyWordModels)
                            throws Exception {
                        poiSearchView.readQuickSearchData(0, keyWordModels);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        poiSearchView.readQuickSearchData(0, null);
                    }
                });*/
        try {
            String jsonString = FileUtils.readFileFromAssets(
                    poiSearchView.getContext(), quickSearchPanelPath);
            Type listType = new TypeToken<ArrayList<QuickSearchKeyWordModel>>() {
            }.getType();
            List<QuickSearchKeyWordModel> data =  gson.fromJson(jsonString, listType);
            poiSearchView.readQuickSearchData(0, data);
        } catch (Exception e) {
            poiSearchView.readQuickSearchData(0, null);
        }
    }

    private void readGroupConfig() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                String jsonString = FileUtils.readFileFromAssets(
                        poiSearchView.getContext(), quickSearchGroupPath);
                emitter.onNext(jsonString);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).map(new Function<String, List<QuickSearchKeyWordModel>>() {
            @Override
            public List<QuickSearchKeyWordModel> apply(@NonNull String s) throws Exception {
                Type listType = new TypeToken<ArrayList<QuickSearchKeyWordModel>>() {
                }.getType();
                return gson.fromJson(s, listType);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Consumer<List<QuickSearchKeyWordModel>>() {
                    @Override
                    public void accept(@NonNull List<QuickSearchKeyWordModel> keyWordModels)
                            throws Exception {
                        poiSearchView.readQuickSearchData(1, keyWordModels);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        poiSearchView.readQuickSearchData(1, null);
                    }
                });
    }

    @Override
    public void requestHistoryPOIData() {
        if (poiSearchView == null) {
            return;
        }
        historyPoiSearchBusiness.find_All().subscribe(
                new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> historyPoiSearches) {
                        poiSearchView.readHistorySearch(historyPoiSearches);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

}
