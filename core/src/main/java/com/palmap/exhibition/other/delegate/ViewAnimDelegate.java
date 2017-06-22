package com.palmap.exhibition.other.delegate;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * Created by 王天明 on 2016/5/4.
 */
public class ViewAnimDelegate {

    private View view;
    private Animation showAnim;
    private Animation hideAnim;

    private long time;

    private static final long DEFAULT_TIME = 200;

    public enum Gravity {
        Top,
        Bottom,
        Left,
        Right
    }

    public ViewAnimDelegate(View view, Gravity gravity, long time) {
        this.time = time;
        this.view = view;

        switch (gravity) {
            case Top:
                showAnim = new ScaleAnimation(1, 1, 0, 1,
                        Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
                hideAnim = new ScaleAnimation(1, 1, 1, 0,
                        Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
                break;
            case Bottom:
                showAnim = new ScaleAnimation(1, 1, 0, 1,
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
                hideAnim = new ScaleAnimation(1, 1, 1, 0,
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
                break;
            case Left:
                showAnim = new ScaleAnimation(0, 1, 1, 1,
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                hideAnim = new ScaleAnimation(1, 0, 1, 1,
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                break;
            case Right:
                showAnim = new ScaleAnimation(0, 1, 1, 1,
                        Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 1);
                hideAnim = new ScaleAnimation(1, 0, 1, 1,
                        Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 1);
                break;
        }
        showAnim.setDuration(time);
        hideAnim.setDuration(time);
    }

    public ViewAnimDelegate(View view, Gravity gravity) {
        this(view, gravity, DEFAULT_TIME);
    }

    public void showOnAnim() {
        if (null == view || view.getVisibility() == View.VISIBLE) return;
        view.setVisibility(View.VISIBLE);
        view.startAnimation(showAnim);
    }

    public void hideOnAnim() {
        if (null == view || view.getVisibility() != View.VISIBLE) return;
        view.setVisibility(View.GONE);
        view.startAnimation(hideAnim);
    }

    public void switchVisibilityOnAnim() {
        if (null == view) return;
        if (view.getVisibility() != View.VISIBLE) {
            showOnAnim();
        } else {
            hideOnAnim();
        }
    }
}
