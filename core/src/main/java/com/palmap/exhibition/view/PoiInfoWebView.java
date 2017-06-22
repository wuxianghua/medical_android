package com.palmap.exhibition.view;

/**
 * Created by 王天明 on 2016/10/18.
 * 用单纯的webView显示poi详情信息
 */
public interface PoiInfoWebView {

    void loadUrlWithWebView(String url);

    void loadDataWithPoiId(long poiId);
}