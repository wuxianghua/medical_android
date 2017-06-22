package com.palmap.exhibition.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.palmap.exhibition.R;

/**
 * Created by 王天明 on 2017/2/16.
 */

public class ChangeFloorAnimDialog extends Dialog {

    private ImageView img_anim;

    public ChangeFloorAnimDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_change_floor_anim);
        img_anim = (ImageView) findViewById(R.id.img_anim);
    }

    @Override
    public void show() {
        super.show();
        AnimationDrawable animationDrawable = (AnimationDrawable) img_anim.getDrawable();
        animationDrawable.start();
    }
}
