package com.palmap.exhibition.other;

import android.text.TextUtils;

import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.R;
import com.palmap.exhibition.model.FacilityModel;
import com.palmap.library.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王天明 on 2016/6/22.
 * <p/>
 * 设施过滤类
 * <p/>
 * 用来获取某一类的categoryid
 * 由FilterFacility.json控制
 */
public class FacilityFilterHelper {


    private static List<FacilityModel> facilityData;

    static {
        String jsonStr = FileUtils.readFileFromAssets(AndroidApplication.getInstance(), "json/FilterFacility.json");
        //解析json
        if (!TextUtils.isEmpty(jsonStr)) {
            facilityData = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                int[] resIdArr = {
                        R.drawable.bg_facilities_escalator,
                        R.drawable.bg_facilities_pay,
                        R.drawable.bg_facilities_toilet,
                        R.drawable.bg_facilities_entrance,
                };

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        JSONArray categoryids = jsonObject.getJSONArray("categoryids");
                        ArrayList<Long> categoryidList = new ArrayList<>();
                        for (int k = 0; k < categoryids.length(); k++) {
                            categoryidList.add(categoryids.getLong(k));
                        }
                        if (i <= resIdArr.length) {
                            facilityData.add(new FacilityModel(name, categoryidList,resIdArr[i]));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<FacilityModel> getFacilityData() {
        return facilityData;
    }
}
