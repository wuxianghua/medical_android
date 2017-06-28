package com.palmap.exhibition.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.QuickSearchKeyWordModel;
import com.palmap.exhibition.view.adapter.QuickSearchGroupAdapter;

import java.util.List;

/**
 * Created by jinhuang.yang on 2017/6/26.
 * E-mail:jinhuang.yang@palmaplus.com
 */

public class QuickSearchFragment extends Fragment {

    private ExpandableListView mExpandLvGroup = null;
    private QuickSearchGroupAdapter mAdapter = null;
    private OnAcceptSearchKeyListener mOnAcceptSearchKeyListener = null;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quick_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mExpandLvGroup = (ExpandableListView) view.findViewById(R.id.expandLvGroup);
        mAdapter = new QuickSearchGroupAdapter(getContext(), null);
        mAdapter.setOnItemClickListener(new QuickSearchGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(QuickSearchKeyWordModel.ChildBean childBean) {
                if (childBean == null || mOnAcceptSearchKeyListener == null) {
                    return;
                }
                mOnAcceptSearchKeyListener.acceptSearchKey(childBean.getSearchKeyWord());
            }
        });
        mExpandLvGroup.setAdapter(mAdapter);
    }

    public void setOnAcceptSearchKeyListener(OnAcceptSearchKeyListener listener) {
        mOnAcceptSearchKeyListener = listener;
    }

    public void addKeyWords(List<QuickSearchKeyWordModel> keyWordModels) {
        if (!isAdded()) {
            return;
        }
        mAdapter.addAll(keyWordModels);
        for (int i = 0; i < mAdapter.getKeyWordList().size(); i++) {
            QuickSearchKeyWordModel model = mAdapter.getKeyWordList().get(i);
            if (model != null && model.getState() == 1) {
                mExpandLvGroup.expandGroup(i);
            }
        }
    }

    public void refresh() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public interface OnAcceptSearchKeyListener {
        void acceptSearchKey(String keyWord);
    }

}
