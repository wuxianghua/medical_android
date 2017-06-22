package com.palmap.library.rx.dataSource;

import com.palmap.library.rx.exception.DataSourceStateException;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.MapModel;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by 王天明 on 2016/4/26.
 */
public class DataSourceMapOnSubscribe implements Observable.OnSubscribe<MapModel> {

    private DataSource dataSource;
    private long id;

    public DataSourceMapOnSubscribe(DataSource dataSource, long id) {
        this.dataSource = dataSource;
        this.id = id;
    }

    @Override
    public void call(final Subscriber<? super MapModel> subscriber) {
        dataSource.requestMap(id, new DataSource.OnRequestDataEventListener<MapModel>() {
            @Override
            public void onRequestDataEvent(DataSource.ResourceState resourceState, MapModel mapModel) {
                if (!subscriber.isUnsubscribed()) {
                    if (resourceState == DataSource.ResourceState.OK || resourceState == DataSource.ResourceState.CACHE) {
                        subscriber.onNext(mapModel);
                    } else {
                        subscriber.onError(new DataSourceStateException(resourceState));
                    }
                }
            }
        });
    }
}
