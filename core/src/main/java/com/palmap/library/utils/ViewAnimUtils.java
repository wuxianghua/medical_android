package com.palmap.library.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by 王天明 on 2016/7/12.
 */
public class ViewAnimUtils {

    public static ValueAnimator animHide(final int height, final View animView, Animator.AnimatorListener animatorListener) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fractuion = animation.getAnimatedFraction();
                ViewGroup.LayoutParams lp = animView.getLayoutParams();
                lp.height = (int) (height * (1 - fractuion));
                animView.setLayoutParams(lp);
                if (fractuion >= 1) {
                    animView.setVisibility(View.GONE);
                }
            }
        });
        if (animatorListener != null) {
            valueAnimator.addListener(animatorListener);
        }
        return valueAnimator;
    }

    public static ValueAnimator animShow(final int height, final View animView, Animator.AnimatorListener animatorListener) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fractuion = animation.getAnimatedFraction();
                ViewGroup.LayoutParams lp = animView.getLayoutParams();
                lp.height = (int) (height * fractuion);
                animView.setLayoutParams(lp);
            }
        });
        if (animatorListener != null) {
            valueAnimator.addListener(animatorListener);
        }
        return valueAnimator;
    }

    public static void animHeight(final View animView, final int tagetHeight, int time, Animator.AnimatorListener animatorListener) {
        animHeight(animView,animView.getHeight(),tagetHeight,time,animatorListener);
    }

    public static void animHeight(final View animView, final int currentHeight, final int tagetHeight, int time, Animator.AnimatorListener animatorListener) {
        if (tagetHeight == currentHeight) {
            return;
        }
        final boolean increase = tagetHeight > currentHeight;
        final int offset = Math.abs(tagetHeight - currentHeight);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(.0f, 1.0f);
        valueAnimator.setDuration(time);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();
                ViewGroup.LayoutParams layoutParams = null;
                int nextHeight;
                if (increase) {
                    nextHeight = (int) (currentHeight + (offset * animatedFraction));
                } else {
                    nextHeight = (int) (currentHeight - (offset * animatedFraction));
                }
                if (animView.getParent() instanceof LinearLayout) {
                    layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, nextHeight);
                }
                if (animView.getParent() instanceof RelativeLayout) {
                    layoutParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT, nextHeight);
                }
                if (animView.getParent() instanceof FrameLayout) {
                    layoutParams = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT, nextHeight);
                }
                if (layoutParams == null) {
                    layoutParams = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, nextHeight);
                }
                animView.setLayoutParams(layoutParams);
            }
        });
        if (animatorListener != null) {
            valueAnimator.addListener(animatorListener);
        }
        valueAnimator.start();
    }

}
