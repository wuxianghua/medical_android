package com.palmap.exhibition.widget.overlayer;


import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.R;
import com.palmap.exhibition.dao.business.CoordinateBusiness;
import com.palmap.library.view.transform.GlideBlackWhiteTransform;
import com.palmaplus.nagrand.view.MapView;

/**
 * Created by 王天明 on 2016/10/20.
 * 点亮活动专用mark
 */
public class LightEventMark extends BaseMark {

    private int atyId;//活动ID
    private int pointId;//服务器点位ID 唯一表示点位
    private boolean isLight;//是否点亮了

    private ImageView icon;

    private String imgUrl;

    CoordinateBusiness coordinateBusiness;

    private int logoResId;

    public LightEventMark(MapView mapView, long floorId) {
        super(mapView, floorId);

        coordinateBusiness = AndroidApplication.getInstance().getApplicationComponent().coordinateBusiness();

        LayoutInflater.from(mapView.getContext()).inflate(R.layout.layout_light_mark, this);

        icon = (ImageView) findViewById(R.id.img_icon);

    }

    public int getAtyId() {
        return atyId;
    }

    public void setAtyId(int atyId) {
        this.atyId = atyId;
    }

    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
        setSelected(coordinateBusiness.checkNullByPointId("" + pointId));
    }

    public boolean isLight() {
        return isLight;
    }

    public void setLight(boolean light) {
        isLight = light;
    }

    public ImageView getIcon() {
        return icon;
    }

    public void setIcon(ImageView icon) {
        this.icon = icon;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        RequestManager requestManager = Glide.with(getContext());
        DrawableRequestBuilder builder;

        if (TextUtils.isEmpty(imgUrl)) {
            builder = requestManager
                    .load(R.mipmap.ico_task_light);
        } else {
            builder = requestManager.load(this.imgUrl);
        }
        if (!isSelected()) {
            builder = builder.transform(new GlideBlackWhiteTransform(getContext()));
        }
        builder.error(R.mipmap.ico_task_light).into(icon);
    }

    public void setLogoResId(int logoResId) {
        this.logoResId = logoResId;
//        RequestManager requestManager = Glide.with(getContext());
//        DrawableRequestBuilder builder;
//        builder = requestManager
//                .load(this.logoResId);
//        if (!isSelected()) {
//            builder = builder.transform(new GlideBlackWhiteTransform(getContext()));
//        }
//        builder.error(R.mipmap.ico_task_light).into(icon);
        icon.setImageResource(logoResId);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (!TextUtils.isEmpty(imgUrl)) {
            setImgUrl(imgUrl);
        }
    }
}
