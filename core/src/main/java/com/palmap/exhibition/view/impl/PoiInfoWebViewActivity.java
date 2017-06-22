package com.palmap.exhibition.view.impl;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.Api_PoiModel;
import com.palmap.exhibition.presenter.PoiInfoWebViewPresenter;
import com.palmap.exhibition.view.PoiInfoWebView;
import com.palmap.exhibition.view.base.ExActivity;
import com.palmap.exhibition.widget.PhonePopWindow;
import com.palmap.exhibition.widget.WebSitePopWindow;
import com.palmap.library.utils.ActivityUtils;
import com.palmap.library.utils.LogUtil;

import javax.inject.Inject;

public class PoiInfoWebViewActivity extends ExActivity<PoiInfoWebViewPresenter> implements PoiInfoWebView {

    //private static final String URL_POI = "http://expo.palmap.cn/h5/bobo/detailshop.html?shopsId=";
    private static final String URL_POI = "http://expo.palmap.cn/h5/zhantu/detailshop.html?shopsId=";
    private static final String KEY_POI_ID = "key_poi_id";
    private static final String KEY_POI_NAME = "key_poi_name";

    public static void navigatorThis(Activity that, long poiId, String poiName) {
        Intent intent = new Intent(that, PoiInfoWebViewActivity.class);
        intent.putExtra(KEY_POI_ID, poiId);
        intent.putExtra(KEY_POI_NAME, poiName);
        that.startActivity(intent);
    }

    @Inject
    PoiInfoWebViewPresenter presenter;
    TextView tvTitle;
    WebView webView;
    Toolbar toolbar;
    private long poiId = -1;
    private String poiName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_info_web_view);
        bindView();
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

    private void bindView() {
        findView(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvTitle = findView(R.id.tv_title);
        webView = findView(R.id.webView);
        configWebView(webView);
        findViewById(R.id.layout_search).setVisibility(View.GONE);
        toolbar = findView(R.id.toolBar);
        setSupportActionBar(toolbar);
        initStatusBar(R.color.ngr_colorPrimary);
    }

    private void configWebView(WebView webView) {
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

        webView.addJavascriptInterface(this, "androidContext");
    }

    @JavascriptInterface
    public void callPhoneViewOnHtml(final String tel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Api_PoiModel api_poiModel = new Api_PoiModel();
                Api_PoiModel.ObjBean objBean = new Api_PoiModel.ObjBean();
                objBean.setTelephone(tel);
                api_poiModel.setObj(objBean);
                showPopWindow(new PhonePopWindow(getContext(), api_poiModel));
            }
        });
    }

    @JavascriptInterface
    public void callWebSiteViewOnHtml(final String tel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Api_PoiModel api_poiModel = new Api_PoiModel();
                Api_PoiModel.ObjBean objBean = new Api_PoiModel.ObjBean();
                objBean.setCompanyURL(tel);
                api_poiModel.setObj(objBean);
                showPopWindow(new WebSitePopWindow(getContext(), api_poiModel));
            }
        });
    }


    private void showPopWindow(PopupWindow popupWindow) {
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ActivityUtils.alphaBackground(PoiInfoWebViewActivity.this, 1f);
            }
        });
        popupWindow.showAtLocation(getRootView(), Gravity.BOTTOM, 0, 0);
        ActivityUtils.alphaBackground(PoiInfoWebViewActivity.this, 0.6f);
    }

    @Override
    protected PoiInfoWebViewPresenter inject() {
        daggerInject().inject(this);
        return presenter;
    }

    @Override
    public void loadUrlWithWebView(String url) {
        webView.loadUrl(url);
    }

    @Override
    public void loadDataWithPoiId(long poiId) {
        String url = URL_POI + poiId;
        LogUtil.e("加载poi url:" + url);
        webView.loadUrl(url);
//        webView.loadUrl("http://10.0.10.192:8080/test/android");
//        webView.loadUrl("http://expo.palmap.cn/static/appShareTest/android.html");
//        callH5();
    }

    /**
     * 测试android native调启H5方法
     */
    private void callH5(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:androidCallH5("+System.currentTimeMillis()+")");
                callH5();
            }
        },5000);
    }

    @Override
    public void onBackPressed() {
        if (webView.getVisibility() == View.VISIBLE && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
