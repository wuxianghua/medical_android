package com.palmap.library.rx.dataSource;

import com.palmap.library.rx.exception.DataSourceStateException;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.LocationPagingList;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by 王天明 on 2016/5/11.
 */
public class DataSourceSearchOnSubscribe implements ObservableOnSubscribe<LocationPagingList> {

    private DataSource dataSource;
    private String keyWord;
    private int start;
    private int number;

    private long[] parents,categories;

    public DataSourceSearchOnSubscribe(DataSource dataSource, String keyWord, int start, int number, long[] parents, long[] categories) {
        this.dataSource = dataSource;
        this.keyWord = keyWord;
        this.start = start;
        this.number = number;
        this.parents = parents;
        this.categories = categories;
    }

    @Override
    public void subscribe(final ObservableEmitter<LocationPagingList> e) throws Exception {
        dataSource.search(keyWord,start,number,parents,categories, new DataSource.OnRequestDataEventListener<LocationPagingList>() {
            @Override
            public void onRequestDataEvent(DataSource.ResourceState state, LocationPagingList data) {
                if (!e.isDisposed()) {
                    if (state == DataSource.ResourceState.OK|| state == DataSource.ResourceState.CACHE) {
                        e.onNext(data);
                    } else {
                        e.onError(new DataSourceStateException(state));
                    }
                }
            }
        });
    }
}
