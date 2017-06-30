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

public class NavigationTipPanelView extends LinearLayout {

    private ImageView imgVSign;
    private TextView tvNavigationTip;
    private TextView tvCurrentPosition;
    private TextView tvTargetPosition;

    public NavigationTipPanelView(Context context) {
        super(context);
        initView(context);
    }

    public NavigationTipPanelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NavigationTipPanelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.view_navigation_tip_panel, this);
        imgVSign = (ImageView) findViewById(R.id.imgVSign);
        tvNavigationTip = (TextView) findViewById(R.id.tvNavigationTip);
        tvCurrentPosition = (TextView) findViewById(R.id.tvCurrentPosition);
        tvTargetPosition = (TextView) findViewById(R.id.tvTargetPosition);
    }

    public void setSignIcon(int resID) {
        if(resID == 0){
            imgVSign.setVisibility(GONE);
        }else {
            imgVSign.setVisibility(VISIBLE);
            imgVSign.setImageResource(resID);
        }
    }

    public void setNavigationTip(String info) {
        tvNavigationTip.setText(info);
    }

    public void setTvCurrentPosition(String currentPosition) {
        tvCurrentPosition.setText(currentPosition);
    }

    public void setTargetPosition(String targetPosition) {
        tvTargetPosition.setText(targetPosition);
    }

    public void resetInfo(){
        imgVSign.setImageResource(R.mipmap.ic_nav_straight);
        tvNavigationTip.setText("");
        tvCurrentPosition.setText("");
        tvTargetPosition.setText("");
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
