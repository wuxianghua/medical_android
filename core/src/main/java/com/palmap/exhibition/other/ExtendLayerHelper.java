package com.palmap.exhibition.other;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.palmap.library.utils.FileUtils;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.core.Value;
import com.palmaplus.nagrand.data.BasicElement;
import com.palmaplus.nagrand.data.Feature;
import com.palmaplus.nagrand.data.MapElement;
import com.palmaplus.nagrand.geos.GeometryFactory;
import com.palmaplus.nagrand.geos.Point;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.layer.FeatureLayer;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by 王天明 on 2016/10/11.
 * <p>
 * 添加一些额外的图层 辅助地图显示,读取assets/json/extendLayerConfig.json配置
 */
public class ExtendLayerHelper {

    private String configJson = null;
    private ConfigModel configModel;

    public ExtendLayerHelper(Context context) {
        try {
            configJson = FileUtils.readFileFromAssets(context, "json/extendLayerConfig.json");
        } catch (Exception e) {
            configJson = null;
        }
    }

    public void attachExtendLayerOnFloorChanged(MapView mapView, long floorId) {
        //读取配置文件
        if (TextUtils.isEmpty(configJson)) return;
        if (configModel == null) {
            Type collectionType = new TypeToken<ConfigModel>() {
            }.getType();
            configModel = new Gson().fromJson(configJson, collectionType);
        }
        if (configModel == null || configJson.isEmpty()) return;
        if (!configModel.checkArgs()) return;
        for (ConfigModel.ExtendLayerBean bean : configModel.getExtendLayer()) {
            if (!bean.checkArgs()) continue;
            FeatureLayer tempLayer = new FeatureLayer(bean.getFeatureLayerName());
            boolean layerIsAdd = false;
            for (ConfigModel.ExtendLayerBean.FeaturesBean featuresBean : bean.getFeatures()) {
                if (featuresBean.getFloorId() != floorId && featuresBean.getFloorId() != 0)
                    continue;
                if (!featuresBean.checkArgs()) continue;
                if (!layerIsAdd) {
                    LogUtil.e("添加额外层:" + bean.getFeatureLayerName());
                    mapView.addLayer(tempLayer);
                    mapView.setLayerOffset(tempLayer);
                    layerIsAdd = true;
                }
                Value tempValue = (featuresBean.getKey().trim().toLowerCase().equals("category") || featuresBean.getKey().trim().toLowerCase().equals("id"))
                        ? new Value(Long.parseLong(featuresBean.getValue())) : new Value(featuresBean.getValue());
                List<Feature> tempResult = mapView.searchFeature("Area", featuresBean.getKey(), tempValue);
                if (null != tempResult && tempResult.size() != 0) {
                    for (long i = 0; i < tempResult.size(); i++) {
                        Point point = GeometryFactory.createPoint(tempResult.get((int) i).getCentroid());
                        MapElement mapElement = new MapElement();
                        mapElement.addElement("id", new BasicElement(i)); // 1L为特征点ID，下面需要
                        Feature locationFeature = new Feature(point, mapElement);
                        tempLayer.addFeature(locationFeature);
                    }
                }
            }
        }

        for (ConfigModel.ChangeColorPoisBean bean : configModel.getChangeColorPois()) {
            if (!bean.checkArgs()) continue;
            LogUtil.e("color:" + bean.getColor());
            mapView.setRenderableColor("Area", bean.getId(), Color.parseColor(bean.getColor()));
        }
    }

    public List<ConfigModel.ChangeColorPoisBean> getChangeColorPoiBeans() {
        if (configModel == null) return null;
        return configModel.getChangeColorPois();
    }

    public ConfigModel.ChangeColorPoisBean getChangeColorPoiBeanWithId(long id) {
        if (configModel == null) return null;
        List<ConfigModel.ChangeColorPoisBean> data = configModel.getChangeColorPois();
        if (data == null) return null;
        for (ConfigModel.ChangeColorPoisBean bean : data) {
            if (bean.getId() == id) {
                return bean;
            }
        }
        return null;
    }

    public class ConfigModel {
        /**
         * featureLayerName : testWtm
         * features : [{"floorId":1048860,"key":"category","value":"23003000"}]
         */
        private List<ExtendLayerBean> extendLayer;
        /**
         * id : 1048916
         * color : 0xffff0000
         */
        private List<ChangeColorPoisBean> changeColorPois;

        List<ExtendLayerBean> getExtendLayer() {
            return extendLayer;
        }

        public void setExtendLayer(List<ExtendLayerBean> extendLayer) {
            this.extendLayer = extendLayer;
        }

        List<ChangeColorPoisBean> getChangeColorPois() {
            return changeColorPois;
        }

        public void setChangeColorPois(List<ChangeColorPoisBean> changeColorPois) {
            this.changeColorPois = changeColorPois;
        }

        boolean checkArgs() {
            return !((extendLayer == null || extendLayer.size() == 0) && (changeColorPois == null || changeColorPois.size() == 0));
        }

        public class ExtendLayerBean {
            private String featureLayerName;
            /**
             * floorId : 1048860
             * key : category
             * value : 23003000
             */
            private List<FeaturesBean> features;

            boolean checkArgs() {
                return !TextUtils.isEmpty(featureLayerName) && !(features == null || features.size() == 0);
            }


            String getFeatureLayerName() {
                return featureLayerName;
            }

            public void setFeatureLayerName(String featureLayerName) {
                this.featureLayerName = featureLayerName;
            }

            List<FeaturesBean> getFeatures() {
                return features;
            }

            public void setFeatures(List<FeaturesBean> features) {
                this.features = features;
            }

            class FeaturesBean {
                private int floorId;
                private String key;
                private String value;

                public int getFloorId() {
                    return floorId;
                }

                public void setFloorId(int floorId) {
                    this.floorId = floorId;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                boolean checkArgs() {
                    return !TextUtils.isEmpty(key) && !TextUtils.isEmpty(value);
                }
            }
        }

        public class ChangeColorPoisBean {
            private int id;
            private String color;

            boolean checkArgs() {
                return !(id == 0 || TextUtils.isEmpty(color));
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getColor() {
                if (this.color.startsWith("0x")) {
                    return "#" + this.color.substring(2, this.color.length());
                }
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }
        }
    }

}
