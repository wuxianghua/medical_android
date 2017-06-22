package com.palmap.exhibition.view;

import com.palmap.exhibition.model.Api_PoiModel;
import com.palmap.library.view.LoadDataView;

/**
 * Created by 王天明 on 2016/6/23.
 */
public interface PoiInfoView extends LoadDataView{

    void readPoiData(Api_PoiModel poiModel);

    void poiDataError(Api_PoiModel api_poiModel);

}
