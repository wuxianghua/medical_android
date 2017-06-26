package com.palmap.library.rx.dataSource;

import com.palmap.library.rx.exception.DataSourceStateException;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.LocationModel;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by 王天明 on 2016/5/11.
 */
public class DataSourcePOIOnSubscribe implements ObservableOnSubscribe<LocationModel> {


    private DataSource dataSource;
    private long id;

    public DataSourcePOIOnSubscribe(DataSource dataSource,long id) {
        this.dataSource = dataSource;
        this.id = id;
        LogUtil.d("mapId:" + id);
    }

    @Override
    public void subscribe(@NonNull final ObservableEmitter<LocationModel> e) throws Exception {
        dataSource.requestPOI(id, new DataSource.OnRequestDataEventListener<LocationModel>() {
            @Override
            public void onRequestDataEvent(DataSource.ResourceState state, LocationModel locationModel) {
                if (!e.isDisposed()) {
                    if (state == DataSource.ResourceState.OK|| state == DataSource.ResourceState.CACHE) {
                        e.onNext(locationModel);
                        e.onComplete();
                    } else {
                        e.onError(new DataSourceStateException(state));
                    }
                }
            }
        });
    }
}
