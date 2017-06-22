package com.palmap.exhibition.di.module;


import com.palmap.exhibition.di.ActivityScope;
import com.palmap.exhibition.presenter.PalMapViewPresenter;
import com.palmap.exhibition.presenter.PoiInfoPresenter;
import com.palmap.exhibition.presenter.PoiInfoWebViewPresenter;
import com.palmap.exhibition.presenter.PoiSearchPresenter;
import com.palmap.exhibition.presenter.impl.PalMapViewPresenterImpl;
import com.palmap.exhibition.presenter.impl.PoiInfoPresenterImpl;
import com.palmap.exhibition.presenter.impl.PoiInfoWebViewPresenterImpl;
import com.palmap.exhibition.presenter.impl.PoiSearchPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 王天明 on 2016/5/3.
 */
@Module(includes = {FacilityModule.class,FencingModule.class,DelegateModule.class})
@ActivityScope
public class PresenterModule {

    @Provides
    PalMapViewPresenter providesMainPresenter(PalMapViewPresenterImpl palMapPresenter) {
        return palMapPresenter;
    }

    @Provides
    PoiInfoPresenter providesPoiInfoPresenter(PoiInfoPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    PoiSearchPresenter providesPoiSearchPresenter(PoiSearchPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    PoiInfoWebViewPresenter providesPoiInfoWebViewPresenter(PoiInfoWebViewPresenterImpl presenter) {
        return presenter;
    }
}
