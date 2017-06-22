package com.palmap.exhibition.view;

import com.palmap.exhibition.model.ExFloorModel;

import java.util.List;

/**
 * Created by 王天明 on 2016/10/17.
 */

public interface FloorDataProvides {


    List<ExFloorModel> floorModels();

    String getFloorNameById(long floorId);

}
