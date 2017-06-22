package com.palmap.library.rx.dataSource;

import com.palmap.library.rx.exception.DataSourceStateException;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.PlanarGraph;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by 王天明 on 2016/4/26.
 */
class DataSourcePlanarGraphOnSubscribe implements Observable.OnSubscribe<PlanarGraph> {

    private DataSource dataSource;
    private long floorId;

    public DataSourcePlanarGraphOnSubscribe(DataSource dataSource, long floorId) {
        this.dataSource = dataSource;
        this.floorId = floorId;
    }

    @Override
    public void call(final Subscriber<? super PlanarGraph> subscriber) {
        if (!subscriber.isUnsubscribed()) {
            LogUtil.d("PlanarGraphId:" + floorId);
            dataSource.requestPlanarGraph(floorId, new DataSource.OnRequestDataEventListener<PlanarGraph>() {
                @Override
                public void onRequestDataEvent(DataSource.ResourceState resourceState, PlanarGraph planarGraph) {
                    if (resourceState == DataSource.ResourceState.OK || resourceState == DataSource.ResourceState.CACHE) {
                        subscriber.onNext(planarGraph);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new DataSourceStateException(resourceState));
                    }
                }
            });
        }
    }
}
