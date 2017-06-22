package com.palmap.exhibition.di.compent;

import com.palmap.exhibition.di.ActivityScope;
import com.palmap.exhibition.di.module.ActivityModule;
import com.palmap.exhibition.di.module.LocationModule;
import com.palmap.exhibition.di.module.PresenterModule;
import com.palmap.exhibition.view.impl.PalmapViewActivity;
import com.palmap.exhibition.view.impl.PoiInfoViewActivity;
import com.palmap.exhibition.view.impl.PoiInfoWebViewActivity;
import com.palmap.exhibition.view.impl.PoiSearchViewActivity;

import dagger.Component;

/**
 * Created by 王天明 on 2015/12/21 0021.
 */
@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, PresenterModule.class, LocationModule.class})
public interface PresenterComponent extends ActivityComponent {

    void inject(PalmapViewActivity activity);

    void inject(PoiInfoViewActivity activity);

    void inject(PoiInfoWebViewActivity activity);

    void inject(PoiSearchViewActivity poiSearchViewActivity);
}