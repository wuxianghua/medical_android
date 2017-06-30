package com.palmap.exhibition.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palmap.exhibition.R;
import com.palmap.library.utils.ViewAnimUtils;
import com.palmap.library.utils.ViewUtils;

/**
 * Created by jinhuang.yang on 2017/6/29.
 * E-mail:jinhuang.yang@palmaplus.com
 */

public class StartEndPoiChooseView extends LinearLayout {

    private ImageView imgVBack;
    private TextView tvStartPoiName;
    private TextView tvStartPoiDes;
    private TextView tvEndPoiName;
    private TextView tvEndPoiDes;

    public StartEndPoiChooseView(Context context) {
        super(context);
        initView(context);
    }

    public StartEndPoiChooseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public StartEndPoiChooseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.view_start_end_point_choose, this);
        imgVBack = (ImageView) findViewById(R.id.imgVBack);
        tvStartPoiName = (TextView) findViewById(R.id.tvStartPoiName);
        tvStartPoiDes = (TextView) findViewById(R.id.tvStartPoiDes);
        tvEndPoiName = (TextView) findViewById(R.id.tvEndPoiName);
        tvEndPoiDes = (TextView) findViewById(R.id.tvEndPoiDes);
    }

    public void setStartPoiName(String name) {
        if(name == null || name.isEmpty()){
            name = getResources().getString(R.string.ngr_unknown_position);
        }
        tvStartPoiName.setText(name);
    }

    public void setStartPoiDes(String des) {
        tvStartPoiDes.setText(des);
    }

    public void setEndPoiName(String name) {
        if(name == null || name.isEmpty()){
            name = getResources().getString(R.string.ngr_unknown_position);
        }
        tvEndPoiName.setText(name);
    }

    public void setEndPoiDes(String des) {
        tvEndPoiDes.setText(des);
    }

    public void setOnBackClickListener(OnClickListener listener) {
        if (imgVBack != null) {
            imgVBack.setOnClickListener(listener);
        }
    }

    public void resetInfo(){
        tvStartPoiName.setText("");
        tvStartPoiDes.setText("");
        tvEndPoiName.setText("");
        tvEndPoiDes.setText("");
    }

    public void showRiseAnimation(){
        int originalHeight = ViewUtils.measureView(this).y;
        ViewAnimUtils.animHeight(
                this,
                originalHeight,
                0,
                300, null);
    }

    public void showDropAnimation(){
        int originalHeight = ViewUtils.measureView(this).y;
        ViewAnimUtils.animHeight(
                this,
                0,
                originalHeight,
                300, null);
    }

}
