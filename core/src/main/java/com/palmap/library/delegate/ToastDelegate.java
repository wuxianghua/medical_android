package com.palmap.library.delegate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.Toast;

/**
 * Created by 王天明 on 2015/12/18 0018.
 */
public class ToastDelegate {

    private Toast toast;
    @SuppressLint("ShowToast")
    public ToastDelegate(Activity context) {
        toast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
    }

    public void showMsg(String msg) {
        toast.setText(msg);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showMsgLong(String msg) {
        toast.setText(msg);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}