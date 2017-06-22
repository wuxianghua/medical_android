package com.palmap.library.rx.dataSource;

import com.palmap.library.rx.exception.DataSourceStateException;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.LocationPagingList;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by 王天明 on 2016/5/11.
 */
public class DataSourceSearchOnSubscribe implements Observable.OnSubscribe<LocationPagingList> {

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
    public void call(final Subscriber<? super LocationPagingList> subscriber) {
        dataSource.search(keyWord,start,number,parents,categories, new DataSource.OnRequestDataEventListener<LocationPagingList>() {
            @Override
            public void onRequestDataEvent(DataSource.ResourceState state, LocationPagingList data) {
                if (!subscriber.isUnsubscribed()) {
                    if (state == DataSource.ResourceState.OK|| state == DataSource.ResourceState.CACHE) {
                        subscriber.onNext(data);
                    } else {
                        subscriber.onError(new DataSourceStateException(state));
                    }
                }
            }
        });
    }
}
