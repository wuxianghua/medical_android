package com.palmap.exhibition.di.module;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.palmap.exhibition.di.ActivityScope;
import com.palmap.exhibition.model.AreaModel;
import com.palmap.library.base.BaseActivity;
import com.palmap.library.geoFencing.GeoFencing;
import com.palmap.library.geoFencing.GeoFencingManager;
import com.palmap.library.geoFencing.GeoFencingProvides;
import com.palmap.library.geoFencing.impl.RectGeoFencing;
import com.palmap.library.utils.FileUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 王天明 on 2016/9/9.
 */
@Module
public class FencingModule {

    @Provides
    @ActivityScope
    public GeoFencingManager<AreaModel> providesGeoFencingManager(GeoFencingProvides<AreaModel> provides) {
        GeoFencingManager<AreaModel> geoFencingManager = new GeoFencingManager<>();
        geoFencingManager.setGeoFencingProvides(provides);
        return geoFencingManager;
    }

    @Provides
    @ActivityScope
    public GeoFencingProvides<AreaModel> providesGeoFencingProvides(final BaseActivity activity) {
        return new GeoFencingProvides<AreaModel>() {
            @Override
            public Observable<List<GeoFencing<AreaModel>>> providesGeoFencing() {
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                        String jsonStr = FileUtils.readFileFromAssets(activity, "json/AreaFencing.json");
                        if (TextUtils.isEmpty(jsonStr)) {
                            e.onError(new NullPointerException("json file error"));
                        }else{
                            e.onNext(jsonStr);
                            e.onComplete();
                        }
                    }
                }).map(new Function<String, List<AreaModel>>() {
                    @Override
                    public List<AreaModel> apply(@NonNull String s) throws Exception {
                        Type listType = new TypeToken<List<AreaModel>>() {}.getType();
                        return new Gson().fromJson(s, listType);
                    }
                }).subscribeOn(Schedulers.io()).flatMap(new Function<List<AreaModel>, Observable<List<GeoFencing<AreaModel>>>>() {
                    @Override
                    public Observable<List<GeoFencing<AreaModel>>> apply(@NonNull final List<AreaModel> areaModels) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<GeoFencing<AreaModel>>>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<List<GeoFencing<AreaModel>>> e) throws Exception {
                                List<GeoFencing<AreaModel>> result = new ArrayList<>();
                                for (final AreaModel areaModel : areaModels) {
                                    GeoFencing<AreaModel> temp = new RectGeoFencing(
                                            areaModel.getFloorId(),
                                            areaModel.getTopLeft(),
                                            areaModel.getBottomRight()) {
                                        @Override
                                        public AreaModel obtainGeoData() {
                                            return areaModel;
                                        }
                                    };
                                    result.add(temp);
                                }
                                e.onNext(result);
                                e.onComplete();
                            }
                        });
                    }
                });
            }
        };
    }

}
