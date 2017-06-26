package com.palmap.library.rx.dataSource;

import com.palmap.library.rx.exception.DataSourceStateException;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.LocationList;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by 王天明 on 2016/4/26.
 */
public class DataSourcePOIChildrenOnSubscribe implements ObservableOnSubscribe<LocationList> {

    private DataSource dataSource;
    private long id;

    public DataSourcePOIChildrenOnSubscribe(DataSource dataSource,long id) {
        this.dataSource = dataSource;
        this.id = id;
    }

    public void subscribe(@NonNull final ObservableEmitter<LocationList> e) throws Exception {
        dataSource.requestPOIChildren(id, new DataSource.OnRequestDataEventListener<LocationList>() {
            @Override
            public void onRequestDataEvent(DataSource.ResourceState state, LocationList data) {
                if (!e.isDisposed()) {
                    if (state == DataSource.ResourceState.OK|| state == DataSource.ResourceState.CACHE) {
                        e.onNext(data);
                        e.onComplete();
                    } else {
                        e.onError(new DataSourceStateException(state));
                    }
                }
            }
        });
    }
}
