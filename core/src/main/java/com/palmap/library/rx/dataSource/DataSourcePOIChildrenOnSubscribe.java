package com.palmap.library.rx.dataSource;

import com.palmap.library.rx.exception.DataSourceStateException;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.LocationList;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by 王天明 on 2016/4/26.
 */
public class DataSourcePOIChildrenOnSubscribe implements Observable.OnSubscribe<LocationList> {

    private DataSource dataSource;
    private long id;

    public DataSourcePOIChildrenOnSubscribe(DataSource dataSource,long id) {
        this.dataSource = dataSource;
        this.id = id;
    }

    @Override
    public void call(final Subscriber<? super LocationList> subscriber) {
        dataSource.requestPOIChildren(id, new DataSource.OnRequestDataEventListener<LocationList>() {
            @Override
            public void onRequestDataEvent(DataSource.ResourceState state, LocationList data) {
                if (!subscriber.isUnsubscribed()) {
                    if (state == DataSource.ResourceState.OK|| state == DataSource.ResourceState.CACHE) {
//                        if (data.getSize() == 0) {
//                            subscriber.onError(new MapDataNullException());
//                            return;
//                        }
                        subscriber.onNext(data);
                    } else {
                        subscriber.onError(new DataSourceStateException(state));
                    }
                }
            }
        });
    }
}
