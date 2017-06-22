package com.palmap.exhibition.widget;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.Api_PoiModel;
import com.palmap.library.view.LoadDataView;

/**
 * Created by 天明 on 2016/6/28.
 */
public class WebSitePopWindow extends PopupWindow implements View.OnClickListener{

    private Api_PoiModel model;
    private Context context;
    private LoadDataView loadDataView;

    public WebSitePopWindow(Context context, Api_PoiModel model) {
        super(context);
        this.context = context;
        this.model = model;
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_website, null, false);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.popwin_anim_style);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);

        contentView.findViewById(R.id.btn_jump_web).setOnClickListener(this);
        contentView.findViewById(R.id.btn_copy).setOnClickListener(this);
        contentView.findViewById(R.id.btn_cancel).setOnClickListener(this);
        if (context instanceof  LoadDataView) loadDataView = (LoadDataView) context;
    }

    void jumpWeb(String url) {
        try {
            if (!url.startsWith("http://")) {
                url = "http://" + url;
            }
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(uri);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void copyClick(View v) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(model.toString());
        if (loadDataView != null) {
            loadDataView.showMessage("网址已拷贝");
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_jump_web) {
            jumpWeb(model.getObj().getCompanyURL());
        } else if (i == R.id.btn_copy) {
            copyClick(v);
        }
        dismiss();
    }
}
