package com.palmap.exhibition.dao.business;

import android.content.Context;

import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.dao.HistoryPoiSearch;
import com.palmap.exhibition.dao.HistoryPoiSearchDao;
import com.palmap.exhibition.dao.utils.DBUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

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
        return Observable.create(new ObservableOnSubscribe<List<HistoryPoiSearch>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<HistoryPoiSearch>> e) throws Exception {
                e.onNext(findAll());
                e.onComplete();
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

    public void delete(String nameKeyWord) {
        for (HistoryPoiSearch historyPoiSearch : findAll(nameKeyWord)) {
            dao.delete(historyPoiSearch);
        }
    }

    public void deleteAll() {
        dao.deleteAll();
    }
}
