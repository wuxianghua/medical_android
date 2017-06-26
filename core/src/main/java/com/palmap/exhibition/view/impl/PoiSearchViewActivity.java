package com.palmap.exhibition.view.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.exhibition.model.ExFloorModel;
import com.palmap.exhibition.model.QuickSearchKeyWordModel;
import com.palmap.exhibition.model.SearchResultModel;
import com.palmap.exhibition.presenter.PoiSearchPresenter;
import com.palmap.exhibition.view.PoiSearchView;
import com.palmap.exhibition.view.adapter.SearchListAdapter;
import com.palmap.exhibition.view.base.ExSwipeBackActivity;
import com.palmap.library.utils.ActivityUtils;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.data.LocationModel;
import com.palmaplus.nagrand.data.LocationPagingList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class PoiSearchViewActivity extends ExSwipeBackActivity<PoiSearchPresenter> implements PoiSearchView {

    private static final String KEY_BUILDING = "key_building";
    private static final String KEY_ExFloorModels = "key_ExFloorModelList";
    private static final String KEY_SEARCH_TEXT = "key_searchText";

    @Inject
    PoiSearchPresenter presenter;

    EditText edit_search;
    Toolbar toolBar;
    ListView list_poiSearch;
    LinearLayout layoutSearchResult;
    ViewGroup layout_quickSearch;
    View img_clear;

    View empty_list_poiSearch;

    private String searchText;
    private long buildingId = 0;

    private ArrayList<ExFloorModel> floorModelArrayList;

    private long[] searchCategoryArr = null;

    private boolean isUseKeyWord = true;

    public static void navigatorThisForResult(Activity that, long buildingId, String searchTxt, ArrayList<ExFloorModel> floorModelArrayList, int requestCode) {
        Intent intent = new Intent(that, PoiSearchViewActivity.class);
        intent.putExtra(KEY_BUILDING, buildingId);
        intent.putExtra(KEY_ExFloorModels, floorModelArrayList);
        intent.putExtra(KEY_SEARCH_TEXT, searchTxt);
        that.startActivityForResult(intent, requestCode);
        that.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yantai_poi_search_view);
        initStatusBar(R.color.ngr_colorPrimary);
        try {
            buildingId = getIntent().getExtras().getLong(KEY_BUILDING);
            floorModelArrayList = getIntent().getExtras().getParcelableArrayList(KEY_ExFloorModels);
            searchText = getIntent().getExtras().getString(KEY_SEARCH_TEXT);
        } catch (Exception e) {
        }
        initData();
        initView();
        presenter.attachView(this, buildingId);
//        edit_search.setText(searchText);
//        edit_search.setSelection(edit_search.length());
    }

    /**
     * 获取场馆名
     *
     * @return
     */
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
        empty_list_poiSearch.setVisibility(View.VISIBLE);
        list_poiSearch.setVisibility(View.GONE);
        layout_quickSearch.setVisibility(View.GONE);
    }

    private void initData() {
    }

    private void initView() {
        bindView();

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtil.d("editStr:" + s.toString());

                if (list_poiSearch.getAdapter() != null) {
                    ((SearchListAdapter) list_poiSearch.getAdapter()).clear();
                }
                if (TextUtils.isEmpty(s.toString().trim())) {
                    layoutSearchResult.setVisibility(View.GONE);
                    layout_quickSearch.setVisibility(View.VISIBLE);
                    img_clear.setVisibility(View.INVISIBLE);
                } else {
                    img_clear.setVisibility(View.VISIBLE);
                    layoutSearchResult.setVisibility(View.VISIBLE);
                    //吊起
                    LogUtil.d("吊起查询");
                    if (isUseKeyWord) {
                        presenter.requestPoiData(s.toString(), searchCategoryArr);
                    } else {
                        presenter.requestPoiData(null, searchCategoryArr);
                    }
                    isUseKeyWord = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ItemClick searchClick = new ItemClick() {
            @Override
            public void handlerResult(SearchResultModel searchResultModel) {
                super.handlerResult(searchResultModel);
                presenter.savePoiSearchKeyWord(searchResultModel.getName());
            }
        };

        list_poiSearch.setOnItemClickListener(searchClick);
    }

    private void bindView() {
        edit_search = findView(R.id.edit_search);
        toolBar = findView(R.id.toolBar);
        img_clear = findView(R.id.img_clear);
        layout_quickSearch = findView(R.id.layout_quickSearch);
        list_poiSearch = findView(R.id.list_poiSearch);
//        list_poiSearch.setEmptyView(findView(R.id.empty_list_poiSearch));
        layoutSearchResult = findView(R.id.layout_search_result);

        edit_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    return true;
                }
                return false;
            }
        });

        empty_list_poiSearch = findViewById(R.id.empty_list_poiSearch);
    }

    @Override
    protected PoiSearchPresenter inject() {
        daggerInject().inject(this);
        return presenter;
    }

    @Override
    public void readPoiData(List<LocationModel> data) {
        LogUtil.d("readPoiData");
        layout_quickSearch.setVisibility(View.GONE);
        layoutSearchResult.setVisibility(View.VISIBLE);
        list_poiSearch.setVisibility(View.VISIBLE);
        empty_list_poiSearch.setVisibility(View.GONE);

        if (data != null && data.size() > 0) {
            SearchListAdapter poiSearchListAdapter = new SearchListAdapter(getContext(),null);
            poiSearchListAdapter.addPoiData(data);
            list_poiSearch.setAdapter(poiSearchListAdapter);
//            ViewUtils.setListViewBasedOnItem(list_poiSearch, 5, false);
//            list_poiSearch.setAdapter(poiSearchListAdapter);
        }
    }

    @Override
    public void readSearchMeetingData(Api_ActivityInfo info) {
    }

    @Override
    public void readQuickSearchData(List<QuickSearchKeyWordModel> data) {

    }

    @Override
    public void readHistorySearch(List<String> data) {
    }

    void backClick() {
        setResult(RESULT_OK, PalmapViewActivity.getPoiSearchResultIntent(-1, -1, edit_search.getText().toString(), null));
        finish();
    }

    public void clearClick(View v) {
        edit_search.setText(null);
        searchCategoryArr = null;
        hideAllTypeDataView();
        layout_quickSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        ActivityUtils.hideInputWindow(PoiSearchViewActivity.this);
    }

    /**
     * 隐藏所有类型数据视图
     */
    public void hideAllTypeDataView() {
        layoutSearchResult.setVisibility(View.GONE);
        layout_quickSearch.setVisibility(View.GONE);
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
    public String obtainFloorNameById(long floorId) {
        if (floorModelArrayList == null) return null;
        for (ExFloorModel floorModel : floorModelArrayList) {
            if (floorModel.getId() == floorId) {
                return floorModel.getName();
            }
        }
        return null;
    }

    public void cancelClick(View view) {
        onBackPressed();
    }

    public void quickSearchClick(View view) {

        String searchText = null;

        int i = view.getId();
        if (i == R.id.layout_diningRoom) {//主题餐厅
            searchText = "主题餐厅";
            searchCategoryArr = new long[]{
                    11452000,// 咖啡馆
                    11471000,// 甜品店
            };
        } else if (i == R.id.layout_clothingShoesBag) {//服装鞋包
            searchText = "服装鞋包";
            searchCategoryArr = new long[]{
                    13002000,//男装
                    13003000,//女装
                    13011000,//服装配件
                    13032000,//女鞋
                    13036000,//皮具
                    13010000,//皮草羊绒
                    13031000,//男鞋
            };
        } else if (i == R.id.layout_jewelryMakeup) {//珠宝化妆
            searchText = "珠宝化妆";
            searchCategoryArr = new long[]{
                    13071000,
                    13061000,// 化妆品
                    13062000,// 个人护理
                    13063000,// 香水
            };
        } else if (i == R.id.layout_outdoorSports) {//运动户外
            searchText = "运动户外";
            searchCategoryArr =  new long[]{
                    13037000,
                    13009000,
                    13008000,
                    13008003,
                    13008001,
                    13008002,
                    13037000,
            };
        } else if (i == R.id.layout_homeDigital) {//家电数码
            searchText = "家电数码";
            searchCategoryArr = new long[]{
                    13093000,//手机通讯及配件
            };
        } else if (i == R.id.layout_stationery) {//文具用品
            searchText = "文具用品";
            searchCategoryArr = new long[]{
                    13074000,//钟表
                    13075000,//眼镜
            };
        } else if (i == R.id.layout_recreation) {//娱乐休闲
            searchText = "娱乐休闲";
            searchCategoryArr = new long[]{
                    13076000
            };
        } else if (i == R.id.layout_cinema) {//CGV影院
            searchText = "CGV影院";
            searchCategoryArr = new long[]{
                    12001000
            };
        }
        isUseKeyWord = false;
        edit_search.setText(searchText);
        edit_search.setSelection(edit_search.length());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private class ItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ActivityUtils.hideInputWindow(PoiSearchViewActivity.this);
            if (parent.getAdapter() != null) {
                SearchListAdapter searchListAdapter = (SearchListAdapter) parent.getAdapter();
                SearchResultModel searchResultModel = searchListAdapter.getItem(position);
                setResult(RESULT_OK, PalmapViewActivity.getPoiSearchResultIntent(searchResultModel.getId(), searchResultModel.getFloorId(), edit_search.getText().toString(), searchResultModel.getLocationType()));
                handlerResult(searchResultModel);
                finish();
            }
        }

        public void handlerResult(SearchResultModel searchResultModel) {

        }
    }
}
