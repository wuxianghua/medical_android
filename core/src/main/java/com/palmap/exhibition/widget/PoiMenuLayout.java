package com.palmap.exhibition.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.palmap.exhibition.R;
import com.palmap.library.utils.ViewAnimUtils;
import com.palmap.library.utils.ViewUtils;

/**
 * Created by 王天明 on 2017/6/28.
 */

public class PoiMenuLayout extends LinearLayout {

    private ViewGroup layout_poi_info;
    private ViewGroup layout_select_start;
    private ViewGroup layout_navi_ready;
    private ViewGroup layout_navi_info;
    private ViewGroup layout_navi_ok;

    private View btn_goHere;

    private int height_poi_info = 0;
    private int height_select_start = 0;
    private int height_navi_ready = 0;
    private int height_navi_info = 0;
    private int height_navi_ok = 0;

    public PoiMenuLayout(Context context) {
        this(context, null);
    }

    public PoiMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_poi_menu, this);
        bindView();
    }

    private void bindView() {
        layout_poi_info     = (ViewGroup) findViewById(R.id.layout_poi_info);
        layout_select_start = (ViewGroup) findViewById(R.id.layout_select_start);
        layout_navi_ready   = (ViewGroup) findViewById(R.id.layout_navi_ready);
        layout_navi_info    = (ViewGroup) findViewById(R.id.layout_navi_info);
        layout_navi_ok      = (ViewGroup) findViewById(R.id.layout_navi_ok);
        btn_goHere          = findViewById(R.id.btn_goHere);

        height_poi_info     = ViewUtils.measureView(layout_select_start).y;
        height_select_start = ViewUtils.measureView(layout_select_start).y;
        height_navi_ready   = ViewUtils.measureView(layout_navi_ready).y;
        height_navi_info    = ViewUtils.measureView(layout_navi_info).y;
        height_navi_ok      = ViewUtils.measureView(layout_navi_ok).y;

        btn_goHere.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_poi_info.setVisibility(GONE);
                layout_select_start.setVisibility(View.VISIBLE);
                ViewAnimUtils.animHeight(
                        layout_select_start,
                        getHeight(),
                        height_select_start,
                        300,null);
            }
        });
    }
}
