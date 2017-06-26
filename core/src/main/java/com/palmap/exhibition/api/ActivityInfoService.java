package com.palmap.exhibition.api;

import com.palmap.exhibition.config.ServereConfig;
import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.exhibition.model.Api_PositionInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by 王天明 on 2016/9/19.
 * 活动信息接口
 */
public interface ActivityInfoService {

    /**
     * 获取活动信息
     * 缓存30分钟
     *
     * @param language
     * @param mapId
     * @param activityType 0:议程 1:活动 2:点亮活动
     * @param keyword
     * @param isExt
     * @return
     */
    @Headers("Cache-Control: public,max-stale=1800")
    @GET(ServereConfig.ACTIVITYINFO + "/{lang}/search")
    Observable<Api_ActivityInfo> requestActivityInfo(@Path("lang") String language,
                                                     @Query("mapId") long mapId,
                                                     @Query("activityType") String activityType,
                                                     @Query("keyword") String keyword,
                                                     @Query("isExt") boolean isExt);

    /**
     * 获取对应楼层的活动信息
     *
     * @param language
     * @param mapId
     * @param excludeTypes
     * @param isExt
     * @return
     */
    @Headers("Cache-Control: public,max-stale=1800")
    @GET(ServereConfig.ACTIVITYINFO + "/floor/{floorId}/{lang}/")
    Observable<Api_ActivityInfo> requestActivityInfoWithFloorId(@Path("lang") String language,
                                                                @Path("floorId") long floorId,
                                                                @Query("mapId") long mapId,
                                                                @Query("activityType") String activityType,
                                                                @Query("excludeTypes") int excludeTypes[],//排除的活动类型
                                                                @Query("isExt") boolean isExt);

    /**
     * 获取点亮活动的位置点
     *
     * @param mapId
     * @param atyId 活动ID 如果不传那就是-1
     * @return
     */
    @Headers("Cache-Control: public,max-stale=1800")
    @GET(ServereConfig.POSITIONINFO + "/{mapId}/list")
    Observable<Api_PositionInfo> requestPositionInfo(@Path("mapId") long mapId,
                                                     @Query("activityId") Integer atyId);

}