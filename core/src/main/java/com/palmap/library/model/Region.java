package com.palmap.library.model;

import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhang on 2016/3/23.
 */
public class Region {
  private String name;
  private PointD[] points;

  public Region(){}

//  public Region(String name, PointD[] points) {
//    this.name = name;
//    this.points = points;
//  }

  public Region(String name, PointD ...point) {
    this.name = name;
    this.points = point;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PointD[] getPoints() {
    return points;
  }

  public void setPoints(PointD[] points) {
    this.points = points;
  }

  @Override
  public String toString() {
    return "Region{" +
            "name='" + name + '\'' +
            ", points=" + Arrays.toString(points) +
            '}';
  }

  /*
* 获取区域位置信息列表
* */
  public static List<Region> getRegionList(String data) throws JSONException {
    if (TextUtils.isEmpty(data)){
      return null;
    }

    JSONObject object = new JSONObject(data);
    JSONArray array = object.getJSONArray("regions");
    if (array != null && array.length() > 0){
      List<Region> regions = new ArrayList<Region>();
      Region region;
      List<PointD> points;
      PointD[] ps;
      JSONObject o;
      JSONArray a;
      PointD p;
      JSONArray a2;
      for (int i = 0, size = array.length(); i < size; i++){
        o = array.getJSONObject(i);
        region = new Region();
        region.setName(o.optString("name", "未知区域"));
        a = o.optJSONArray("coordinates");
        if (a != null && a.length() > 0){
          points = new ArrayList<PointD>();
          for (int j = 0, s = a.length(); j < s; j++){
            a2 = a.getJSONArray(j);
            p = new PointD(a2.optDouble(0, 0), a2.optDouble(1, 0));
            points.add(p);
          }
          ps = new PointD[points.size()];
          points.toArray(ps);
          region.setPoints(ps);
        }
        regions.add(region);
      }

      return regions;
    }

    return null;
  }

}
