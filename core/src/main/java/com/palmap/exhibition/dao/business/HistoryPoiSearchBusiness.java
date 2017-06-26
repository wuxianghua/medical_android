package com.palmap.exhibition.dao.business;

import android.content.Context;

import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.dao.HistoryPoiSearch;
import com.palmap.exhibition.dao.HistoryPoiSearchDao;
import com.palmap.exhibition.dao.utils.DBUtils;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static com.palmap.library.utils.Preconditions.checkNotNull;
/**
 * Created by 王天明 on 2016/5/9.
 */
public class HistoryPoiSearchBusiness {

    //private Context context;

    private HistoryPoiSearchDao dao;

    public HistoryPoiSearchBusiness(Context context) {
        checkNotNull(context, "context == null!!!");
        //this.context = context;
        dao = DBUtils.getDaoSession(context)
                .getHistoryPoiSearchDao();
    }

    public void insert(String name){
        if (contains(name)) return;
        HistoryPoiSearch search = new HistoryPoiSearch();
        search.setAppKey(Config.APP_KEY);
        search.setPoiKeyWord(name);
        dao.insert(search);
    }

    public List<HistoryPoiSearch> findAll(String name) {
        return dao.queryBuilder()
                .where(HistoryPoiSearchDao.Properties.AppKey.eq(Config.APP_KEY), HistoryPoiSearchDao.Properties.PoiKeyWord.eq(name))
                .build()
                .list();
    }

    public List<HistoryPoiSearch> findAll() {
        return dao.queryBuilder()
                .where(HistoryPoiSearchDao.Properties.AppKey.eq(Config.APP_KEY))
                .build()
                .list();
    }

    public Observable<List<HistoryPoiSearch>> find_All(){
        return Observable.create(new Observable.OnSubscribe<List<HistoryPoiSearch>>() {
            @Override
            public void call(Subscriber<? super List<HistoryPoiSearch>> subscriber) {
                subscriber.onNext(findAll());
                subscriber.onCompleted();
            }
        });
    }

    /**
     * 是否已经有了
     * @param name
     * @return
     */
    public boolean contains(String name) {
        return !(findAll(name).size() == 0);
    }

    public void delete(String namekeyWord) {
        for (HistoryPoiSearch historyPoiSearch : findAll(namekeyWord)) {
            dao.delete(historyPoiSearch);
        }
    }

    public void deleteAll() {
        dao.deleteAll();
    }
}
