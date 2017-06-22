package com.palmap.library.model;

/**
 * Created by zhang on 2015/11/24.
 */
public class PointD {
  public double x;
  public double y;

  public PointD() {
  }

  public PointD(double x, double y){
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "PointD{" +
            "x=" + x +
            ", y=" + y +
            '}';
  }
}
