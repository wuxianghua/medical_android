package com.palmap.library.rx.dataSource;

import com.palmap.library.rx.exception.DataSourceStateException;
import com.palmap.library.rx.exception.MapDataNullException;
import com.palmaplus.nagrand.data.DataList;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.MapModel;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by 王天明 on 2016/4/26.
 */
public class DataSourceMapsOnSubscribe implements Observable.OnSubscribe<DataList<MapModel>> {

    private DataSource dataSource;

    public DataSourceMapsOnSubscribe(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void call(final Subscriber<? super DataList<MapModel>> subscriber) {
        dataSource.requestMaps(new DataSource.OnRequestDataEventListener<DataList<MapModel>>() {
            @Override
            public void onRequestDataEvent(DataSource.ResourceState resourceState, DataList<MapModel> data) {
                if (!subscriber.isUnsubscribed()) {
                    if (resourceState == DataSource.ResourceState.OK|| resourceState == DataSource.ResourceState.CACHE) {
                        if (data.getSize() == 0) {
                            subscriber.onError(new MapDataNullException());
                            return;
                        }
                        subscriber.onNext(data);
                    } else {
                        subscriber.onError(new DataSourceStateException(resourceState));
                    }
                }
            }
        });
    }


}
