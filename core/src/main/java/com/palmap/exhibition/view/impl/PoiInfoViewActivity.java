package com.palmap.exhibition.view.impl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.zxing.WriterException;
import com.palmap.exhibition.R;
import com.palmap.exhibition.model.Api_PoiModel;
import com.palmap.exhibition.presenter.PoiInfoPresenter;
import com.palmap.exhibition.view.PoiInfoView;
import com.palmap.exhibition.view.base.ExSwipeBackActivity;
import com.palmap.exhibition.widget.PhonePopWindow;
import com.palmap.exhibition.widget.WebSitePopWindow;
import com.palmap.exhibition.widget.imgslider.Animations.DescriptionAnimation;
import com.palmap.exhibition.widget.imgslider.SliderLayout;
import com.palmap.exhibition.widget.imgslider.SliderTypes.BaseSliderView;
import com.palmap.exhibition.widget.imgslider.SliderTypes.DefaultSliderView;
import com.palmap.library.utils.ActivityUtils;
import com.palmap.library.utils.DeviceUtils;
import com.palmap.library.utils.LogUtil;
import com.palmap.zxing.encoding.EncodingHandler;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PoiInfoViewActivity extends ExSwipeBackActivity<PoiInfoPresenter> implements PoiInfoView {

    public static final String QRCODE_EXBT = "qrcode_exbt";

    TextView tvTitle;
    TextView poiInfoCompanyName;
    TextView poiInfoTelephony;
    TextView poiInfoEmail;
    TextView poiInfoNetwork;
    TextView poiInfoAddress;
    TextView poiInfoDescription;
    TextView tvPoiErrorMsg;
    TextView poiInfoWeixinName;
    TextView poiInfoWeixinNumber;

    Toolbar toolBar;
    ScrollView scrollView;
    WebView webView;
    ImageView img_adColumn;
    SliderLayout slider;
    ImageView poiInfoQrCode;
    RelativeLayout layoutSorry;
    ViewGroup layout_back;
    ViewGroup layout_search;

    private void bindViews() {
        tvTitle = findView(R.id.tv_title);
        layout_back = findView(R.id.layout_back);
        layout_search = findView(R.id.layout_search);
        poiInfoCompanyName = findView(R.id.poi_info_company_name);
        poiInfoTelephony = findView(R.id.poi_info_telephony);
        poiInfoEmail = findView(R.id.poi_info_email);
        poiInfoNetwork = findView(R.id.poi_info_network);
        poiInfoAddress = findView(R.id.poi_info_address);
        poiInfoDescription = findView(R.id.poi_info_description);
        poiInfoQrCode = findView(R.id.poi_info_qr_code);
        poiInfoWeixinName = findView(R.id.poi_info_weixin_name);
        poiInfoWeixinNumber = findView(R.id.poi_info_weixin_number);
        toolBar = findView(R.id.toolBar);
        scrollView = findView(R.id.scrollView);
        webView = findView(R.id.webView);
        img_adColumn = findView(R.id.img_adColumn);
        slider = findView(R.id.slider);
        tvPoiErrorMsg = findView(R.id.tv_poiErrorMsg);
        layoutSorry = findView(R.id.layout_sorry);


        poiInfoTelephony.setOnClickListener(this);
        poiInfoNetwork.setOnClickListener(this);
        layout_back.setOnClickListener(this);
    }


    @Inject
    PoiInfoPresenter presenter;

    private static final String KEY_POI_ID = "key_poi_id";
    private static final String KEY_POI_NAME = "key_poi_name";


    private View rootView;

    private long poiId = -1;
    private String poiName;

    private Api_PoiModel poiModel;

    public static void navigatorThis(Activity that, long poiId, String poiName) {
        Intent intent = new Intent(that, PoiInfoViewActivity.class);
        intent.putExtra(KEY_POI_ID, poiId);
        intent.putExtra(KEY_POI_NAME, poiName);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_info);
//        ButterKnife.bind(this);
        bindViews();
        setSupportActionBar(toolBar);
        initStatusBar(R.color.ngr_colorPrimary);
        layout_search.setVisibility(View.GONE);
        try {
            Intent valueIntent = getIntent();
            if (Intent.ACTION_VIEW.equals(valueIntent.getAction())) {
                Uri uri = valueIntent.getData();
                if (uri != null) {
                    poiId = Long.parseLong(uri.getQueryParameter(KEY_POI_ID));
                    poiName = uri.getQueryParameter(KEY_POI_NAME);
                }
            } else {
                poiId = getIntent().getExtras().getLong(KEY_POI_ID, -1);
                poiName = getIntent().getExtras().getString(KEY_POI_NAME);
            }
            LogUtil.d("poiId:" + poiId);
            if (poiId == -1) {
                //poiDataError();
                getRootView().setVisibility(View.INVISIBLE);
                return;
            }

            tvTitle.setText(poiName);
            presenter.attachView(this);
            presenter.loadPoiInfo(poiId);

        } catch (Exception e) {
            //error
            //poiDataError();
            e.printStackTrace();
            getRootView().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected PoiInfoPresenter inject() {
        daggerInject().inject(this);
        return presenter;
    }

    @Override
    public void readPoiData(Api_PoiModel poiModel) {
        if (poiModel == null) return;
        this.poiModel = poiModel;
        // TODO: 2016/6/28 该poi没有公司网址 使用自己的样式显示
        if (TextUtils.isEmpty(poiModel.getObj().getRedirectURL())) {
            scrollView.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            poiInfoCompanyName.setText(poiModel.getObj().getCompanyName());
            poiInfoTelephony.setText(poiModel.getObj().getTelephone());
            poiInfoEmail.setText(poiModel.getObj().getEmail());
            poiInfoNetwork.setText(poiModel.getObj().getCompanyURL());
            poiInfoAddress.setText(poiModel.getObj().getAddress());
            poiInfoDescription.setText("　　" + poiModel.getObj().getDescription());
            poiInfoWeixinNumber.setText(poiModel.getObj().getWechatNumber());
//            poiInfoWeixinName.setText(poiModel.getObj().getCompanyName());
            poiInfoWeixinName.setText(poiModel.getObj().getWechatName());

            String qrcode = poiModel.getObj().getQrCode();
//            loadQRCode(qrcode, 80);
            if (!TextUtils.isEmpty(qrcode)) {
                if (QRCODE_EXBT.equals(qrcode)) {
                    poiInfoQrCode.setImageResource(R.mipmap.exbt_qrcode);
                } else {
                    Glide.with(this).load(qrcode).into(poiInfoQrCode);
                }
            }

            List<String> listAdColumns = poiModel.getObj().getAdList();
            if (listAdColumns != null) {
                if (listAdColumns.size() == 1) {
                    img_adColumn.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(listAdColumns.get(0))
                            .into(img_adColumn);
                    //img_adColumn
                    return;
                }
                for (int i = 0; i < listAdColumns.size(); i++) {
                    if (slider.getVisibility() == View.GONE) {
                        slider.setVisibility(View.VISIBLE);
                        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                        slider.setCustomAnimation(new DescriptionAnimation());
                    }
                    String imgUrl = listAdColumns.get(i);
                    DefaultSliderView sliderView = new DefaultSliderView(getContext());
                    sliderView.image(imgUrl)
                            .error(R.mipmap.ic_launcher)
                            .setScaleType(BaseSliderView.ScaleType.CenterCrop);
                    slider.addSlider(sliderView);
                }
            }
        } else {
            //使用weiView显示网址即可
            webView.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            loadWebView(poiModel.getObj().getRedirectURL());
        }
    }

    private void loadQRCode(final String code, final int widthHeight) {
        if (TextUtils.isEmpty(code)) {
            return;
        }
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Bitmap> emitter) throws Exception {
                try {
                    emitter.onNext(EncodingHandler.createQRCode(code, DeviceUtils.dip2px(getContext(), widthHeight)));
                    emitter.onComplete();
                } catch (WriterException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(@NonNull Bitmap bitmap) throws Exception {
                        poiInfoQrCode.setImageBitmap(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        showErrorMessage("二维码加载失败");
                    }
                });
    }

    private void loadWebView(String url) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(false);
        settings.setGeolocationEnabled(true);
        settings.setSaveFormData(true);
        settings.setSavePassword(true);
        settings.setTextZoom(100);
        int minimumFontSize = 1;
        settings.setMinimumFontSize(minimumFontSize);
        settings.setMinimumLogicalFontSize(minimumFontSize);
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(false);
        settings.setBuiltInZoomControls(true);
        settings.setSupportMultipleWindows(false);
        settings.setEnableSmoothTransition(true);
        // HTML5 API flags
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        // HTML5 configuration settings.
        settings.setAppCacheMaxSize(3 * 1024 * 1024);
        settings.setAppCachePath(getDir("appcache", 0).getPath());
        settings.setDatabasePath(getDir("databases", 0).getPath());
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setGeolocationDatabasePath(getDir("geolocation",
                0).getPath());
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webView.getVisibility() == View.VISIBLE && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void poiDataError(Api_PoiModel poiModel) {
//        showErrorMessage("获取poi详情失败！！");
        readPoiData(poiModel);
        tvPoiErrorMsg.setVisibility(View.VISIBLE);
        layoutSorry.setVisibility(View.VISIBLE);
    }

    void phoneClick() {
        showPopWindow(new PhonePopWindow(getContext(), poiModel));
    }

    void websiteClick() {
        showPopWindow(new WebSitePopWindow(getContext(), poiModel));
    }

    private void showPopWindow(PopupWindow popupWindow) {
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ActivityUtils.alphaBackground(PoiInfoViewActivity.this, 1f);
            }
        });
        popupWindow.showAtLocation(getRootView(), Gravity.BOTTOM, 0, 0);
        ActivityUtils.alphaBackground(PoiInfoViewActivity.this, 0.6f);
    }

    public void backClick() {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.poi_info_telephony) {
            phoneClick();
            return;
        }

        if (id == R.id.poi_info_network) {
            websiteClick();
            return;
        }

        if (id == R.id.layout_back) {
            backClick();
            return;
        }

    }

    @Override
    public View getRootView() {
        if (rootView == null) {
            rootView = super.getRootView();
        }
        return rootView;
    }

}
