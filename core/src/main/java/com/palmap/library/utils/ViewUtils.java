package com.palmap.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;

/**
 * Created by zhang on 2015/9/14.
 */
public class ViewUtils {

    /*
    *  根据item项设置listView宽高
    *  @param isMeasuredWidth 是否测量width
    * */
    public static void setListViewBasedOnItem(ListView listView, int maxLineNum, boolean isMeasuredWidth) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int maxWidth = 0;

        View listItem = null;
        Log.w("ViewUtils", "listAdapter.getCount() = " + listAdapter.getCount());
        for (int i = 0; i < maxLineNum && i < listAdapter.getCount(); i++) {
            listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            int width = listItem.getMeasuredWidth();
            if (width > maxWidth) maxWidth = width;
        }

        totalHeight += listView.getDividerHeight() * maxLineNum; // 加上分割线高度

        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = totalHeight;
        if (isMeasuredWidth) {
            layoutParams.width = maxWidth;
        }
        listView.setLayoutParams(layoutParams);

    }

    /*
    *  测量view的宽高
    * */
    public static Point measureView(View view) {
        if (view == null) {
            return null;
        }

        Point point = new Point();
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        point.x = view.getMeasuredWidth();
        point.y = view.getMeasuredHeight();

        return point;
    }

    /*
    * 获取手机屏幕分辨率
    * */
    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /*
    * 关闭软键盘
    * @param view 当前获取焦点视图
    * */
    public static void closeInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 扩大view的点击范围
     *
     * @param clickView
     * @param size
     */
    public static void expandViewClickRegion(View clickView, int size) {
        Rect rect = new Rect();
        clickView.getHitRect(rect);
        rect.left -= size;
        rect.top -= size;
        rect.right += size;
        rect.bottom += size;
        TouchDelegate touchDelegate = new TouchDelegate(rect, clickView);
        ((ViewGroup) clickView.getParent()).setTouchDelegate(touchDelegate);
    }

    /**
     * 判断一个点在不在屏幕可视区内
     * @param context
     * @param x
     * @param y
     * @return
     */
    public static boolean pointInWindowFrame(Activity context, int x, int y) {
        if (context == null) return false;
        int deviceWidth = DeviceUtils.getWidth(context);
        int deviceHeight = DeviceUtils.getHeight(context);
        Rect rect = new Rect(0, 0, deviceWidth, deviceHeight);
        return rect.contains(x, y);
    }

    /**
     * 设置Drawer的左边拖动的触摸事件区域大小
     * @param activity
     * @param drawerLayout
     * @param displayWidthPercentage
     */
    public static void setDrawerLeftEdgeSize(Activity activity,
                                            DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null)
            return;
        try {
            Field leftDraggerField = drawerLayout.getClass().getDeclaredField(
                    "mLeftDragger");
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField
                    .get(drawerLayout);
            Field edgeSizeField = leftDragger.getClass().getDeclaredField(
                    "mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize,
                    (int) (dm.widthPixels * displayWidthPercentage)));
        } catch (Exception e) {
        }
    }

    /**
     * 创建一个带颜色的文字段落
     * @param name
     * @param keyWord
     * @param color
     * @return
     */
    public static SpannableStringBuilder builderColorText(String name, String keyWord, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(name);
        int index = name.indexOf(keyWord);
        int end = index + keyWord.length();
        builder.setSpan(new ForegroundColorSpan(color), index, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

}
