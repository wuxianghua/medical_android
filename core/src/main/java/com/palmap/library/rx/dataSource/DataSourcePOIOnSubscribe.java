package com.palmap.library.rx.dataSource;

import com.palmap.library.rx.exception.DataSourceStateException;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.LocationModel;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by 王天明 on 2016/5/11.
 */
public class DataSourcePOIOnSubscribe implements Observable.OnSubscribe<LocationModel> {


    private DataSource dataSource;
    private long id;

    public DataSourcePOIOnSubscribe(DataSource dataSource,long id) {
        this.dataSource = dataSource;
        this.id = id;
        LogUtil.d("mapId:" + id);
    }

    @Override
    public void call(final Subscriber<? super LocationModel> subscriber) {
        dataSource.requestPOI(id, new DataSource.OnRequestDataEventListener<LocationModel>() {
            @Override
            public void onRequestDataEvent(DataSource.ResourceState state, LocationModel locationModel) {
                if (!subscriber.isUnsubscribed()) {
                    if (state == DataSource.ResourceState.OK|| state == DataSource.ResourceState.CACHE) {
                        subscriber.onNext(locationModel);
                    } else {
                        subscriber.onError(new DataSourceStateException(state));
                    }
                }
            }
        });
    }

}
