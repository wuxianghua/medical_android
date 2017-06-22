package com.palmap.exhibition.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.library.utils.ViewUtils;

/**
 * Created by 王天明 on 2016/10/27.
 * 任务完成时的分享对话框
 */
public class TaskShareDialog extends Dialog {

    TextView tvMsg;

    Api_ActivityInfo.ObjBean objBean;

    OnShareComplete onShareComplete;

    public TaskShareDialog(Context context, Api_ActivityInfo.ObjBean objBean) {
        super(context, R.style.dialog_task);
        setContentView(R.layout.dialog_taskshare);
        this.objBean = objBean;

        findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvMsg = (TextView) findViewById(R.id.tv_msg);
        tvMsg.setText(ViewUtils.builderColorText(context.getResources().getString(R.string.taskShare_msg),
                context.getResources().getString(R.string.friends),
                context.getResources().getColor(R.color.searchKeyWordColor)
        ));

        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareEvent();
            }
        });
    }

    public interface OnShareComplete {
        void onComplete(Api_ActivityInfo.ObjBean objBean);
    }


    public void setOnShareComplete(OnShareComplete onShareComplete) {
        this.onShareComplete = onShareComplete;
    }

    private void shareEvent() {

    }

}
