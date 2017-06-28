package com.palmap.exhibition.dao.business;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
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

    /********************************数据库存储********************************/
//      private Context context;
//
//    private HistoryPoiSearchDao dao;
//
//    public HistoryPoiSearchBusiness(Context context) {
//        checkNotNull(context, "context == null!!!");
//        //this.context = context;
//        dao = DBUtils.getDaoSession(context)
//                .getHistoryPoiSearchDao();
//    }
//
//    public void insert(String name){
//        if (contains(name)) return;
//        HistoryPoiSearch search = new HistoryPoiSearch();
//        search.setAppKey(Config.APP_KEY);
//        search.setPoiKeyWord(name);
//        dao.insert(search);
//    }
//
//    public List<HistoryPoiSearch> findAll(String name) {
//        return dao.queryBuilder()
//                .where(HistoryPoiSearchDao.Properties.AppKey.eq(Config.APP_KEY), HistoryPoiSearchDao.Properties.PoiKeyWord.eq(name))
//                .build()
//                .list();
//    }
//
//    public List<HistoryPoiSearch> findAll() {
//        return dao.queryBuilder()
//                .where(HistoryPoiSearchDao.Properties.AppKey.eq(Config.APP_KEY))
//                .build()
//                .list();
//    }
//
//    public Observable<List<HistoryPoiSearch>> find_All(){
//        return Observable.create(new ObservableOnSubscribe<List<HistoryPoiSearch>>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<List<HistoryPoiSearch>> e) throws Exception {
//                e.onNext(findAll());
//                e.onComplete();
//            }
//        });
//    }
//
//    /**
//     * 是否已经有了
//     * @param name
//     * @return
//     */
//    public boolean contains(String name) {
//        return !(findAll(name).size() == 0);
//    }
//
//    public void delete(String namekeyWord) {
//        for (HistoryPoiSearch historyPoiSearch : findAll(namekeyWord)) {
//            dao.delete(historyPoiSearch);
//        }
//    }
//
//    public void deleteAll() {
//        dao.deleteAll();
//    }

    /********************************SharedPreferences存储********************************/
    private SharedPreferences sp = null;
    private List<String> mHistoryRecords = new ArrayList<>();
    private final int RECORD_COUNT = 5;

    public HistoryPoiSearchBusiness(Context context) {
        checkNotNull(context, "context == null!!!");
        sp = context.getSharedPreferences("SP_FILE", Context.MODE_PRIVATE);
    }

    public void insert(String keyWord) {
        SharedPreferences.Editor editor = sp.edit();
        if (contains(keyWord)) {
            for (int i = 0; i < mHistoryRecords.size(); i++) {
                editor.remove("History_" + i);
            }
            editor.remove("History_Size");
            mHistoryRecords.remove(keyWord);
            mHistoryRecords.add(keyWord);
            editor.putInt("History_Size", mHistoryRecords.size());
            for (int i = 0; i < mHistoryRecords.size(); i++) {
                editor.putString(
                        "History_" + i, mHistoryRecords.get(i));
            }
        } else {
            if (mHistoryRecords.size() < RECORD_COUNT) {
                mHistoryRecords.add(keyWord);
                editor.putString("History_" + (mHistoryRecords.size() - 1), keyWord)
                        .putInt("History_Size", mHistoryRecords.size());
            } else {
                for (int i = 0; i < mHistoryRecords.size(); i++) {
                    editor.remove("History_" + i);
                }
                editor.remove("History_Size");
                mHistoryRecords.remove(0);
                mHistoryRecords.add(keyWord);
                editor.putInt("History_Size", mHistoryRecords.size());
                for (int i = 0; i < mHistoryRecords.size(); i++) {
                    editor.putString("History_" + i, mHistoryRecords.get(i));
                }
            }
        }
        editor.apply();
    }

    public List<String> findAll() {
        mHistoryRecords.clear();
        int count = sp.getInt("History_Size", 0);
        for (int i = 0; i < count; i++) {
            String keyWord = sp.getString("History_" + i, "");
            if (keyWord.isEmpty()) {
                continue;
            }
            mHistoryRecords.add(keyWord);
        }
        return mHistoryRecords;
    }

    public Observable<List<String>> find_All() {
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
                e.onNext(findAll());
                e.onComplete();
            }
        });
    }

    /**
     * 是否已经有了
     *
     * @param keyWord
     * @return
     */
    public boolean contains(String keyWord) {
        return mHistoryRecords.contains(keyWord);
    }

    public boolean delete(String keyWord) {
        int index = mHistoryRecords.indexOf(keyWord);
        if (index < 0) {
            return false;
        }
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < mHistoryRecords.size(); i++) {
            editor.remove("History_" + i);
        }
        editor.remove("History_Size");
        mHistoryRecords.remove(index);
        editor.putInt("History_Size", mHistoryRecords.size());
        for (int i = 0; i < mHistoryRecords.size(); i++) {
            editor.putString("History_" + i, mHistoryRecords.get(i));
        }
        editor.apply();
        return true;
    }

    public void add(List<String> data) {
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < mHistoryRecords.size(); i++) {
            editor.remove("History_" + i);
        }
        editor.remove("History_Size");
        mHistoryRecords.addAll(data);
        editor.putInt("History_Size", mHistoryRecords.size());
        for (int i = 0; i < mHistoryRecords.size(); i++) {
            editor.putString("History_" + i, mHistoryRecords.get(i));
        }
        editor.apply();
    }

    public void deleteAll() {
        mHistoryRecords.clear();
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < mHistoryRecords.size(); i++) {
            editor.remove("History_" + i);
        }
        editor.remove("History_Size");
        editor.apply();
    }

}
