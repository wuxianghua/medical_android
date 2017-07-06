package com.palmap.exhibition.view.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.palmap.exhibition.R;
import com.palmap.exhibition.exception.NotFoundDataException;
import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.exhibition.model.ExFloorModel;
import com.palmap.exhibition.model.QuickSearchKeyWordModel;
import com.palmap.exhibition.model.SearchResultModel;
import com.palmap.exhibition.presenter.PoiSearchPresenter;
import com.palmap.exhibition.view.PoiSearchView;
import com.palmap.exhibition.view.base.ExActivity;
import com.palmap.exhibition.view.fragment.ManualSearchFragment;
import com.palmap.exhibition.view.fragment.QuickSearchFragment;
import com.palmap.library.utils.ActivityUtils;
import com.palmaplus.nagrand.data.LocationModel;
import com.palmaplus.nagrand.data.LocationPagingList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by jinhuang.yang on 2017/6/23.
 * E-mail:jinhuang.yang@palmaplus.com
 */

public class DestinationSearchActivity extends ExActivity<PoiSearchPresenter> implements PoiSearchView {

    private final String TAG = DestinationSearchActivity.class.getSimpleName();
    private final String QUICK_FRAGMENT = "Quick";
    private final String MANUAL_FRAGMENT = "Manual";
    private static final String KEY_BUILDING = "key_building";
    private static final String KEY_ExFloorModels = "key_ExFloorModelList";

    private long buildingId = 0;
    private ArrayList<ExFloorModel> floorModelArrayList;
    private QuickSearchFragment mQuickSearchFragment = null;
    private ManualSearchFragment mManualSearchFragment = null;
    private boolean mIsFirstManual = true;
    private boolean mIsShowNoResultTip = false;

    private EditText mEdtTxtSearch = null;

    public static void navigatorThisForResult(
            Activity that, long buildingId,
            ArrayList<ExFloorModel> floorModelArrayList, int requestCode) {
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
        setContentView(R.layout.activity_destination_search);
        try {
            buildingId = getIntent().getExtras().getLong(KEY_BUILDING);
            floorModelArrayList = getIntent().getExtras().getParcelableArrayList(KEY_ExFloorModels);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        initView();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenter.attachView(this, buildingId);
        presenter.requestQuickSearchModel();
    }

    private void initView() {
        if (mQuickSearchFragment == null) {
            mQuickSearchFragment = new QuickSearchFragment();
            mQuickSearchFragment.setOnAcceptSearchKeyListener(
                    new QuickSearchFragment.OnAcceptSearchKeyListener() {
                        @Override
                        public void acceptSearchKey(String keyWord) {
                            mIsShowNoResultTip = true;
                            presenter.requestPoiData(keyWord);// TODO: 2017/6/28 是否使用categories
                            presenter.savePoiSearchKeyWord(keyWord);
                        }
                    });
        }
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.relativeLaySearchKey, mQuickSearchFragment, QUICK_FRAGMENT)
                .commit();
        mEdtTxtSearch = (EditText) findViewById(R.id.edtTxtSearch);
        mEdtTxtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (isFocused) {
                    changeToManualFragment();
                }
            }
        });
        mEdtTxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                mIsShowNoResultTip = false;
                if (TextUtils.isEmpty(s.toString().trim())) {
                    mManualSearchFragment.hideSearchAssociation();
                } else {
                    presenter.requestPoiData(s.toString(), null);
                    mManualSearchFragment.showSearchAssociation();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void changeToManualFragment() {
        if (mManualSearchFragment == null) {
            mManualSearchFragment = new ManualSearchFragment();
            mManualSearchFragment.setOnHistoryOperateListener(
                    new ManualSearchFragment.OnHistoryOperateListener() {

                        @Override
                        public void onSelectHistory(String keyWord) {
                            mEdtTxtSearch.setText(keyWord);
                            mEdtTxtSearch.setSelection(keyWord.length());
                        }

                        @Override
                        public boolean deleteByKeyWord(String keyWord) {
                            return presenter.deleteHistoryPoiKey(keyWord);
                        }
                    });
            mManualSearchFragment.setOnSearchSelectedListener(
                    new ManualSearchFragment.OnSearchSelectedListener() {
                        @Override
                        public void onSelectSearch(SearchResultModel model) {
                            presenter.savePoiSearchKeyWord(model.getName());
                            ArrayList<SearchResultModel> data = new ArrayList<>();
                            data.add(model);
                            setResult(RESULT_OK, PalmapViewActivity.getPoiSearchResultIntent(data));
                            finish();
                        }
                    });
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mIsFirstManual) {
            fragmentTransaction
                    .add(R.id.relativeLaySearchKey, mManualSearchFragment, MANUAL_FRAGMENT)
                    .hide(mQuickSearchFragment);
            mIsFirstManual = false;
        } else {
            fragmentTransaction
                    .show(mManualSearchFragment)
                    .hide(mQuickSearchFragment);
        }
        fragmentTransaction.commit();
        presenter.requestHistoryPOIData();
    }

    @Override
    public void readPoiData(List<LocationModel> data) {
        mIsShowNoResultTip = false;
        if (mQuickSearchFragment.isVisible()) {
            ArrayList<SearchResultModel> models = new ArrayList<>();
            for (LocationModel model : data) {
                if (model == null) {
                    continue;
                }
                models.add(new SearchResultModel(model, null));
            }
            setResult(RESULT_OK, PalmapViewActivity.getPoiSearchResultIntent(models));
            finish();
        } else {
            mManualSearchFragment.addSearchData(data);
        }
    }

    @Override
    public void readHistorySearch(List<String> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        mManualSearchFragment.addHistoryData(data);
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
        if (throwable instanceof NotFoundDataException && mIsShowNoResultTip) {
            showMessage(getString(R.string.ngr_current_no_search_result));
        }
    }

    /**
     * 显示快捷搜索的UI
     */
    @Override
    public void readQuickSearchData(int index, List<QuickSearchKeyWordModel> data) {
        if (index == 0) {
            mQuickSearchFragment.addPanelKeyWords(data);
        } else if (index == 1) {
            mQuickSearchFragment.addGroupKeyWords(data);
        } else {
            //
        }
    }

    public void onSearchClick(View view) {
        String keyWord = mEdtTxtSearch.getText().toString();
        if (TextUtils.isEmpty(keyWord.trim())) {
            showMessage(getString(R.string.ngr_please_input_search_key));
            return;
        }
        ArrayList<SearchResultModel> models = new ArrayList<>();
        models.addAll(mManualSearchFragment.getSearchResultModels());
        if (models.isEmpty()) {
            mIsShowNoResultTip = true;
            presenter.requestPoiData(keyWord);
            presenter.savePoiSearchKeyWord(keyWord);
            presenter.requestHistoryPOIData();
        } else {
            presenter.savePoiSearchKeyWord(keyWord);
            setResult(RESULT_OK, PalmapViewActivity.getPoiSearchResultIntent(models));
            finish();
        }
    }

    public void onGoBack(View view) {
        goBack();
    }

    private void goBack() {
        if (mQuickSearchFragment.isVisible()) {
            finish();
        } else {
            ActivityUtils.hideInputWindow(this);
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(mQuickSearchFragment)
                    .hide(mManualSearchFragment)
                    .commit();
            mEdtTxtSearch.getText().clear();
            mEdtTxtSearch.clearFocus();
        }
    }

    @Override
    public void finish() {
        presenter.destroy();
        super.finish();
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return true;
        }
        return false;
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
