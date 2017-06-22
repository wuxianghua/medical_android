package com.palmap.exhibition.repo;


/**
 * Created by wtm on 2017/1/3.
 */
public interface LocationRepo {

    void startLocation();

    void stopLocation();

    void addListener(LocationListener locationListener);

    void removeListener(LocationListener locationListener);
}
