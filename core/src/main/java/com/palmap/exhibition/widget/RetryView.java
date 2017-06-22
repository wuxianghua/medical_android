package com.palmap.exhibition.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmap.exhibition.R;


/**
 * Created by 王天明 on 2016/6/7.
 */
public class RetryView extends RelativeLayout {

    TextView tvMsg;
    Button btnRetry;

    private OnClickListener retryClickListener = null;

    private String msg;


    public RetryView(Context context) {
        this(context, null);
    }

    public RetryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_retry, this);

        tvMsg = (TextView) findViewById(R.id.tv_msg);
        btnRetry = (Button) findViewById(R.id.btn_retry);

        btnRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (retryClickListener != null) {
                    retryClickListener.onClick(v);
                }
            }
        });
    }

    public static RetryView showRetryView(Context context, ViewGroup parent, String msg, OnClickListener retryClickListener) {
        RetryView retryView = new RetryView(context);
        retryView.setRetryClickListener(retryClickListener);
        retryView.setMsg(msg);
        parent.addView(retryView);
        return retryView;
    }

    public void setRetryClickListener(OnClickListener retryClickListener) {
        this.retryClickListener = retryClickListener;
    }

    public void setMsg(String msg) {
        this.msg = msg;
        tvMsg.setText(msg);
    }
}
