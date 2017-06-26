package com.palmap.library.rx.dataSource;

import com.palmap.library.rx.exception.DataSourceStateException;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.MapModel;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by 王天明 on 2016/4/26.
 */
public class DataSourceMapOnSubscribe implements ObservableOnSubscribe<MapModel> {

    private DataSource dataSource;
    private long id;

    public DataSourceMapOnSubscribe(DataSource dataSource, long id) {
        this.dataSource = dataSource;
        this.id = id;
    }

    @Override
    public void subscribe(@NonNull final ObservableEmitter<MapModel> e) throws Exception {
        dataSource.requestMap(id, new DataSource.OnRequestDataEventListener<MapModel>() {
            @Override
            public void onRequestDataEvent(DataSource.ResourceState resourceState, MapModel mapModel) {
                if (!e.isDisposed()) {
                    if (resourceState == DataSource.ResourceState.OK || resourceState == DataSource.ResourceState.CACHE) {
                        e.onNext(mapModel);
                        e.onComplete();
                    } else {
                        e.onError(new DataSourceStateException(resourceState));
                    }
                }
            }
        });
    }
}
