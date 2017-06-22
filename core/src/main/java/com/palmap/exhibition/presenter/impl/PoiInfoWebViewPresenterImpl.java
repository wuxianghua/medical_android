package com.palmap.exhibition.presenter.impl;

import com.palmap.exhibition.presenter.PoiInfoWebViewPresenter;
import com.palmap.exhibition.view.PoiInfoWebView;

import javax.inject.Inject;

/**
 * Created by 王天明 on 2016/10/18.
 */
public class PoiInfoWebViewPresenterImpl implements PoiInfoWebViewPresenter {

    private PoiInfoWebView poiInfoWebView;

    @Inject
    public PoiInfoWebViewPresenterImpl() {
    }


    @Override
    public void attachView(PoiInfoWebView view) {
        poiInfoWebView = view;
    }

    @Override
    public void loadPoiInfo(long poiId) {
        poiInfoWebView.loadDataWithPoiId(poiId);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
