package com.palmap.exhibition.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.palmap.exhibition.R;
import com.palmap.library.utils.DeviceUtils;

/**
 * Created by 王天明 on 2016/11/21.
 */
public class WebDialog extends Dialog {

    private ImageView img_close;
    private WebView webView;

    public WebDialog(Context context, View.OnClickListener shareListener) {
        super(context, R.style.dialog_task);
        setContentView(R.layout.dialog_web);
        bindView();
        findViewById(R.id.btn_share).setOnClickListener(shareListener);
    }

    private void bindView() {
        img_close = (ImageView) findViewById(R.id.img_close);
        webView = (WebView) findViewById(R.id.webView);
        configWebView(webView);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.height =(int) (DeviceUtils.getHeight(getContext()) * 0.6f);
        this.getWindow().setAttributes(params);




        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setBuiltInZoomControls(false); // 设置支持缩放
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebView.HitTestResult hit = view.getHitTestResult();
                if (hit != null) {
                    int hitType = hit.getType();
                    if (hitType == WebView.HitTestResult.SRC_ANCHOR_TYPE
                            || hitType == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {// 点击超链接
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        getContext().startActivity(i);
                    } else {
                        view.loadUrl(url);
                    }
                } else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
    }

    public void loadWebUrl(String url){
        webView.loadUrl(url);
    }

    public void loadData(String data){
//        webView.loadData(data,"text/html", "utf-8");
        webView.loadDataWithBaseURL(null,data,"text/html", "utf-8",null);
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
        settings.setAppCachePath(getContext().getDir("appcache", 0).getPath());
        settings.setDatabasePath(getContext().getDir("databases", 0).getPath());
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setGeolocationDatabasePath(getContext().getDir("geolocation",
                0).getPath());
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (webView != null) {
            webView.destroy();
        }
    }
}