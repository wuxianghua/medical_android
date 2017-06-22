package com.palmap.exhibition.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.palmap.exhibition.dao.HistoryPoiSearch;
import com.palmap.exhibition.dao.CoordinateModel;
import com.palmap.exhibition.dao.ActivityModel;

import com.palmap.exhibition.dao.HistoryPoiSearchDao;
import com.palmap.exhibition.dao.CoordinateModelDao;
import com.palmap.exhibition.dao.ActivityModelDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig historyPoiSearchDaoConfig;
    private final DaoConfig coordinateModelDaoConfig;
    private final DaoConfig activityModelDaoConfig;

    private final HistoryPoiSearchDao historyPoiSearchDao;
    private final CoordinateModelDao coordinateModelDao;
    private final ActivityModelDao activityModelDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        historyPoiSearchDaoConfig = daoConfigMap.get(HistoryPoiSearchDao.class).clone();
        historyPoiSearchDaoConfig.initIdentityScope(type);

        coordinateModelDaoConfig = daoConfigMap.get(CoordinateModelDao.class).clone();
        coordinateModelDaoConfig.initIdentityScope(type);

        activityModelDaoConfig = daoConfigMap.get(ActivityModelDao.class).clone();
        activityModelDaoConfig.initIdentityScope(type);

        historyPoiSearchDao = new HistoryPoiSearchDao(historyPoiSearchDaoConfig, this);
        coordinateModelDao = new CoordinateModelDao(coordinateModelDaoConfig, this);
        activityModelDao = new ActivityModelDao(activityModelDaoConfig, this);

        registerDao(HistoryPoiSearch.class, historyPoiSearchDao);
        registerDao(CoordinateModel.class, coordinateModelDao);
        registerDao(ActivityModel.class, activityModelDao);
    }
    
    public void clear() {
        historyPoiSearchDaoConfig.getIdentityScope().clear();
        coordinateModelDaoConfig.getIdentityScope().clear();
        activityModelDaoConfig.getIdentityScope().clear();
    }

    public HistoryPoiSearchDao getHistoryPoiSearchDao() {
        return historyPoiSearchDao;
    }

    public CoordinateModelDao getCoordinateModelDao() {
        return coordinateModelDao;
    }

    public ActivityModelDao getActivityModelDao() {
        return activityModelDao;
    }

}
