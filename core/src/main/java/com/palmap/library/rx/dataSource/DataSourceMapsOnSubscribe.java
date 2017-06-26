package com.palmap.library.rx.dataSource;

import com.palmap.library.rx.exception.DataSourceStateException;
import com.palmap.library.rx.exception.MapDataNullException;
import com.palmaplus.nagrand.data.DataList;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.MapModel;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;


/**
 * Created by 王天明 on 2016/4/26.
 */
public class DataSourceMapsOnSubscribe implements ObservableOnSubscribe<DataList<MapModel>> {

    private DataSource dataSource;

    public DataSourceMapsOnSubscribe(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void subscribe(@NonNull final ObservableEmitter<DataList<MapModel>> e) throws Exception {
        dataSource.requestMaps(new DataSource.OnRequestDataEventListener<DataList<MapModel>>() {
            @Override
            public void onRequestDataEvent(DataSource.ResourceState resourceState, DataList<MapModel> data) {
                if (!e.isDisposed()) {
                    if (resourceState == DataSource.ResourceState.OK|| resourceState == DataSource.ResourceState.CACHE) {
                        if (data.getSize() == 0) {
                            e.onError(new MapDataNullException());
                            return;
                        }
                        e.onNext(data);
                        e.onComplete();
                    } else {
                        e.onError(new DataSourceStateException(resourceState));
                    }
                }
            }
        });
    }
}
