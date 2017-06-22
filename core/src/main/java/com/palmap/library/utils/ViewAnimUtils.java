package com.palmap.library.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

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

}
