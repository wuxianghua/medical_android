package com.palmap.exhibition.presenter;

import com.palmap.exhibition.view.PoiInfoWebView;
import com.palmap.library.presenter.Presenter;

/**
 * Created by aoc on 2016/10/18.
 */

public interface PoiInfoWebViewPresenter  extends Presenter{

    void attachView(PoiInfoWebView view);

    void loadPoiInfo(long poiId);
}
