package com.palmap.exhibition.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmap.exhibition.BuildConfig;
import com.palmap.exhibition.R;
import com.palmap.exhibition.view.FloorDataProvides;
import com.palmap.library.utils.DeviceUtils;
import com.palmap.library.utils.LogUtil;
import com.palmap.library.utils.ViewUtils;

/**
 * Created by 王天明 on 2016/9/12.
 * 路线详情
 */
public class RouteInfoView extends LinearLayout {

    private TextView tv_detailsMsg, tv_endMsg, tv_startMsg;
    private TextView lable_endMsg, lable_startMsg;

    private ViewGroup layout_Arrow;
    private ImageView img_Arrow;
    private TextView tv_Arrow;

    private ViewGroup layout_routeFloor;
    private ViewGroup layout_info_top;
    private LinearLayout layout_routeFloor_content;

    public RouteInfoView(Context context) {
        this(context, null);
    }

    public RouteInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RouteInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_route_info, this);
        bindView();
    }

    private void bindView() {
        tv_detailsMsg = (TextView) findViewById(R.id.tv_detailsMsg);
        tv_endMsg = (TextView) findViewById(R.id.tv_endMsg);
        tv_startMsg = (TextView) findViewById(R.id.tv_startMsg);

        lable_startMsg = (TextView) findViewById(R.id.lable_startMsg);
        lable_endMsg = (TextView) findViewById(R.id.lable_endMsg);

        layout_Arrow = (ViewGroup) findViewById(R.id.layout_Arrow);
        img_Arrow = (ImageView) findViewById(R.id.img_Arrow);
        tv_Arrow = (TextView) findViewById(R.id.tv_Arrow);

        layout_info_top = (ViewGroup) findViewById(R.id.layout_info_top);
        layout_routeFloor = (ViewGroup) findViewById(R.id.layout_routeFloor);
        layout_routeFloor_content = (LinearLayout) findViewById(R.id.layout_routeFloor_content);

    }

    public void showStartMsg(String lable, String msg) {
        setVisibility(VISIBLE);
        if (TextUtils.isEmpty(lable)) lable = "辅助";
        lable_startMsg.setText(lable);
        tv_startMsg.setText(msg);
    }

    public void showEndMsg(String lable, String msg) {
        setVisibility(VISIBLE);
        if (TextUtils.isEmpty(lable)) lable = "辅助";
        lable_endMsg.setText(lable);
        tv_endMsg.setText(msg);
    }

    public void showDetailsMsg(String details) {
        setVisibility(VISIBLE);
        tv_detailsMsg.setText(details);
    }

    public void showUpView() {
//        img_Arrow.setImageResource(R.mipmap.ico_in_up);
//        tv_Arrow.setText(getContext().getString(R.string.ngr_msg_navi_up));
//        layout_Arrow.setVisibility(VISIBLE);
    }

    public void showDownView() {
//        img_Arrow.setImageResource(R.mipmap.ico_in_down);
//        tv_Arrow.setText(getContext().getString(R.string.ngr_msg_navi_down));
//        layout_Arrow.setVisibility(VISIBLE);
    }

    public void hideArrowView() {
        layout_Arrow.setVisibility(INVISIBLE);
    }

    public void clear() {
        lable_startMsg.setText(null);
        tv_startMsg.setText(null);
        lable_endMsg.setText(null);
        tv_endMsg.setText(null);
        tv_detailsMsg.setText(null);
    }

    public void showFloorsView(FloorDataProvides floorDataProvides, long[] floorIds, long currentFloorId) {
        setVisibility(VISIBLE);
        destroyFloorLayout();
        if (floorIds == null || floorIds.length < 2 || floorDataProvides == null) {
            return;
        }
        int width = ViewUtils.measureView(this).x - DeviceUtils.dip2px(getContext(), 10);
//        int width = ViewUtils.measureView(this).x - getChildAt(0).getPaddingLeft() - getChildAt(0).getPaddingRight();
        LogUtil.e("width:" + width);

        for (int i = 0; i < floorIds.length; i++) {
            long floorId = floorIds[i];
            String floorName = floorDataProvides.getFloorNameById(floorId);
            if (BuildConfig.DEBUG && floorName == null) {
                floorName = "C1"; //测试
            }

            TextView textView = new TextView(getContext());

            textView.setTextColor(Color.WHITE);

            textView.setText(floorName);

            textView.setTextSize(8);

            textView.setGravity(Gravity.CENTER);

            textView.setBackgroundResource(R.mipmap.ico_in_green);

            if (i == 0) {
                textView.setBackgroundResource(R.mipmap.ico_in_blue);
            }
            if (i == floorIds.length - 1) {
                textView.setBackgroundResource(R.mipmap.ico_in_red);
            }

            RelativeLayout tempLayout = new RelativeLayout(getContext());
            tempLayout.setGravity(Gravity.CENTER);
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(R.mipmap.ico_in_arrow);
            imageView.setVisibility(INVISIBLE);

            if (floorId == currentFloorId) {//当前楼层显示一个角标图片
                imageView.setVisibility(VISIBLE);
            }

            RelativeLayout.LayoutParams rLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rLp.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
            textView.setLayoutParams(rLp);
//            if (currentFloorId != -1) {
            tempLayout.addView(imageView);
//            }
            tempLayout.addView(textView);
            //计算2个item之间的距离
            int offsetSize = (width - floorIds.length * ViewUtils.measureView(tempLayout).x) / (floorIds.length - 1);
            if (i != 0) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(offsetSize, 0, 0, 0);
                tempLayout.setLayoutParams(lp);
            }
            layout_routeFloor_content.addView(tempLayout);
        }
        layout_routeFloor.setVisibility(VISIBLE);
        layout_info_top.addView(layout_routeFloor);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility != VISIBLE) {
            destroyFloorLayout();
        }
    }

    private LinearLayout createFloorLayout() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setLayerType(LAYER_TYPE_SOFTWARE, null);
        LinearLayout.LayoutParams layouLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layouLp.setMargins(0, DeviceUtils.dip2px(getContext(), 10), 0, 0);
        layout.setLayoutParams(layouLp);
        layout.setOrientation(HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setBackgroundResource(R.drawable.dotted_line);
        return layout;
    }

    private void destroyFloorLayout() {
        if (layout_routeFloor != null) {
            LogUtil.e("移除layout_routeFloor");
            layout_routeFloor_content.removeAllViews();
            layout_info_top.removeView(layout_routeFloor);
        }
    }
}