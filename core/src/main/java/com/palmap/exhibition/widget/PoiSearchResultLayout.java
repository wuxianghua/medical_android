package com.palmap.exhibition.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.PoiModel;
import com.palmap.exhibition.view.adapter.SearchResultListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinhuang.yang on 2017/6/29.
 * E-mail:jinhuang.yang@palmaplus.com
 */

public class PoiSearchResultLayout extends LinearLayout implements View.OnClickListener{

    private ImageView mImgVExpand = null;
    private ListView mLvSearchResult = null;
    private boolean mIsExpand = true;
    private List<PoiModel> mPoiModels = new ArrayList<>();
    private SearchResultListAdapter mAdapter = null;

    public PoiSearchResultLayout(Context context) {
        super(context);
        initView(context);
    }

    public PoiSearchResultLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PoiSearchResultLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = inflate(context, R.layout.layout_poi_search_result, null);
        mImgVExpand = (ImageView)view.findViewById(R.id.imgVExpand);
        mLvSearchResult = (ListView)view.findViewById(R.id.lvSearchResult);
        mImgVExpand.setOnClickListener(this);
//        mAdapter = new SearchResultListAdapter(context, mPoiModels);
    }

    public void addData(){

    }

    @Override
    public void onClick(View view) {
        if(mIsExpand){
            mImgVExpand.setImageResource(R.mipmap.ic_list_arrow_down);
            mLvSearchResult.setVisibility(VISIBLE);
        }else{
            mImgVExpand.setImageResource(R.mipmap.ic_list_arrow_up);
            mLvSearchResult.setVisibility(GONE);
        }
    }

}
