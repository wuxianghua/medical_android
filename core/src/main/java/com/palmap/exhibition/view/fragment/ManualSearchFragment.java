package com.palmap.exhibition.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.SearchResultModel;
import com.palmap.exhibition.view.adapter.HistoryListAdapter;
import com.palmap.exhibition.view.adapter.SearchListAdapter;
import com.palmaplus.nagrand.data.LocationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinhuang.yang on 2017/6/26.
 * E-mail:jinhuang.yang@palmaplus.com
 */

public class ManualSearchFragment extends Fragment {

    private HistoryListAdapter mHistoryAdapter = null;
    private SearchListAdapter mSearchAdapter = null;
    private OnHistoryOperateListener mOnHistoryOperateListener = null;
    private OnSearchSelectedListener mOnSearchSelectedListener = null;
    private LinearLayout mLinearLayHistory = null;
    private ListView mLvAssociation = null;
    private List<String> mHistoryData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manual_search, container, false);
        initHistoryList(view);
        initAssociationList(view);
        return view;
    }

    private void initHistoryList(View view) {
        mLinearLayHistory = (LinearLayout) view.findViewById(R.id.linearLayHistory);
        ListView mLvHistory = (ListView) view.findViewById(R.id.lvHistory);
        mHistoryAdapter = new HistoryListAdapter(getContext(), mHistoryData);
        mHistoryAdapter.setOnDeleteClickListener(new HistoryListAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(String keyword) {
                if (mOnHistoryOperateListener != null &&
                        mOnHistoryOperateListener.deleteByKeyWord(keyword)) {
                    mHistoryData.remove(keyword);
                    refreshHistory();
                }
            }
        });
        mLvHistory.setAdapter(mHistoryAdapter);
        mLvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mOnHistoryOperateListener != null){
                    mOnHistoryOperateListener.onSelectHistory(mHistoryAdapter.getItem(i));
                }
            }
        });
    }

    private void initAssociationList(View view) {
        mLvAssociation = (ListView) view.findViewById(R.id.lvAssociation);
        mSearchAdapter = new SearchListAdapter(getContext(), "");
        mLvAssociation.setAdapter(mSearchAdapter);
        mLvAssociation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mOnSearchSelectedListener != null) {
                    mOnSearchSelectedListener.onSelectSearch(mSearchAdapter.getItem(i));
                }
            }
        });
    }

    public void setOnHistoryOperateListener(OnHistoryOperateListener listener) {
        mOnHistoryOperateListener = listener;
    }

    public void setOnSearchSelectedListener(OnSearchSelectedListener listener) {
        mOnSearchSelectedListener = listener;
    }

    public void refreshHistory() {
        if (mHistoryAdapter != null) {
            mHistoryAdapter.notifyDataSetChanged();
        }
    }

    public void addHistoryData(List<String> data) {
        mHistoryData.clear();
        mHistoryData.addAll(data);
        mHistoryAdapter.setHistoryData(mHistoryData);
        refreshHistory();
    }

    public void showSearchAssociation(){
        mLinearLayHistory.setVisibility(View.GONE);
        mLvAssociation.setVisibility(View.VISIBLE);
    }

    public void hideSearchAssociation(){
        mLinearLayHistory.setVisibility(View.VISIBLE);
        mLvAssociation.setVisibility(View.GONE);
        mSearchAdapter.clear();
    }

    public void addSearchData(List<LocationModel> data) {
        mSearchAdapter.clear();
        mSearchAdapter.addPoiData(data);
    }

    public List<SearchResultModel> getSearchResultModels(){
        return mSearchAdapter.getLocationModel();
    }

    public interface OnHistoryOperateListener {
        void onSelectHistory(String keyWord);

        boolean deleteByKeyWord(String keyWord);
    }

    public interface OnSearchSelectedListener {
        void onSelectSearch(SearchResultModel model);
    }

}
