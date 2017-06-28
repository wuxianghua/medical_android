package com.palmap.exhibition.repo;

import com.palmap.exhibition.model.LocationInfoModel;

/**
 * Created by wtm on 2017/1/3.
 */

public interface LocationListener {

    void onComplete(LocationInfoModel locationInfoModel, long timeStamp);

    void onFailed(Exception ex, String msg);

    void onMockLocation(LocationInfoModel locationInfoModel, long timeStamp);

}
