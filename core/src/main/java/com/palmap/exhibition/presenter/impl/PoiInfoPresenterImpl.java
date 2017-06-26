package com.palmap.exhibition.presenter.impl;

import com.palmap.exhibition.BuildConfig;
import com.palmap.exhibition.api.PoiInfoService;
import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.factory.ServiceFactory;
import com.palmap.exhibition.model.Api_PoiModel;
import com.palmap.exhibition.presenter.PoiInfoPresenter;
import com.palmap.exhibition.view.PoiInfoView;
import com.palmap.exhibition.view.impl.PoiInfoViewActivity;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aoc on 2016/6/23.
 */
public class PoiInfoPresenterImpl implements PoiInfoPresenter {

    private PoiInfoView poiInfoView;
    private Disposable subscription;

    @Inject
    public PoiInfoPresenterImpl() {
    }

    @Override
    public void loadPoiInfo(long poiId) {
        if (subscription != null) {
            subscription.dispose();
            subscription = null;
        }
        subscription = ServiceFactory.create(PoiInfoService.class).loadPoiData(poiId, Config.getLanguagePara())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        poiInfoView.showLoading();
                    }
                })
                .subscribe(new Consumer<Api_PoiModel>() {
                    @Override
                    public void accept(Api_PoiModel s) {
                        poiInfoView.hideLoading();
                        if (s.isOk()) {
                            poiInfoView.readPoiData(s);
                        } else {
                            poiInfoView.poiDataError(obtainDefaultApi_PoiModel());
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        poiInfoView.hideLoading();
                        poiInfoView.poiDataError(obtainDefaultApi_PoiModel());
                        if (BuildConfig.DEBUG) {
                            throwable.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void attachView(PoiInfoView poiInfoView) {
        this.poiInfoView = poiInfoView;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        if (subscription != null && subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    private Api_PoiModel obtainDefaultApi_PoiModel(){
        Api_PoiModel api_poiModel = new Api_PoiModel();
        Api_PoiModel.ObjBean objBean = new Api_PoiModel.ObjBean();

        objBean.setAddress("上海市浦东新区浦东南路528号证券大厦南塔21楼");
        objBean.setEmail("business@palmaplus.com");
        objBean.setCompanyName("上海图聚智能科技股份有限公司");
        objBean.setTelephone("400-728-9687");
        objBean.setCompanyURL("http://www.palmap.cn/");
        objBean.setDescription("上海图聚智能科技股份有限公司成立于2012年，是国内最早把室外位置服务延伸到室内环境的公司。图聚创建并维护着国内最大的室内地图数据库，是国内最大的室内地图数据提供商和室内位置服务提供商。");
        objBean.setWechatNumber("Palmap_ExpoAtlas");
        objBean.setWechatName("图聚智能展图");
        objBean.setQrCode(PoiInfoViewActivity.QRCODE_EXBT);

        api_poiModel.setObj(objBean);
        return api_poiModel;
    }

}
