package com.palmap.exhibition.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by 王天明 on 2016/9/21.
 * 定位显示指针方向委托
 */

public class LocationSensorDelegate {

    private final static boolean DEBUG = false;

    public interface AcceleroListener {
        void onSensorChanged(SensorEvent event, double angle);
    }

    private Sensor mOrientationSensor;// 方向传感器

    private SensorManager mSensorManager;
    private Activity context;
    private AcceleroListener listener;
    private AccelerateInterpolator mInterpolator;

    private static final float MAX_ROTATE_DEGREE = 1.0f;

    public LocationSensorDelegate(Activity activity) {
        // 实例化传感器管理者
        this.context = activity;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mInterpolator = new AccelerateInterpolator();
        try {
            mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        if (mOrientationSensor != null) {
            mSensorManager.registerListener(new MySensorEventListener(),
                    mOrientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void onPause() {
        if (mOrientationSensor != null) {
            mSensorManager.unregisterListener(new MySensorEventListener());
        }
    }

    private float mDirection;

    private class MySensorEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (null == listener) return;
            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                float direction = event.values[0] * -1.0f;
                float mTargetDirection = normalizeDegree(direction);
                if (mDirection != mTargetDirection) {
                    float shorterRoutine = mTargetDirection;
                    if (shorterRoutine - mDirection > 180) {
                        shorterRoutine -= 360;
                    } else if (shorterRoutine - mDirection < -180) {
                        shorterRoutine += 360;
                    }
                    float distance = shorterRoutine - mDirection;
                    if (Math.abs(distance) > MAX_ROTATE_DEGREE) {
                        distance = distance > 0 ? MAX_ROTATE_DEGREE : (-1.0f * MAX_ROTATE_DEGREE);
                    }
                    mDirection = normalizeDegree(mDirection + ((shorterRoutine - mDirection) * mInterpolator.getInterpolation(Math.abs(distance) > MAX_ROTATE_DEGREE ? 0.4f : 0.3f)));
                }
                float angle = normalizeDegree(mTargetDirection * -1.0f);
                listener.onSensorChanged(event, angle);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }

    public void setListener(AcceleroListener listener) {
        this.listener = listener;
    }

    private float normalizeDegree(float degree) {
        return (degree + 720) % 360;
    }
}
