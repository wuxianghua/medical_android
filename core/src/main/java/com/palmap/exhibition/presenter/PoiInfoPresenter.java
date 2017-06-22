package com.palmap.exhibition.presenter;

import com.palmap.exhibition.view.PoiInfoView;
import com.palmap.library.presenter.Presenter;

/**
 * Created by 王天明 on 2016/6/23.
 */
public interface PoiInfoPresenter extends Presenter {

    void attachView(PoiInfoView poiInfoView);

    void loadPoiInfo(long poiId);
}
