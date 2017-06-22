package com.palmap.exhibition.dao.business;

import android.content.Context;

import com.palmap.exhibition.dao.ActivityModel;
import com.palmap.exhibition.dao.ActivityModelDao;
import com.palmap.exhibition.dao.utils.DBUtils;
import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.library.utils.StringUtils;

import java.util.List;

/**
 * Created by 王天明 on 2016/10/20.
 * 记录活动 事物
 */
public class ActivityInfoBusiness {

    //    private Context context;
    private ActivityModelDao dao;

    public ActivityInfoBusiness(Context context) {
        //this.context = context;
        dao = DBUtils.getDaoSession(context)
                .getActivityModelDao();
    }

    public long insert(Api_ActivityInfo.ObjBean info,int aimsCount) {
        if (info == null) return -1;
        ActivityModel model = new ActivityModel();
        model.setAtyId(info.getId());
        model.setFloorId(info.getFloorId());
        model.setBuildId(info.getBuildId());
        model.setActivityDesc(StringUtils.checkNullString(info.getActivityDesc()));
        model.setActivityName(StringUtils.checkNullString(info.getActivityName()));
        model.setActivityType(info.getActivityType());
        model.setMapId(info.getMapId());
        model.setPoiId(info.getPoiId());
        model.setRoomNumber(StringUtils.checkNullString(info.getRoomNumber()));
        model.setStartTime(StringUtils.checkNullString(info.getStartTime()));
        model.setEndTime(StringUtils.checkNullString(info.getEndTime()));
        model.setExt(StringUtils.checkNullString(info.getExt()));
        model.setState(0);
        model.setAimsCount(aimsCount);
        return dao.insert(model);
    }

    /**
     * 更新
     *
     * @param info
     * @param state 0 进行中 1 待接收 2 完成等待分享 3 分享完成
     */
    public void update(Api_ActivityInfo.ObjBean info, int state) {
        if (info == null) return;
        ActivityModel model = new ActivityModel();
        model.setAtyId(info.getId());
        model.setFloorId(info.getFloorId());
        model.setBuildId(info.getBuildId());
        model.setActivityDesc(StringUtils.checkNullString(info.getActivityDesc()));
        model.setActivityName(StringUtils.checkNullString(info.getActivityName()));
        model.setActivityType(info.getActivityType());
        model.setMapId(info.getMapId());
        model.setPoiId(info.getPoiId());
        model.setRoomNumber(StringUtils.checkNullString(info.getRoomNumber()));
        model.setStartTime(StringUtils.checkNullString(info.getStartTime()));
        model.setEndTime(StringUtils.checkNullString(info.getEndTime()));
        model.setExt(StringUtils.checkNullString(info.getExt()));
        model.setState(state);
        dao.update(model);
    }

    public void updateStateWitiAtyId(int atyId, int state) {
        List<ActivityModel> list = findListByAtyId(atyId);
        if (list.size() > 0) {
            list.get(0).setState(state);
        }
        dao.update(list.get(0));
    }

    public List<ActivityModel> findListByAtyId(int atyId) {
        return dao.queryBuilder().where(ActivityModelDao.Properties.AtyId.eq(atyId)).build().list();
    }

    public ActivityModel findOneByAtyId(int atyId) {
        List<ActivityModel> list = findListByAtyId(atyId);
        if (list.size() >0) {
            return list.get(0);
        }
        return null;
    }

    public int findCountByAtyId(int atyId) {
        return dao.queryBuilder().where(ActivityModelDao.Properties.AtyId.eq(atyId)).build().list().size();
    }

    public List<ActivityModel> findAll() {
        return dao.queryBuilder().list();
    }

    /**
     * 检查是否保存了这个ID
     *
     * @return
     */
    public boolean checkExisted(int atyId) {
        return !(findListByAtyId(atyId).size() == 0);
    }

    public void delete(int atyId) {
        List<ActivityModel> listByAtyId = findListByAtyId(atyId);
        if (listByAtyId != null && listByAtyId.size() > 0) {
            for (ActivityModel m : listByAtyId) {
                dao.delete(m);
            }
        }
    }

    public void clear() {
        dao.deleteAll();
    }

}
