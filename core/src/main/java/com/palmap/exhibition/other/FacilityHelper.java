package com.palmap.exhibition.other;

import android.text.TextUtils;

import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.model.FacilityModel;
import com.palmap.library.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by 王天明 on 2016/6/22.
 * <p/>
 * 设施帮助类
 */
public class FacilityHelper {

    private static HashMap<Long, FacilityModel> facilityData;

    static {
        String jsonStr = FileUtils.readFileFromAssets(AndroidApplication.getInstance(), "json/Facility.json");
        //解析json
//        LogUtil.e(jsonStr);
        if (!TextUtils.isEmpty(jsonStr)) {
            facilityData = new HashMap<>();
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        FacilityModel facilityModel = new FacilityModel();
                        facilityModel.setName(jsonObject.getString("name"));
                        long categoryId = jsonObject.getLong("categoryId");
                        facilityModel.setId(categoryId);
                        facilityData.put(categoryId, facilityModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据id判断改id是否是一个设施
     *
     * @param id
     */
    public static FacilityModel obtainFacilityType(long id) {
        if (null == facilityData) return null;
        return facilityData.get(id);
    }

}
