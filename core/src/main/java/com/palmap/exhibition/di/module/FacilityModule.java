package com.palmap.exhibition.di.module;

import com.palmap.exhibition.model.FacilityModel;
import com.palmap.exhibition.model.ServiceFacilityModel;
import com.palmap.exhibition.other.FacilityFilterHelper;

import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 王天明 on 2016/6/6.
 */
@Module
public class FacilityModule {

    /**
     * 全部隐藏的ID
     */
    public static final int ID_NONE = -1;

/*    @Provides
    public List<FacilityModel> providesPubFacilityList() {
        List<FacilityModel> data = new ArrayList<>();
        int[][] resIdArr = {
//                {R.mipmap.facility_none,ID_NONE},
                {R.drawable.icon_pub_rest,13151000},
                {R.drawable.icon_ser_eat,11000000},
                {R.drawable.icon_ser_advice,23011000},
//                {R.drawable.icon_pub_ladder,24097000},
//                {R.drawable.icon_pub_elevator,24091000},
                {R.drawable.icon_pub_toilet,23024000},
        };
        for (int i = 0; i < resIdArr.length; i++) {
            data.add(new FacilityModel(resIdArr[i][1], resIdArr[i][0]));
        }
        return data;
    }*/

    @Provides
    public List<ServiceFacilityModel> providesServiceFacilityList() {
        return null;
    }


    @Provides
    public List<FacilityModel> providesPubFacilityList() {
        return FacilityFilterHelper.getFacilityData();
    }

}