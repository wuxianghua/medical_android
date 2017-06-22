package com.palmap.exhibition.api;


import com.palmap.exhibition.model.LocationInfoModel;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wtm on 2017/1/3.
 * 使用华为基站定位api
 */
public interface LocationService {
    @GET("https://h2.ipalmap.com/pos")
    Observable<LocationInfoModel> requestLocation(
            @Query("appKey") String appKey,
            @Query("idType") String idType,
            @Query("idData") String idData);

    /**
     * 获取测试定位数据
     * @param idType
     * @param idData
     * @return
     */
    @GET("http://123.59.132.198:8080/comet/pos")
    Observable<LocationInfoModel> requestLocationTest(
            @Query("idType") String idType,
            @Query("idData") String idData);

}
