package com.palmap.library.rx.dataSource;

import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.data.DataList;
import com.palmaplus.nagrand.data.DataSource;
import com.palmaplus.nagrand.data.LocationList;
import com.palmaplus.nagrand.data.LocationModel;
import com.palmaplus.nagrand.data.LocationPagingList;
import com.palmaplus.nagrand.data.MapModel;
import com.palmaplus.nagrand.data.PlanarGraph;

import io.reactivex.Observable;


/**
 * Created by 王天明 on 2016/4/26.
 * DataSource类rx封装体
 */
public final class RxDataSource {

    /**
     * 请求maps数据
     *
     * @param dataSource
     * @return
     */
    public static Observable<DataList<MapModel>> requestMaps(DataSource dataSource) {
        return Observable.create(new DataSourceMapsOnSubscribe(dataSource));
    }

    /**
     * 请求map数据
     *
     * @param dataSource
     * @return
     */
    public static Observable<MapModel> requestMap(DataSource dataSource, long id) {
        return Observable.create(new DataSourceMapOnSubscribe(dataSource, id));
    }

    /**
     * 根据poiId请求这个POI下所有孩子的集合，比如传入的如果是一个MapID，那么返回的就是这个MapModel 对应的所有FloorModel信息
     *
     * @param dataSource
     * @param id
     * @return
     */
    public static Observable<LocationList> requestPOIChildren(DataSource dataSource,long id) {
        return Observable.create(new DataSourcePOIChildrenOnSubscribe(dataSource,id));
    }

    /**
     * 请求附近poi
     * @param dataSource
     * @param id
     * @return
     */
    public static Observable<LocationModel> requestPOI(DataSource dataSource, long id) {
        return Observable.create(new DataSourcePOIOnSubscribe(dataSource,id));
    }

    /**
     * 请求平面渲染图像
     *
     * @param dataSource
     * @param floorId
     * @return
     */
    public static Observable<PlanarGraph> requestPlanarGraph(DataSource dataSource, long floorId) {
        LogUtil.e("floorid:" + floorId);
        return Observable.create(new DataSourcePlanarGraphOnSubscribe(dataSource,floorId));
    }

    /**
     * 根据关键字搜索
     * @param dataSource
     * @param keyWord
     * @param start 第几条开始
     * @param number 一共要几条
     * @param parents 他父POI id，比如只把范围缩小到某一个地图，这里可以传入一个MapID
     * @param categories  满足符合条件的Category
     * @return
     */
    public static Observable<LocationPagingList> search(DataSource dataSource, String keyWord, int start, int number, long[] parents, long[] categories){
        return Observable.create(new DataSourceSearchOnSubscribe(dataSource, keyWord, start,number,parents,categories));
    }

}
