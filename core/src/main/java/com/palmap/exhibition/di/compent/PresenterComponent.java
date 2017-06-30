package com.palmap.exhibition.di.compent;

import com.palmap.exhibition.di.ActivityScope;
import com.palmap.exhibition.di.module.ActivityModule;
import com.palmap.exhibition.di.module.LocationModule;
import com.palmap.exhibition.di.module.PresenterModule;
import com.palmap.exhibition.view.impl.DestinationSearchActivity;
import com.palmap.exhibition.view.impl.PalmapViewActivity;

import dagger.Component;

/**
 * Created by 王天明 on 2015/12/21 0021.
 */
@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, PresenterModule.class, LocationModule.class})
public interface PresenterComponent extends ActivityComponent {

    void inject(PalmapViewActivity activity);

    void inject(DestinationSearchActivity poiSearchViewActivity);
}