package com.palmap.library.rx.dataSource;

import com.palmap.library.rx.exception.DataSourceStateException;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.PlanarGraph;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by 王天明 on 2016/4/26.
 */
class DataSourcePlanarGraphOnSubscribe implements ObservableOnSubscribe<PlanarGraph> {

    private DataSource dataSource;
    private long floorId;

    public DataSourcePlanarGraphOnSubscribe(DataSource dataSource, long floorId) {
        this.dataSource = dataSource;
        this.floorId = floorId;
    }


    @Override
    public void subscribe(final ObservableEmitter<PlanarGraph> e) throws Exception {
        if (!e.isDisposed()) {
            LogUtil.d("PlanarGraphId:" + floorId);
            dataSource.requestPlanarGraph(floorId, new DataSource.OnRequestDataEventListener<PlanarGraph>() {
                @Override
                public void onRequestDataEvent(DataSource.ResourceState resourceState, PlanarGraph planarGraph) {
                    if (resourceState == DataSource.ResourceState.OK || resourceState == DataSource.ResourceState.CACHE) {
                        e.onNext(planarGraph);
                        e.onComplete();
                    } else {
                        e.onError(new DataSourceStateException(resourceState));
                    }
                }
            });
        }
    }
}
