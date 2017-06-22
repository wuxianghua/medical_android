package com.palmap.library.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Vibrator;

/**
 * Created by 王天明 on 2016/9/9.
 */
public class SystemUtils {

    /**
     * 打开蓝牙
     */
    public static void openBluetooth(){
        try{
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
                    .getDefaultAdapter();
            mBluetoothAdapter.enable();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void switchWifi(Context context,boolean open){
        try{
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(open);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 震动
     */
    public static void vibrate(Context context,long time) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
    }

    /**
     * 震动
     */
    public static void vibrate(Context context,long[] pattern, int repeat) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern,repeat);
    }

    public static void cancelVibrate(Context context){
        ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).cancel();
    }

}
