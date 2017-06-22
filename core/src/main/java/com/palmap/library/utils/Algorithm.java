package com.palmap.library.utils;


import com.palmap.library.model.PointD;

public class Algorithm {
    /**
     * 射线方法
     * 判断一个点在不在一个多边形内
     * @param points 多边形点集合
     * @param point 参考点
     * @return
     */
    public static boolean insidePolygon(PointD[] points, PointD point) {
        if (points == null || point == null || points.length == 0) { // 参数验证
            return false;
        }
        int n = points.length;
        int i, j;
        boolean result = false;
        PointD p1, p2;
        for (i = 0, j = n - 1; i < n; j = i++) {
            p1 = points[i];
            p2 = points[j];
            if (((p1.y <= point.y) && (point.y < p2.y))
                    || ((p2.y <= point.y) && (point.y < p1.y))) {
                if (point.x < ((p2.x - p1.x) * (point.y - p1.y) / (p2.y - p1.y) + p1.x)) {
                    result = !result;
                }
            }
        }
        return result;
    }

    public static boolean insidePolygon(PointD[] points, double x,double y) {
        return insidePolygon(points,new PointD(x,y));
    }

}
