package com.palmap.exhibition.dao.business;

import android.content.Context;

import com.palmap.exhibition.dao.CoordinateModel;
import com.palmap.exhibition.dao.CoordinateModelDao;
import com.palmap.exhibition.dao.utils.DBUtils;
import com.palmaplus.nagrand.geos.Coordinate;

import java.util.List;

import static com.palmap.library.utils.Preconditions.checkNotNull;

/**
 * Created by 王天明 on 2016/5/16.
 * 坐标表事物
 */
public class CoordinateBusiness {

    private CoordinateModelDao dao;

    public CoordinateBusiness(Context context) {
        checkNotNull(context, "context == null!!!");
        dao = DBUtils.getDaoSession(context)
                .getCoordinateModelDao();
    }

    /**
     * 保存一个坐标
     * @param atyId
     * @param pointId
     * @param coordinate 坐标
     * @return 返回插入的ID值
     */
    public long insert(String atyId,String pointId,Coordinate coordinate) {
        CoordinateModel coordinateModel = new CoordinateModel();
        coordinateModel.setAtyId(atyId);
        coordinateModel.setPointId(pointId);
        coordinateModel.setX(coordinate.getX());
        coordinateModel.setY(coordinate.getY());
        coordinateModel.setZ(coordinate.getZ());
        try {
            return dao.insert(coordinateModel);
        } catch (Exception e) {
            return -1;
        }
    }

    public List<CoordinateModel> findAll() {
        return dao.queryBuilder().list();
    }

    public List<CoordinateModel> findListByPointId(String pointId) {
        return dao.queryBuilder().where(CoordinateModelDao.Properties.PointId.eq(pointId)).build().list();
    }

    public List<CoordinateModel> findListByAtyId(String atyId) {
        return dao.queryBuilder().where(CoordinateModelDao.Properties.AtyId.eq(atyId)).build().list();
    }

    /**
     * 检查一个key的点是否存在
     *
     * @param pointId
     * @return
     */
    public boolean checkNullByPointId(String pointId) {
        List<CoordinateModel> coordinateModels = findListByPointId(pointId);
        return !(coordinateModels == null || coordinateModels.size() == 0);
    }

    public void deleteByTag(String key) {
        for (CoordinateModel c : findListByPointId(key)) {
            dao.delete(c);
        }
    }

    public void clear() {
        dao.deleteAll();
    }

}
