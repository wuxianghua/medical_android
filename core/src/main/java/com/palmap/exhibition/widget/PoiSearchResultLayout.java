package com.palmap.exhibition.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.PoiModel;
import com.palmap.library.utils.DeviceUtils;
import com.palmap.library.utils.ViewAnimUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinhuang.yang on 2017/6/29.
 * E-mail:jinhuang.yang@palmaplus.com
 */

public class PoiSearchResultLayout extends LinearLayout
        implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView mImgVExpand = null;
    private ListView mLvSearchResult = null;
    private boolean mIsExpand = true;
    private List<PoiModel> mPoiModels = new ArrayList<>();
    private SearchResultListAdapter mAdapter = null;
    private OnResultClickListener mOnResultClickListener = null;
    private int listItemHeight = 0;
    private int currentMaxHeight = 0;
    private int topHeight = 0;
    private int currentHeight = 0;

    public PoiSearchResultLayout(Context context) {
        super(context);
        initView(context);
    }

    public PoiSearchResultLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_poi_search_result, this);
        mImgVExpand = (ImageView) findViewById(R.id.imgVExpand);
        mLvSearchResult = (ListView) findViewById(R.id.lvSearchResult);
        mImgVExpand.setOnClickListener(this);
        mLvSearchResult.setOnItemClickListener(this);
        mAdapter = new SearchResultListAdapter();
        mLvSearchResult.setAdapter(mAdapter);

        listItemHeight = DeviceUtils.dip2px(context, 60);
        topHeight = DeviceUtils.dip2px(context, 30);
    }

    public void addData(List<PoiModel> models) {
        if (mPoiModels == null) {
            mPoiModels = new ArrayList<>();
        } else {
            mPoiModels.clear();
        }
        mPoiModels.addAll(models);
        mImgVExpand.setImageResource(R.mipmap.ic_map_floordown);
        mIsExpand = true;
        mImgVExpand.setVisibility(mPoiModels.size() > 1 ? VISIBLE : GONE);
        LinearLayout.LayoutParams listViewLp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        int size = mPoiModels.size();
        if (size > 2) {
            listViewLp.height = listItemHeight * 3;
        } else {
            listViewLp.height = listItemHeight * size;
        }
        mLvSearchResult.setLayoutParams(listViewLp);
        mLvSearchResult.setVisibility(VISIBLE);
        mAdapter.notifyDataSetChanged();
        currentMaxHeight = listViewLp.height + topHeight;
        currentHeight = listViewLp.height + (mImgVExpand.getVisibility() == VISIBLE ? topHeight : 0);
    }

    public void setOnResultClickListener(OnResultClickListener listener) {
        mOnResultClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (mIsExpand) {
            mIsExpand = false;
            mLvSearchResult.setVisibility(GONE);
            showDropAnimation();
            mImgVExpand.setImageResource(R.mipmap.ic_map_floorup);
        } else {
            mIsExpand = true;
            mLvSearchResult.setVisibility(VISIBLE);
            showRiseAnimation();
            mImgVExpand.setImageResource(R.mipmap.ic_map_floordown);
        }
    }

    public void showRiseAnimation() {
        ViewAnimUtils.animHeight(
                (View) getParent(),
                currentMaxHeight,
                300, null);
    }

    /**
     * 缩小
     */
    public void showDropAnimation() {
        ViewAnimUtils.animHeight(
                (View) getParent(),
                topHeight,
                300, null);
    }

    public int getCurrentHeight() {
        return currentHeight;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mOnResultClickListener.showIcon(mPoiModels.get(i));
    }

    private class SearchResultListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPoiModels == null ? 0 : mPoiModels.size();
        }

        @Override
        public Object getItem(int i) {
            return mPoiModels == null ? null : mPoiModels.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(getContext())
                        .inflate(R.layout.layout_poi_search_info, viewGroup, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            final PoiModel model = mPoiModels.get(i);
            if (model != null) {
                viewHolder.tv_poiName.setText(model.getDisPlay());
                viewHolder.tvPoiDes.setText(model.getFloorName() + " " + model.getAddress());
                viewHolder.btn_goHere.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnResultClickListener.goHere(model);
                    }
                });
            }
            return view;
        }
    }

    private static class ViewHolder {

        TextView tv_poiName;
        TextView tvPoiDes;
        TextView btn_goHere;

        ViewHolder(View rootView) {
            tv_poiName = (TextView) rootView.findViewById(R.id.tv_poiName);
            tvPoiDes = (TextView) rootView.findViewById(R.id.tvPoiDes);
            btn_goHere = (TextView) rootView.findViewById(R.id.btn_goHere);
        }
    }

    interface OnResultClickListener {
        void goHere(PoiModel model);

        void showIcon(PoiModel model);
    }

}
