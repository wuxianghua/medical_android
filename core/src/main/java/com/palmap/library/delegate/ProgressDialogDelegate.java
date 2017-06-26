package com.palmap.library.delegate;

import android.app.ProgressDialog;
import android.content.Context;

import com.palmap.library.utils.IOUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by 王天明 on 2015/12/21 0021.
 */
public class ProgressDialogDelegate {

    private ProgressDialog progressDialog;
    private String msg;
    private String title;
    private Context context;

    public ProgressDialogDelegate(Context context, String title, String msg) {
        this.msg = msg;
        this.title = title;
        this.context = context;
    }

    public void show() {
        if (isShowing()) {
            return;
        }
        if (IOUtils.checkMainThread()) {
            showDialog();
        } else {
            AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                @Override
                public void run() {
                    showDialog();
                }
            });
        }
    }

    public void hide() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public boolean isShowing(){
        if (null == progressDialog) {
            return false;
        }
        return progressDialog.isShowing();
    }

    private void showDialog(){
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle(title);
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }
}