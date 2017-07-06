package com.palmap.exhibition.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.QuickSearchKeyWordModel;
import com.palmap.exhibition.view.adapter.QuickSearchGroupAdapter;
import com.palmap.exhibition.widget.QuickSearchPanelView;
import com.palmap.exhibition.widget.imgslider.Indicators.PagerIndicator;
import com.palmap.exhibition.widget.imgslider.SliderLayout;
import com.palmap.library.utils.DeviceUtils;

import java.util.List;

/**
 * Created by jinhuang.yang on 2017/6/26.
 * E-mail:jinhuang.yang@palmaplus.com
 */

public class QuickSearchFragment extends Fragment {

    private ExpandableListView mExpandLvGroup = null;
    private QuickSearchGroupAdapter mAdapter = null;
    private OnAcceptSearchKeyListener mOnAcceptSearchKeyListener = null;
    private SliderLayout mSliderView = null;

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
        mExpandLvGroup.addHeaderView(getSliderView());
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

    public void addPanelKeyWords(List<QuickSearchKeyWordModel> keyWordModels) {
        if (!isAdded()) {
            return;
        }
        if (keyWordModels == null || keyWordModels.isEmpty()) {
            return;
        }
        QuickSearchKeyWordModel model = keyWordModels.get(0);
        if (model == null) {
            return;
        }
        int pageCount = (int) Math.ceil(model.getChild().size() / 8.0);
        for (int i = 0; i < pageCount; i++) {
            QuickSearchPanelView view = new QuickSearchPanelView(getContext(),
                    model.getChild().subList(i * 8, 8 * (i + 1) > model.getChild().size()
                            ? model.getChild().size() : 8 * (i + 1)));
            view.setItemViewClickListener(new QuickSearchPanelView.ItemViewClickListener() {
                @Override
                public void onItemClick(QuickSearchKeyWordModel.ChildBean childBean) {
                    if (childBean == null || mOnAcceptSearchKeyListener == null) {
                        return;
                    }
                    mOnAcceptSearchKeyListener.acceptSearchKey(childBean.getSearchKeyWord());
                }
            });
            mSliderView.addSlider(view);
        }
        mSliderView.setIndicatorVisibility(pageCount > 1
                ? PagerIndicator.IndicatorVisibility.Visible
                : PagerIndicator.IndicatorVisibility.Gone);
    }

    public void addGroupKeyWords(List<QuickSearchKeyWordModel> keyWordModels) {
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

    private View getSliderView() {
        if (mSliderView == null) {
            mSliderView = new SliderLayout(getContext());
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtils.dip2px(getContext(), 165));
            mSliderView.setLayoutParams(layoutParams);
            mSliderView.getPagerIndicator().setDefaultIndicatorColor(
                    getResources().getColor(R.color.ngr_indicator_color_active),
                    getResources().getColor(R.color.ngr_indicator_color_normal));
            mSliderView.stopAutoCycle();
        }
        return mSliderView;
    }

    public interface OnAcceptSearchKeyListener {
        void acceptSearchKey(String keyWord);
    }

}
