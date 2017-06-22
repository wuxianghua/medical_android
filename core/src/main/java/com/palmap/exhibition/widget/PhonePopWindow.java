package com.palmap.exhibition.widget;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
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
public class PhonePopWindow extends PopupWindow implements View.OnClickListener{

    private Api_PoiModel model;
    private Context context;
    private LoadDataView loadDataView;

    public PhonePopWindow(Context context, Api_PoiModel model) {
        super(context);
        this.context = context;
        this.model = model;
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_phone, null, false);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.popwin_anim_style);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);

        contentView.findViewById(R.id.btn_call).setOnClickListener(this);
        contentView.findViewById(R.id.btn_copy).setOnClickListener(this);
        contentView.findViewById(R.id.btn_saveAsMailList).setOnClickListener(this);
        contentView.findViewById(R.id.btn_cancel).setOnClickListener(this);
        if (context instanceof  LoadDataView) loadDataView = (LoadDataView) context;
    }

    void callClick(View v) {
        Uri uri = Uri.parse("tel:" + model.getObj().getTelephone());
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(it);
    }

    void copyClick(View v) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(model.getObj().getTelephone());
        if (loadDataView != null) {
            loadDataView.showMessage("电话号码已拷贝");
        }
    }

    void saveAsMailListClick(View v) {
        Intent it = new Intent(Intent.ACTION_INSERT, Uri.withAppendedPath(
                Uri.parse("content://com.android.contacts"), "contacts"));
        it.setType("vnd.android.cursor.dir/person");
        // 公司
        it.putExtra(ContactsContract.Intents.Insert.COMPANY,
                model.getObj().getCompanyName());
        // email
        it.putExtra(ContactsContract.Intents.Insert.EMAIL,
                model.getObj().getEmail());
        // 手机号码
        it.putExtra(ContactsContract.Intents.Insert.PHONE,
                model.getObj().getTelephone());
        // 备注信息
        it.putExtra(ContactsContract.Intents.Insert.JOB_TITLE,
                "地址:"+model.getObj().getAddress());
        context.startActivity(it);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_call) {
            callClick(v);

        } else if (i == R.id.btn_copy) {
            copyClick(v);

        } else if (i == R.id.btn_saveAsMailList) {
            saveAsMailListClick(v);

//            case R.id.btn_cancel:
//
//                break;
        }
        dismiss();
    }
}
