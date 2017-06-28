package com.palmap.exhibition.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.PoiModel;
import com.palmap.exhibition.view.impl.PalmapViewState;
import com.palmap.library.utils.LogUtil;
import com.palmap.library.utils.ViewAnimUtils;
import com.palmap.library.utils.ViewUtils;

/**
 * Created by 王天明 on 2017/6/28.
 */

public class PoiMenuLayout extends LinearLayout implements IPoiMenu {

    private ViewGroup layout_poi_info;
    private ViewGroup layout_select_start;
    private ViewGroup layout_navi_ready;
    private ViewGroup layout_navi_info;
    private ViewGroup layout_navi_ok;

    private View btn_goHere;
    private View btn_mockNavi;
    private View btn_startNavi;

    private int height_poi_info = 0;
    private int height_select_start = 0;
    private int height_navi_ready = 0;
    private int height_navi_info = 0;
    private int height_navi_ok = 0;

    private PoiModel poiModel;

    public interface ViewHandler{
        ViewHandler DEFAULE = new ViewHandler() {
            @Override
            public void onGoHereClick() {}
            @Override
            public void onMockNaviClick() {}
            @Override
            public void onStartNaviClick() {}
        };

        void onGoHereClick();

        void onMockNaviClick();

        void onStartNaviClick();
    }

    private ViewHandler viewHandler = ViewHandler.DEFAULE;


    public PoiMenuLayout(Context context) {
        this(context, null);
    }

    public PoiMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_poi_menu, this);
        bindView();
    }

    private void bindView() {
        layout_poi_info = (ViewGroup) findViewById(R.id.layout_poi_info);
        layout_select_start = (ViewGroup) findViewById(R.id.layout_select_start);
        layout_navi_ready = (ViewGroup) findViewById(R.id.layout_navi_ready);
        layout_navi_info = (ViewGroup) findViewById(R.id.layout_navi_info);
        layout_navi_ok = (ViewGroup) findViewById(R.id.layout_navi_ok);
        btn_goHere = findViewById(R.id.btn_goHere);
        btn_startNavi = findViewById(R.id.btn_startNavi);
        btn_mockNavi = findViewById(R.id.btn_mockNavi);

        height_poi_info = ViewUtils.measureView(layout_poi_info).y;
        height_select_start = ViewUtils.measureView(layout_select_start).y;
        height_navi_ready = ViewUtils.measureView(layout_navi_ready).y;
        height_navi_info = ViewUtils.measureView(layout_navi_info).y;
        height_navi_ok = ViewUtils.measureView(layout_navi_ok).y;

        btn_goHere.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHandler.onGoHereClick();
            }
        });

        btn_mockNavi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHandler.onMockNaviClick();
            }
        });

        btn_startNavi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHandler.onStartNaviClick();
            }
        });


    }

    public void setViewHandler(ViewHandler viewHandler) {
        this.viewHandler = viewHandler == null ? ViewHandler.DEFAULE : viewHandler;
    }

    @Override
    public void refreshView(PalmapViewState state) {
        LogUtil.e("state :" + state);
        layout_poi_info.setVisibility(GONE);
        layout_select_start.setVisibility(GONE);
        layout_navi_ready.setVisibility(GONE);
        layout_navi_info.setVisibility(GONE);
        layout_navi_ok.setVisibility(GONE);

        int nextHeight = getHeight();
        switch (state) {
            case Normal:
                layout_poi_info.setVisibility(VISIBLE);
                nextHeight = height_poi_info;
                break;

            case ENd_SET:
                layout_select_start.setVisibility(VISIBLE);
                nextHeight = height_select_start;
                break;

            case RoutePlanning:
                layout_navi_ready.setVisibility(VISIBLE);
                nextHeight = height_navi_ready;
                break;
            default:

                break;
        }
        animShow(nextHeight);
    }

    public void animShow(int height) {
        ViewAnimUtils.animHeight(
                this,
                this.getHeight(),
                height,
                300, null);
    }

    public PoiModel getPoiModel() {
        return poiModel;
    }

    @Override
    public void refreshView(PoiModel poiModel, PalmapViewState state) {
        this.poiModel = poiModel;
        refreshView(state);
    }
}
