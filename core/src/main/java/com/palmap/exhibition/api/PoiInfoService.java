package com.palmap.exhibition.api;

import com.palmap.exhibition.config.ServereConfig;
import com.palmap.exhibition.model.Api_PoiModel;
import com.palmap.exhibition.model.Api_SimpleInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by 天明 on 2016/6/16.
 */
public interface PoiInfoService {

    @Headers("Cache-Control: public,max-stale=180")
    @GET(ServereConfig.BOOTHINFO + "/{poiId}/{lang}/getSimpleInfo")
    Observable<Api_SimpleInfo> loadSimplePoiData(@Path("poiId") long id, @Path("lang") String lang);

    @Headers("Cache-Control: public,max-stale=180")
    @GET(ServereConfig.BOOTHINFO + "/{poiId}/{lang}")
    Observable<Api_PoiModel> loadPoiData(@Path("poiId") long id, @Path("lang") String lang);

}