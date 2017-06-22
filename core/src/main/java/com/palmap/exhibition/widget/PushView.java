package com.palmap.exhibition.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.palmap.exhibition.R;
import com.palmap.exhibition.model.PushModel;
import com.palmap.library.utils.DeviceUtils;

/**
 * Created by 王天明 on 2016/9/13.
 */
public class PushView extends PopupWindow {

    private View parentView;
    private Context context;
    private ImageView img_close;
    private ImageView img_logo;
    private TextView tv_name;
    private TextView tv_describe;

    public PushView(View parentView, PushModel pushModel) {
        this.parentView = parentView;
        this.context = parentView.getContext();
        int srceenW = DeviceUtils.getWidth(context);
        setWidth((int) (srceenW * 0.8));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setContentView(initView(pushModel));
    }

    private View initView(PushModel pushModel) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_push,
                null, false);
        img_close = (ImageView) contentView.findViewById(R.id.img_close);
        img_logo = (ImageView) contentView.findViewById(R.id.img_logo);
        tv_name = (TextView) contentView.findViewById(R.id.tv_name);
        tv_describe = (TextView) contentView.findViewById(R.id.tv_describe);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (pushModel != null) {
            tv_name.setText(pushModel.getName());
            tv_describe.setText(pushModel.getDescribe());

            Glide.with(context)
                    .load(pushModel.getLogo())
                    .placeholder(R.mipmap.logo_palmap)
                    .error(R.mipmap.logo_palmap)
                    .into(img_logo);
        }
        return contentView;
    }


    public void showAsCenter() {
        showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }
}
