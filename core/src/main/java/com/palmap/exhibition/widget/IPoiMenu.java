package com.palmap.exhibition.widget;

import com.palmap.exhibition.model.PoiModel;
import com.palmap.exhibition.view.impl.PalmapViewState;

import java.util.List;

/**
 * Created by 王天明 on 2017/6/28.
 */
public interface IPoiMenu {

    void refreshView(PalmapViewState state);

    void refreshView(PoiModel poiModel, PalmapViewState state);

    void refreshView(List<PoiModel> poiModels, PalmapViewState state);
}
