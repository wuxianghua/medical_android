package com.palmap.exhibition.view.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.exhibition.model.ExFloorModel;
import com.palmap.exhibition.model.QuickSearchKeyWordModel;
import com.palmap.exhibition.presenter.PoiSearchPresenter;
import com.palmap.exhibition.view.PoiSearchView;
import com.palmap.exhibition.view.base.ExActivity;
import com.palmaplus.nagrand.data.LocationModel;
import com.palmaplus.nagrand.data.LocationPagingList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by jinhuang.yang on 2017/6/23.
 * E-mail:jinhuang.yang@palmaplus.com
 */

public class DestinationSearchActivity extends ExActivity<PoiSearchPresenter> implements PoiSearchView{


    private static final String KEY_BUILDING = "key_building";
    private static final String KEY_ExFloorModels = "key_ExFloorModelList";

    private long buildingId = 0;
    private ArrayList<ExFloorModel> floorModelArrayList;

    public static void navigatorThisForResult(Activity that, long buildingId, ArrayList<ExFloorModel> floorModelArrayList, int requestCode) {
        Intent intent = new Intent(that, DestinationSearchActivity.class);
        intent.putExtra(KEY_BUILDING, buildingId);
        intent.putExtra(KEY_ExFloorModels, floorModelArrayList);
        that.startActivityForResult(intent, requestCode);
        that.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Inject
    PoiSearchPresenter presenter;

    @Override
    protected PoiSearchPresenter inject() {
        daggerInject().inject(this);
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            buildingId = getIntent().getExtras().getLong(KEY_BUILDING);
            floorModelArrayList = getIntent().getExtras().getParcelableArrayList(KEY_ExFloorModels);
        } catch (Exception e) {
        }
        initView();
        presenter.attachView(this, buildingId);
        presenter.requestQuickSearchModel();
    }

    private void initView() {
        //

        //发起查询
        //presenter.requestPoiData("关键字", null);
    }

    @Override
    public void readPoiData(List<LocationModel> data) {

    }

    @Override
    public void readHistorySearch(List<String> data) {

    }

    @Override
    public String obtainFloorNameById(long floorId) {
        return null;
    }

    @Override
    public String obtainVenueName(long floorId) {
        if (floorModelArrayList != null) {
            for (int i = 0; i < floorModelArrayList.size(); i++) {
                if (floorModelArrayList.get(i).getId() - floorId == 0) {
                    return floorModelArrayList.get(i).getName();
                }
            }
        }
        return "";
    }

    @Override
    public void requestPoiDataError(Throwable throwable) {

    }

    /**
     * 显示快捷搜索的UI
     */
    @Override
    public void readQuickSearchData(List<QuickSearchKeyWordModel> data) {

    }

    ///////////////////////////下面的没用//////////////////////////////////////////

    @Override
    public void hideAllTypeDataView() {

    }

    @Override
    public void readExbtShopData(LocationPagingList data) {

    }

    @Override
    public void readAllMeetingData(Api_ActivityInfo info) {

    }

    @Override
    public void readActivityData(Api_ActivityInfo info) {

    }

    @Override
    public void readSearchMeetingData(Api_ActivityInfo info) {

    }
}
